package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityBindWxBinding;
/**
 * 绑定微信
 */
public class BindWeChatActivity extends BaseActivity {

    private BindWeChatViewModal bindModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String avatar = getIntent().getStringExtra("avatar");
        String userName = getIntent().getStringExtra("userName");
        String openId = getIntent().getStringExtra("openId");
        ActivityBindWxBinding wxBingding = DataBindingUtil.setContentView(this, R.layout.activity_bind_wx);
        bindModal = new BindWeChatViewModal(this, this, wxBingding, avatar, userName, openId);
        wxBingding.setBindModal(bindModal);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bindModal.cancelTimer();
    }
}
