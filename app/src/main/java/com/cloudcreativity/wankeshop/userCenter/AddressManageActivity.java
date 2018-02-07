package com.cloudcreativity.wankeshop.userCenter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityAddressManageBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 地址管理
 */
public class AddressManageActivity extends BaseActivity {

    private AddressManageModal manageModal;

    //这是刷新界面的消息
    public static final String MSG_REFRESH_DATA = "msg_refresh_data";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        ActivityAddressManageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_address_manage);

        manageModal = new AddressManageModal(this,binding);

        binding.srlAddressManage.setOnRefreshListener(manageModal.onRefreshListener);
        binding.srlAddressManage.setDistanceToTriggerSync((int) (getResources().getDisplayMetrics().densityDpi*1.5));
        binding.rcvAddressManage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.shape_list_item_10dp_tranparent));
        binding.rcvAddressManage.setItemAnimator(new DefaultItemAnimator());
        binding.rcvAddressManage.addItemDecoration(dividerItemDecoration);

        binding.setAddressModal(manageModal);

        manageModal.refreshData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(String msg){
        if(MSG_REFRESH_DATA.equals(msg)){
            //刷新数据
            manageModal.refreshData();
        }
    }
}
