package com.cloudcreativity.wankeshop.collect;

import android.view.View;
import com.cloudcreativity.wankeshop.databinding.ActivityGoodsCollectBinding;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

/**
 * 这是商品收藏ViewModal
 */
public class GoodsCollectModal {
    private GoodsCollectActivity context;
    private ActivityGoodsCollectBinding binding;

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


    GoodsCollectModal(GoodsCollectActivity context, ActivityGoodsCollectBinding binding) {
        this.context = context;
        this.binding = binding;
        this.binding.refreshGoodsCollect.setOnRefreshListener(refreshListenerAdapter);
    }

    public void onBack(View view){
        context.finish();
    }
}
