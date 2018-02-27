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
import com.cloudcreativity.wankeshop.databinding.FragmentShoppingcarBinding;

/**
 * 这是购物车Fragment
 */
public class ShoppingCarFragment extends LazyFragment {

    private FragmentShoppingcarBinding binding;
    private ShoppingCarModal shoppingCarModal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shoppingcar,container,false);
        binding.setShoppingModal(shoppingCarModal = new ShoppingCarModal(binding,this,context));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
