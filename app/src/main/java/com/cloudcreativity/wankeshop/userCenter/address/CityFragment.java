package com.cloudcreativity.wankeshop.userCenter.address;

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
import com.cloudcreativity.wankeshop.databinding.FragmentCityBinding;
/**
 * 市Fragment
 */
public class CityFragment extends LazyFragment {

    private CityFragmentModal cityFragmentModal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCityBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city,container,false);
        binding.rcvCity.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        cityFragmentModal = new CityFragmentModal(context,this,binding);
        binding.setModal(cityFragmentModal);
        cityFragmentModal.refreshData();
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

    /**
     * 刷新数据
     */
    public void refreshData(){
        if(cityFragmentModal!=null)
            cityFragmentModal.refreshData();
    }
}
