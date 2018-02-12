package com.cloudcreativity.wankeshop.collect;

import android.view.View;

import com.cloudcreativity.wankeshop.databinding.ActivityShopCollectBinding;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

/**
 * 这是店铺收藏ViewModal
 */
public class ShopCollectModal {
    private ShopCollectActivity context;
    private ActivityShopCollectBinding binding;

    private RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.finishRefreshing();
                }
            },2000);
        }

        @Override
        public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.finishLoadmore();
                }
            },2000);
        }
    };


    ShopCollectModal(ShopCollectActivity context, ActivityShopCollectBinding binding) {
        this.context = context;
        this.binding = binding;
        this.binding.refreshShopCollect.setOnRefreshListener(refreshListenerAdapter);
    }

    public void onBack(View view){
        context.finish();
    }
}
