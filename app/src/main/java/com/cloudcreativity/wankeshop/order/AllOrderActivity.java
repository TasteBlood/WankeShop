package com.cloudcreativity.wankeshop.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityAllOrderBinding;
import com.cloudcreativity.wankeshop.main.MineFragment;
import com.cloudcreativity.wankeshop.main.MineFragmentModal;

/**
 * 全部订单页面
 */
public class AllOrderActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAllOrderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_all_order);
        binding.tabAllOrder.setupWithViewPager(binding.vpAllOrder);
        binding.vpAllOrder.setOffscreenPageLimit(5);
        binding.setAllModal(new AllOrderViewModal(this));
        String state = getIntent().getStringExtra("state");
        if(MineFragmentModal.STATE_WAIT_RECEIVE.equals(state)){
            //选中待收货订单
           binding.getRoot().postDelayed(new Runnable() {
               @Override
               public void run() {
                   binding.vpAllOrder.setCurrentItem(1);
               }
           },200);
        }else if(MineFragmentModal.STATE_RETURN.equals(state)){
            //选中退换货订单
            binding.getRoot().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.vpAllOrder.setCurrentItem(4);
                }
            },200);
        }
    }
}
