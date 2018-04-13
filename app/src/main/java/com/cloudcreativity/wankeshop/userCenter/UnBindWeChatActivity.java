package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityUnbindWxBinding;

/**
 * 解除绑定微信
 */
public class UnBindWeChatActivity extends BaseActivity {

    private UnBindWeChatViewModal viewModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUnbindWxBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_unbind_wx);
        binding.setBindModal(viewModal = new UnBindWeChatViewModal(this,this,binding));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModal.cancelTimer();

    }
}
