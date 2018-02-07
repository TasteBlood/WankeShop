package com.cloudcreativity.wankeshop.userCenter.address;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.wankeshop.BR;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentProvinceBinding;

/**
 * 省份Fragment
 */
public class ProvinceFragment extends LazyFragment {

    private ProvinceFragmentModal fragmentModal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentProvinceBinding provinceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_province,container,false);
        fragmentModal = new ProvinceFragmentModal(context,this,provinceBinding);
        provinceBinding.rcvProvince.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        provinceBinding.setModal(fragmentModal);
        fragmentModal.refreshData();
        return provinceBinding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
