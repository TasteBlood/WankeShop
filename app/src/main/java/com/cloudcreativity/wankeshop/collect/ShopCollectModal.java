package com.cloudcreativity.wankeshop.collect;

import android.view.View;

import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityShopCollectBinding;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
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

        this.binding.refreshShopCollect.startRefresh();
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
}
