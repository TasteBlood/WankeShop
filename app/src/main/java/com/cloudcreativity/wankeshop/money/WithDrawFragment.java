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
import com.cloudcreativity.wankeshop.databinding.FragmentWithdrawBinding;

/**
 * 提现页面
 */
public class WithDrawFragment extends LazyFragment {

    private FragmentWithdrawModal withdrawModal;
    private FragmentWithdrawBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_withdraw,container,false);
        binding.setWithdrawModal(withdrawModal = new FragmentWithdrawModal(this,context,binding,this));
        binding.rcvWithdraw.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
        this.binding.refreshWithdraw.startRefresh();
    }
}
