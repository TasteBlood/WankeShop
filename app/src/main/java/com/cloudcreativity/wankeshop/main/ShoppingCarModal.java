package com.cloudcreativity.wankeshop.main;

import android.content.Context;

import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentShoppingcarBinding;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

/**
 * 这是购物车ViewModal
 */
public class ShoppingCarModal {
    private FragmentShoppingcarBinding binding;
    private BaseDialogImpl baseDialog;
    private Context context;

    ShoppingCarModal(FragmentShoppingcarBinding binding, BaseDialogImpl baseDialog, Context context) {
        this.binding = binding;
        this.baseDialog = baseDialog;
        this.context = context;

        this.binding.refreshShoppingCar.setOnRefreshListener(new RefreshListenerAdapter() {
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
        });
    }
}
