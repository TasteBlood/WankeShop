package com.cloudcreativity.wankeshop.goods;

import android.content.Intent;
import android.databinding.ObservableField;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityGoodsBannerListBinding;
import com.cloudcreativity.wankeshop.databinding.ItemHomeGoodsListItemBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;
import com.cloudcreativity.wankeshop.entity.GoodsWrapper;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是banner点进来的商品列表
 */
public class GoodsListForBannerViewModal {
    private GoodsListForBannerActivity context;
    private BaseDialogImpl baseDialog;
    //这是标题
    public ObservableField<String> title = new ObservableField<>();
    private String keyWords;
    private String thirdClassId;
    private ActivityGoodsBannerListBinding listBinding;
    private int pageNum = 2;

    public BaseBindingRecyclerViewAdapter<GoodsEntity,ItemHomeGoodsListItemBinding> adapter;

    public RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {

        @Override
        public void onRefresh(TwinklingRefreshLayout refreshLayout) {
            pageNum = 1;
            loadData(pageNum,keyWords,thirdClassId);
        }
        @Override
        public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
            loadData(pageNum,keyWords,thirdClassId);
        }

    };

    //加载数据
    private void loadData(int page, String keyWords, String thirdClassId) {
        //在这里进行数据请求
        HttpUtils.getInstance().getBannerContent(page,keyWords,thirdClassId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        GoodsWrapper goodsWrapper = new Gson().fromJson(t, GoodsWrapper.class);
                        if(goodsWrapper.getData()!=null&&!goodsWrapper.getData().isEmpty()){
                            if(pageNum==1){
                                listBinding.refreshGoodsList.finishRefreshing();
                                adapter.getItems().clear();
                            }else{
                                listBinding.refreshGoodsList.finishLoadmore();
                            }
                            adapter.getItems().addAll(goodsWrapper.getData());
                            pageNum++;
                        }else{
                            if(pageNum==1){
                                listBinding.refreshGoodsList.finishRefreshing();
                            }else{
                                listBinding.refreshGoodsList.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(pageNum==1){
                            listBinding.refreshGoodsList.finishRefreshing();
                        }else{
                            listBinding.refreshGoodsList.finishLoadmore();
                        }
                    }
                });
    }

    GoodsListForBannerViewModal(GoodsListForBannerActivity context, String keyWords, String thirdClassId,
                                String title,ActivityGoodsBannerListBinding listBinding, List<GoodsEntity> entityList) {
        this.context = context;
        this.baseDialog = context;
        this.title.set(title);
        this.keyWords = keyWords;
        this.thirdClassId = thirdClassId;
        this.listBinding = listBinding;
        adapter = new BaseBindingRecyclerViewAdapter<GoodsEntity, ItemHomeGoodsListItemBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_home_goods_list_item;
            }

            @Override
            protected void onBindItem(ItemHomeGoodsListItemBinding binding, final GoodsEntity item, int position) {
                binding.setGoodsItem(item);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,GoodsDetailActivity.class);
                        intent.putExtra("spuId",item.getId());
                        context.startActivity(intent);
                    }
                });
            }
        };
        adapter.getItems().addAll(entityList);
    }

    public void onBack(View view){
        context.finish();
    }
}
