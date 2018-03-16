package com.cloudcreativity.wankeshop.loginAndRegister;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityLoginBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

/**
 * 登录的Activity
 */
public class LoginActivity extends BaseActivity {
    private LoginModal modal;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLoginModal(modal = new LoginModal(this,this,binding));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //接收微信登录返回的数据
    @Subscribe
    public void onEvent(Map<String,Object> wxLoginInfo){
        if(wxLoginInfo==null||wxLoginInfo.isEmpty())
            return;
        if(wxLoginInfo.get("type").toString().equals("wx_login")){
            modal.onWeChatLoginCallback(wxLoginInfo.get("mobile").toString(),wxLoginInfo.get("password").toString());
        }else if(wxLoginInfo.get("type").toString().equals("wx_auth")){
            modal.onWeChatCallBack(wxLoginInfo.get("userName").toString(),
                    wxLoginInfo.get("avatar").toString(),
                    wxLoginInfo.get("openId").toString());
        }

    }
}
