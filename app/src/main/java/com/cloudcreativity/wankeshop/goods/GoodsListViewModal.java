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
import com.cloudcreativity.wankeshop.databinding.ActivityGoodsListBinding;
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

public class GoodsListViewModal {
    private GoodsListActivity context;
    private BaseDialogImpl baseDialog;
    //这是标题
    public ObservableField<String> title = new ObservableField<>();
    private String style;
    private String type;
    private ActivityGoodsListBinding listBinding;
    private int pageNum = 2;

    public BaseBindingRecyclerViewAdapter<GoodsEntity,ItemHomeGoodsListItemBinding> adapter;

    public RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {

        @Override
        public void onRefresh(TwinklingRefreshLayout refreshLayout) {
            pageNum = 1;
            loadData(pageNum,style,type);
        }
        @Override
        public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
            loadData(pageNum,style,type);
        }

    };

    //加载数据
    private void loadData(int page, String style, String type) {
        //在这里进行数据请求
        HttpUtils.getInstance().getChanelContent(page,style,type)
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

    GoodsListViewModal(GoodsListActivity context, String title, String style, String type, ActivityGoodsListBinding listBinding,List<GoodsEntity> entityList) {
        this.context = context;
        this.baseDialog = context;
        this.title.set(title);
        this.style = style;
        this.type = type;
        this.listBinding = listBinding;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        int gridWidth = (int) ((metrics.widthPixels-25*metrics.density)/2);
//        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        adapter.getItems().addAll(entityList);
    }

    public void onBack(View view){
        context.finish();
    }
}
