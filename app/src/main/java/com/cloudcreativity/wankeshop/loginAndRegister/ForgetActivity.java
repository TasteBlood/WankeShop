package com.cloudcreativity.wankeshop.loginAndRegister;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityForgetBinding;

/**
 * 忘记密码
 */
public class ForgetActivity extends BaseActivity {
    //销毁计时器，需要进行操作
    private ForgetModal forgetModal;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityForgetBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_forget);
        forgetModal = new ForgetModal(this,this,binding);
        binding.setForgetModal(forgetModal);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(forgetModal!=null)
            forgetModal.stopTimer();
    }
}
