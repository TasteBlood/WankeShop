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
import com.cloudcreativity.wankeshop.databinding.FragmentMineBinding;
import com.cloudcreativity.wankeshop.utils.SPUtils;

/**
 * 这是我的Fragment
 */
public class MineFragment extends LazyFragment {

    private MineFragmentModal mineFragmentModal;
    private FragmentMineBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mine,container,false);
        binding.setMineModal(mineFragmentModal = new MineFragmentModal(context));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mineFragmentModal.user = SPUtils.get().getUser();
        binding.invalidateAll();
    }
}
