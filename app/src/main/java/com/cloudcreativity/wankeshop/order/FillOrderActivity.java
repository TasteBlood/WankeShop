package com.cloudcreativity.wankeshop.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityFillOrderBinding;
import com.cloudcreativity.wankeshop.entity.ShopCarItemEntity;
import com.cloudcreativity.wankeshop.goods.GoodsIndexFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 填写订单页面
 */
public class FillOrderActivity extends BaseActivity {


    private FillOrderViewModal modal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        ActivityFillOrderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fill_order);
        binding.rcvFillOrder.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line_1dp_grayf1f1f1));
        binding.rcvFillOrder.addItemDecoration(itemDecoration);

        binding.setOrderModal(modal = new FillOrderViewModal(this,this,getIntent().<ShopCarItemEntity>getParcelableArrayListExtra("list")));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(String msg){
        if(GoodsIndexFragment.MSG_REFRESH_ADDRESS.equals(msg)){
            modal.updateAddress(this);
        }
    }
}
