package com.cloudcreativity.wankeshop.collect;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityGoodsCollectBinding;
import com.cloudcreativity.wankeshop.entity.CollectEntity;
import com.cloudcreativity.wankeshop.entity.CollectEntityWrapper;
import com.cloudcreativity.wankeshop.goods.GoodsDetailActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.cloudcreativity.wankeshop.databinding.ItemGoodsCollectLayoutBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是商品收藏ViewModal
 */
public class GoodsCollectModal {
    private GoodsCollectActivity context;
    private ActivityGoodsCollectBinding binding;
    private BaseDialogImpl baseDialog;
    private int pageNum = 1;

    public BaseBindingRecyclerViewAdapter<CollectEntity,ItemGoodsCollectLayoutBinding> adapter;

    private RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
           pageNum = 1;
           loadData(pageNum);
        }

        @Override
        public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
            loadData(pageNum);
        }
    };


    GoodsCollectModal(GoodsCollectActivity context, ActivityGoodsCollectBinding binding) {
        this.context = context;
        this.binding = binding;
        this.baseDialog = context;
        this.binding.refreshGoodsCollect.setOnRefreshListener(refreshListenerAdapter);
        this.binding.refreshGoodsCollect.startRefresh();

        adapter = new BaseBindingRecyclerViewAdapter<CollectEntity, ItemGoodsCollectLayoutBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_goods_collect_layout;
            }

            @Override
            protected void onBindItem(ItemGoodsCollectLayoutBinding binding, final CollectEntity item, int position) {
                binding.setCollect(item);
                binding.tvSaleNum.setText(String.format(context.getString(R.string.str_sale_count),item.getSpu().getSaleNum()));
                binding.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpUtils.getInstance().editCollection(item.getId(),2)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                    @Override
                                    public void onSuccess(String t) {
                                        adapter.getItems().remove(item);
                                    }

                                    @Override
                                    public void onFail(ExceptionReason msg) {

                                    }
                                });
                    }
                });
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.getSpu().getSpuStatus()==0){
                            ToastUtils.showShortToast(context,"该商品暂时无法购买");
                        }else{
                            //跳转到商品详情页面
                            Intent intent = new Intent(context, GoodsDetailActivity.class);
                            intent.putExtra("spuId",item.getCollectId());
                            context.startActivity(intent);
                        }

                    }
                });
            }
        };
    }

    public void onBack(View view){
        context.finish();
    }

    private void loadData(final int page){
        HttpUtils.getInstance().getCollections(page,2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        if(page==1){
                            binding.refreshGoodsCollect.finishRefreshing();
                        }else{
                            binding.refreshGoodsCollect.finishLoadmore();
                        }
                        CollectEntityWrapper wrapper = new Gson().fromJson(t, CollectEntityWrapper.class);
                        if(wrapper.getData()==null||wrapper.getData().isEmpty()){
                            ToastUtils.showShortToast(context,R.string.str_no_data);
                            if(page==1)
                                adapter.getItems().clear();
                        }else{
                            if(page==1){
                                adapter.getItems().clear();
                                adapter.getItems().addAll(wrapper.getData());
                            }else{
                                adapter.getItems().addAll(wrapper.getData());
                            }
                            pageNum++;
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshGoodsCollect.finishRefreshing();
                        }else{
                            binding.refreshGoodsCollect.finishLoadmore();
                        }
                    }
                });
    }

    @BindingAdapter("collectGoodsPic")
    public static void display(ImageView imageView,String url){
        GlideUtils.load(imageView.getContext(),url,imageView);
    }

    public void onRemoveItem(int id) {
        for(CollectEntity entity : adapter.getItems()){
            if(entity.getCollectId()==id){
                adapter.getItems().remove(entity);
                break;
            }
        }
    }
}
