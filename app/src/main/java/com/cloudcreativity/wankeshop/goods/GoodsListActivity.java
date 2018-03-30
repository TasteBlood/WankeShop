package com.cloudcreativity.wankeshop.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.base.GridDecoration;
import com.cloudcreativity.wankeshop.base.GridDividerItemDecoration;
import com.cloudcreativity.wankeshop.databinding.ActivityGoodsListBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;

/**
 * 这是商品列表
 */
public class GoodsListActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGoodsListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_list);
        binding.rcvGoodsList.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        binding.rcvGoodsList.addItemDecoration(new GridDividerItemDecoration(15,getResources().getColor(R.color.transparent)));
        GoodsListViewModal modal;
        binding.setGoodsModal(modal = new GoodsListViewModal(this,
                getIntent().getStringExtra("title"),
                getIntent().getStringExtra("style"),
                getIntent().getStringExtra("type"),
                binding,
                getIntent().<GoodsEntity>getParcelableArrayListExtra("list")));
        binding.refreshGoodsList.setOnRefreshListener(modal.refreshListenerAdapter);
    }
}
