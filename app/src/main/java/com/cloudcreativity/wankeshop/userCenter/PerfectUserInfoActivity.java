package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityPerfectUserInfoBinding;
/**
 * 完善用户信息
 */
public class PerfectUserInfoActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPerfectUserInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_perfect_user_info);
        binding.setUserModal(new PeferctUserInfoModal(this));
    }
}
