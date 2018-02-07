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
import com.cloudcreativity.wankeshop.databinding.FragmentAreaBinding;

/**
 * 这是区的Fragment
 */
public class AreaFragment extends LazyFragment {

    private AreaFragmentModal areaFragmentModal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAreaBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_area,container,false);
        binding.rcvArea.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        areaFragmentModal = new AreaFragmentModal(context,this,binding);
        binding.setModal(areaFragmentModal);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

    /**
     * 刷新数据
     */
    public void refreshData(){
        if(areaFragmentModal!=null)
            areaFragmentModal.refreshData();
    }
}
