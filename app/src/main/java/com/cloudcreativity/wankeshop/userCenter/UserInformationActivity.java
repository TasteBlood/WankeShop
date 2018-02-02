package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityUserinformationBinding;
/**
 * 这是用户详情页面
 */
public class UserInformationActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserinformationBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_userinformation);
        binding.setUserModal(new UserInformationModal(this,this));
    }
}
