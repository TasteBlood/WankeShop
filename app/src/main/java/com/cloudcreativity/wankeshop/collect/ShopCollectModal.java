package com.cloudcreativity.wankeshop.collect;

import android.content.Intent;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityShopCollectBinding;
import com.cloudcreativity.wankeshop.databinding.ItemShopCollectLayoutBinding;
import com.cloudcreativity.wankeshop.entity.CollectEntity;
import com.cloudcreativity.wankeshop.entity.CollectEntityWrapper;
import com.cloudcreativity.wankeshop.shop.ShopGoodsListActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是店铺收藏ViewModal
 */
public class ShopCollectModal {
    private ShopCollectActivity context;
    private ActivityShopCollectBinding binding;

    private BaseDialogImpl baseDialog;
    private int pageNum = 1;

    public BaseBindingRecyclerViewAdapter<CollectEntity,ItemShopCollectLayoutBinding> adapter;

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


    ShopCollectModal(ShopCollectActivity context, ActivityShopCollectBinding binding) {
        this.context = context;
        this.baseDialog = context;
        this.binding = binding;
        this.binding.refreshShopCollect.setOnRefreshListener(refreshListenerAdapter);

        adapter = new BaseBindingRecyclerViewAdapter<CollectEntity, ItemShopCollectLayoutBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_shop_collect_layout;
            }

            @Override
            protected void onBindItem(ItemShopCollectLayoutBinding binding, final CollectEntity item, int position) {
                binding.setCollect(item);
                binding.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpUtils.getInstance().editCollection(item.getId(),1)
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
                        skipToDetail(item);
                    }
                });
            }
        };

        this.binding.refreshShopCollect.startRefresh();
    }

    //跳转到详情页面
    private void skipToDetail(CollectEntity item) {
        Intent intent = new Intent(context, ShopGoodsListActivity.class);
        intent.putExtra("shop",item.getShop());
        context.startActivity(intent);
    }

    public void onBack(View view){
        context.finish();
    }


    private void loadData(final int page){
        HttpUtils.getInstance().getCollections(page,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        if(page==1){
                            binding.refreshShopCollect.finishRefreshing();
                        }else{
                            binding.refreshShopCollect.finishLoadmore();
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
                            binding.refreshShopCollect.finishRefreshing();
                        }else{
                            binding.refreshShopCollect.finishLoadmore();
                        }
                    }
                });
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
