package com.cloudcreativity.wankeshop.shop;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.collect.ShopCollectActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityShopGoodsListBinding;
import com.cloudcreativity.wankeshop.databinding.ItemHomeGoodsListItemBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;
import com.cloudcreativity.wankeshop.entity.GoodsWrapper;
import com.cloudcreativity.wankeshop.entity.ShopEntity;
import com.cloudcreativity.wankeshop.goods.GoodsDetailActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是商铺商品列表ViewModal
 */
public class ShopGoodsListViewModal {
    private ShopGoodsListActivity context;
    private BaseDialogImpl baseDialog;
    public ShopEntity shop;
    private ActivityShopGoodsListBinding binding;

    private int pageNum = 1;

    private boolean isLoadMore = false;

    public BaseBindingRecyclerViewAdapter<GoodsEntity,ItemHomeGoodsListItemBinding> adapter;

    public ObservableField<Boolean> isCollect = new ObservableField<>();

    ShopGoodsListViewModal(ShopGoodsListActivity context, BaseDialogImpl baseDialog,ActivityShopGoodsListBinding binding, ShopEntity entity) {
        this.context = context;
        this.baseDialog = baseDialog;
        this.shop = entity;
        this.binding = binding;

        this.isCollect.set(this.shop.getIsCollect()==1);


        adapter = new BaseBindingRecyclerViewAdapter<GoodsEntity, ItemHomeGoodsListItemBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_home_goods_list_item;
            }

            @Override
            protected void onBindItem(ItemHomeGoodsListItemBinding binding, final GoodsEntity item, int position) {
                binding.setGoodsItem(item);
//                    binding.getRoot().setLayoutParams(params);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),GoodsDetailActivity.class);
                        intent.putExtra("spuId",item.getId());
                        context.startActivity(intent);
                    }
                });
            }
        };

        this.binding.rcvShopGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int itemCount = recyclerView.getLayoutManager().getItemCount();

                if(dy>0 && visibleItemPosition>itemCount-8 && !isLoadMore){
                    loadData(pageNum);
                    isLoadMore = true;
                }
            }
        });

        loadData(pageNum);
    }

    public void onBack(View view){
        context.finish();
    }

    private void loadData(final int page){
        HttpUtils.getInstance().getGoodsByShop(page,shop.getStores().getShopId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        GoodsWrapper goodsWrapper = new Gson().fromJson(t, GoodsWrapper.class);
                        if(goodsWrapper.getData()!=null&&!goodsWrapper.getData().isEmpty()){
                            if(pageNum==1){
                                adapter.getItems().clear();
                            }
                            adapter.getItems().addAll(goodsWrapper.getData());
                            pageNum++;
                        }else{
                            if(page==1){
                                ToastUtils.showShortToast(context,R.string.str_no_data);
                            }
                        }
                        isLoadMore = false;
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                    }
                });
    }

    @BindingAdapter("shopBg")
    public static void loadBg(ImageView view, String url){
        GlideUtils.load(view.getContext(),url,view);
    }

    @BindingAdapter("shopIcon")
    public static void loadIcon(ImageView view,String url){
        GlideUtils.loadCircle(view.getContext(),url,view);
    }

    //收藏点击
    public void onCollectClick(View view){
        if(isCollect.get()){
            //取消收藏
            HttpUtils.getInstance().editCollectionForOther(shop.getId(),1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            ToastUtils.showShortToast(context,"已取消收藏");
                            isCollect.set(false);
                            //在这里发送消息
                            Map<String,Object> data = new HashMap<>();
                            data.put("msg_collect", ShopCollectActivity.MSG_REMOVE_COLLECT_ITEM);
                            data.put("id",shop.getId());
                            EventBus.getDefault().post(data);
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {
                            isCollect.set(true);
                        }
                    });
        }else{
            //添加收藏
            HttpUtils.getInstance().addCollection(1,shop.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            ToastUtils.showShortToast(context,"已收藏");
                            isCollect.set(true);
                            //在这里发送消息
                            Map<String,Object> data = new HashMap<>();
                            data.put("msg_collect", ShopCollectActivity.MSG_REFRESH_COLLECT);
                            EventBus.getDefault().post(data);
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {
                            isCollect.set(false);
                        }
                    });
        }
    }
}
