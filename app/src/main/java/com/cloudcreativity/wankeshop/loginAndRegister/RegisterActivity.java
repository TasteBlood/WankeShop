package com.cloudcreativity.wankeshop.loginAndRegister;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityRegisterBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutRegisterStepOneBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutRegisterStepTwoBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutRegisterStepThreeBinding;
import com.cloudcreativity.wankeshop.userCenter.address.TempAddress;

/**
 * 注册的Activity
 */

public class RegisterActivity extends BaseActivity {
    //需要全局第三个ViewModal，因为要关闭计时器（防止内存泄漏）
    private RegisterModal.StubThreeModal threeModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        //初始化第一个Stub的事件
        binding.stubRegisterOne.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutRegisterStepOneBinding oneBinding = DataBindingUtil.bind(inflated);
                oneBinding.setStubOne(new RegisterModal.StubOneModal(RegisterActivity.this,oneBinding,stub));
            }
        });
        //初始化第二个Stub的事件
        binding.stubRegisterTwo.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutRegisterStepTwoBinding twoBinding = DataBindingUtil.bind(inflated);
                twoBinding.setStubTwo(new RegisterModal.StubTwoModal(RegisterActivity.this,twoBinding,stub));
            }
        });
        //初始化第三个的Stub事件
        binding.stubRegisterThree.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutRegisterStepThreeBinding threeBinding = DataBindingUtil.bind(inflated);
                threeModal = new RegisterModal.StubThreeModal(RegisterActivity.this,threeBinding,stub);
                threeBinding.setStubThree(threeModal);
            }
        });


        binding.setRegisterModal(new RegisterModal(binding,this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在这里停止获取验证码的计时器
        if(threeModal!=null)
            threeModal.stopTimer();

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
            threeModal.setAddress(buffer.toString());
    }
}
