package com.cloudcreativity.wankeshop.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentGroupBinding;

/**
 * 拼团Fragment
 */
public class GroupFragment extends LazyFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentGroupBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_group,container,false);
        binding.setGroupModal(new GroupFragmentModal(context));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
