package com.cloudcreativity.wankeshop.loginAndRegister.wxLogin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityWxLoginBindMobileBinding;
/**
 * 微信登录绑定手机号
 */
public class WxLoginBindMobileActivity extends BaseActivity {

    private WxLoginBindMobileModal mobileModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWxLoginBindMobileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_wx_login_bind_mobile);
        String userName = getIntent().getStringExtra("userName");
        String avatar = getIntent().getStringExtra("avatar");
        String openId = getIntent().getStringExtra("openId");
        binding.setBindModal(mobileModal = new WxLoginBindMobileModal(this,binding,userName,avatar,openId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mobileModal.cancelTimer();
    }
}
