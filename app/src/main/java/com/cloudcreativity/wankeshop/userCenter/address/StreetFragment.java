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
import com.cloudcreativity.wankeshop.databinding.FragmentStreetBinding;

public class StreetFragment extends LazyFragment {

    private StreetFragmentModal streetFragmentModal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentStreetBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_street,container,false);
        binding.rcvStreet.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.setModal(streetFragmentModal = new StreetFragmentModal(context,this,binding));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

    public void refreshData(){
        if(streetFragmentModal!=null)
            streetFragmentModal.refreshData();
    }
}
