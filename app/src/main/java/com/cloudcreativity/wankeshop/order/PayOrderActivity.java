package com.cloudcreativity.wankeshop.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityPayOrderBinding;

/**
 * 支付订单页面
 */
public class PayOrderActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPayOrderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_pay_order);
        //获取订单号和金额
        String orderNum = getIntent().getStringExtra("orderNum");
        float totalMoney = getIntent().getFloatExtra("totalMoney", 0.00f);
        binding.setPayModal(new PayOrderViewModal(this,this,binding,orderNum,totalMoney));
    }
}
