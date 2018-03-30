package com.cloudcreativity.wankeshop.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityDetailBinding;

/**
 * 商品详情
 */
public class GoodsDetailActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.tabGoodsDetail.setupWithViewPager(binding.vpGoodsDetail);
        binding.setDetailModal(new GoodsDetailViewModal(this,binding,getIntent().getIntExtra("spuId",0)));
    }
}
