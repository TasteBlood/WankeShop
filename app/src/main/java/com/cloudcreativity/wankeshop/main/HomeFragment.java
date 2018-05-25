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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 首页的Fragment
 */
public class HomeFragment extends LazyFragment {

    public static final String MSG_REFRESH = "msg_refresh_category";

    private HomeFragmentModal homeModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        homeModal = new HomeFragmentModal(context, this, this, binding);
        binding.setHomeModal(homeModal);
        binding.homeTabLayout.setupWithViewPager(binding.homeViewPager);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initialLoadData() {

    }

    @Subscribe
    public void onEvent(String msg){
        if(MSG_REFRESH.equals(msg)){
                //说明数据加载失败
                homeModal.loadData();
        }
    }
}
