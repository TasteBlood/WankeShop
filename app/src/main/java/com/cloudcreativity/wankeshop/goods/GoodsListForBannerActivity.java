package com.cloudcreativity.wankeshop.goods;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.base.GridDecoration;
import com.cloudcreativity.wankeshop.base.GridDividerItemDecoration;
import com.cloudcreativity.wankeshop.databinding.ActivityGoodsBannerListBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;

/**
 * 这是banner商品列表
 */
@SuppressLint("Registered")
public class GoodsListForBannerActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGoodsBannerListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_banner_list);
        binding.rcvGoodsList.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        binding.rcvGoodsList.addItemDecoration(new GridDividerItemDecoration(15,getResources().getColor(R.color.transparent)));
        GoodsListForBannerViewModal modal;
        binding.setGoodsModal(modal = new GoodsListForBannerViewModal(this,
                getIntent().getStringExtra("keyWords"),
                getIntent().getStringExtra("thirdClassId"),
                getIntent().getStringExtra("title"),
                binding,
                getIntent().<GoodsEntity>getParcelableArrayListExtra("list")));
        binding.refreshGoodsList.setOnRefreshListener(modal.refreshListenerAdapter);
    }
}
