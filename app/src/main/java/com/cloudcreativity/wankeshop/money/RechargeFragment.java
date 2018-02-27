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
import com.cloudcreativity.wankeshop.databinding.FragmentRechargeBinding;

/**
 * 充值页面
 */

public class RechargeFragment extends LazyFragment {

    private FragmentRechargeModal rechargeModal;
    private FragmentRechargeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recharge,container,false);
        binding.setRecharge(rechargeModal = new FragmentRechargeModal(this,context,binding,this));
        binding.rcvRecharge.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
        this.binding.refreshRecharge.startRefresh();
    }
}
