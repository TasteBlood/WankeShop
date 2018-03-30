package com.cloudcreativity.wankeshop.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;

/**
 * 商铺商品列表
 */
public class ShopGoodsListActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_goods_list);
    }
}
