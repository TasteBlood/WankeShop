package com.cloudcreativity.wankeshop.collect;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityShopCollectBinding;

/**
 * 商铺收藏
 */
public class ShopCollectActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShopCollectBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_collect);
        binding.setShopCollectModal(new ShopCollectModal(this,binding));
    }
}
