package com.cloudcreativity.wankeshop.collect;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityGoodsCollectBinding;
/**
 * 商品收藏
 */
public class GoodsCollectActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGoodsCollectBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_collect);
        binding.setGoodsCollectModal(new GoodsCollectModal(this,binding));
    }
}
