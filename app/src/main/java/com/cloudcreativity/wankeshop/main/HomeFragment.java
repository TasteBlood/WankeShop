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
import com.cloudcreativity.wankeshop.databinding.FragmentHomeBinding;

/**
 * 首页的Fragment
 */
public class HomeFragment extends LazyFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        binding.setHomeModal(new HomeFragmentModal(getChildFragmentManager()));
        binding.homeTabLayout.setupWithViewPager(binding.homeViewPager);
        binding.homeViewPager.setOffscreenPageLimit(10);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
