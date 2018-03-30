package com.cloudcreativity.wankeshop.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentShoppingcarBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 这是购物车Fragment
 */
public class ShoppingCarFragment extends LazyFragment {

    private FragmentShoppingcarBinding binding;
    private ShoppingCarModal shoppingCarModal;


    public static final String MSG_REFRESH_SHOP_CAR = "msg_refresh_shop_car";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shoppingcar,container,false);
        binding.setShoppingModal(shoppingCarModal = new ShoppingCarModal(this,binding,this,context));
        binding.refreshShoppingCar.setOnRefreshListener(shoppingCarModal.refreshListenerAdapter);
        binding.rcvShoppingCar.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.shape_list_item_10dp_tranparent));
        binding.rcvShoppingCar.addItemDecoration(itemDecoration);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
        binding.refreshShoppingCar.startRefresh();
    }

    @Subscribe
    public void onEvent(String msg){
        //刷新购物车
        if(msg.equals(MSG_REFRESH_SHOP_CAR)){
            binding.refreshShoppingCar.startRefresh();
        }
    }
}
