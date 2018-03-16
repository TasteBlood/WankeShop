package com.cloudcreativity.wankeshop.loginAndRegister.wxLogin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityWxLoginRegisterBinding;
import com.cloudcreativity.wankeshop.userCenter.address.TempAddress;

/**
 * 这是微信登录注册页面
 */
public class WxLoginRegisterActivity extends BaseActivity {

    private WxLoginRegisterModal registerModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWxLoginRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_wx_login_register);
        String mobile = getIntent().getStringExtra("mobile");
        String userName = getIntent().getStringExtra("userName");
        String avatar = getIntent().getStringExtra("avatar");
        String openId = getIntent().getStringExtra("openId");

        registerModal = new WxLoginRegisterModal(this, binding, mobile, userName, avatar, openId);
        binding.setRegisterModal(registerModal);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空临时保存的省市数据
        TempAddress.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //在这里判断当前的地址信息是否存在
        StringBuffer buffer = new StringBuffer();
        if(TempAddress.provinceEntity!=null)
            buffer.append(TempAddress.provinceEntity.getName());
        if(TempAddress.cityEntity!=null)
            buffer.append(TempAddress.cityEntity.getName());
        if(TempAddress.areaEntity!=null)
            buffer.append(TempAddress.areaEntity.getName());
        if(TempAddress.streetEntity!=null)
            buffer.append(TempAddress.streetEntity.getName());

        if(buffer.length()>0)
            registerModal.onAddressSuccess(buffer.toString());
    }
}
