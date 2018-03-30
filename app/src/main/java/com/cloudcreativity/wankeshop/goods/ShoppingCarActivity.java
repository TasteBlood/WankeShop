package com.cloudcreativity.wankeshop.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityShoppingcarBinding;
import com.cloudcreativity.wankeshop.main.ShoppingCarFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 这是购物车
 */
public class ShoppingCarActivity extends BaseActivity {

    private ActivityShoppingcarBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_shoppingcar);
        binding.rcvShoppingCar.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.shape_list_item_10dp_tranparent));
        binding.rcvShoppingCar.addItemDecoration(itemDecoration);
        binding.setShoppingModal(new ShoppingCarViewModal(binding,this,this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(String msg){
        //刷新购物车
        if(ShoppingCarFragment.MSG_REFRESH_SHOP_CAR.equals(msg)){
            binding.refreshShoppingCar.startRefresh();
        }
    }
}
