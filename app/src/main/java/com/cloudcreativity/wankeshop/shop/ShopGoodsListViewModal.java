package com.cloudcreativity.wankeshop.shop;

import android.view.View;

import com.cloudcreativity.wankeshop.base.BaseDialogImpl;

/**
 * 这是商铺商品列表ViewModal
 */
public class ShopGoodsListViewModal {
    private ShopGoodsListActivity context;
    private BaseDialogImpl baseDialog;

    ShopGoodsListViewModal(ShopGoodsListActivity context, BaseDialogImpl baseDialog) {
        this.context = context;
        this.baseDialog = baseDialog;
    }

    public void onBack(View view){
        context.finish();
    }
}
