package com.cloudcreativity.wankeshop.money;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityApplyWithdrawBinding;


/**
 * 申请提现接口
 */
public class ApplyWithDrawActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityApplyWithdrawBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_withdraw);
        binding.setWithdraw(new ApplyWithDrawViewModal(this,this,binding));
    }
}
