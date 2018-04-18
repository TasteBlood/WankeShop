package com.cloudcreativity.wankeshop.money;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentMoneyPayBinding;
/**
 * 这是资金记录的支付记录
 */
public class PayFragment extends LazyFragment {

    private FragmentMoneyPayBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_money_pay,container,false);
        binding.rcvPay.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.setPay(new FragmentPayModal(this,context,binding,this));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
        binding.refreshPay.startRefresh();
    }
}
