package com.cloudcreativity.wankeshop.money;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityMoneyRecordBinding;
/**
 * 资金记录页面
 */
public class MoneyRecordsActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMoneyRecordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_money_record);
        binding.tlMoneyRecords.setupWithViewPager(binding.vpMoneyRecords);
        binding.setMoneyModal(new MoneyRecordsModal(this));
        binding.vpMoneyRecords.setOffscreenPageLimit(2);
    }
}
