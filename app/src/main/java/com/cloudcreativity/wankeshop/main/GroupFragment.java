package com.cloudcreativity.wankeshop.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.GridDividerItemDecoration;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentGroupBinding;

/**
 * 商铺Fragment，目前不存在拼团，所以改为商铺
 */
public class GroupFragment extends LazyFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentGroupBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_group,container,false);
        //初始化layoutManager
        binding.rcvGroup.setLayoutManager(new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false));
        binding.rcvGroup.addItemDecoration(new GridDividerItemDecoration(10,getResources().getColor(R.color.transparent)));
        binding.setGroupModal(new GroupFragmentModal(this,binding,context));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
