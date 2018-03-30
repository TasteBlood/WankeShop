package com.cloudcreativity.wankeshop.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityOrderDetailBinding;

import java.io.Serializable;

/**
 * 订单详情页面
 */
public class OrderDetailActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOrderDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);

        //初始化布局
        binding.rcvOrderGoods.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line_1dp_grayf1f1f1));
        binding.rcvOrderGoods.addItemDecoration(itemDecoration);

        //这是传递过来的订单数据，有可能是大订单，有可能是小定单数据，所以是序列化对象，具体在使用时，需要做类型判断
        Serializable order = getIntent().getSerializableExtra("order");

        binding.setDetailModal(new OrderDetailViewModal(this,this,order,binding));
    }
}
