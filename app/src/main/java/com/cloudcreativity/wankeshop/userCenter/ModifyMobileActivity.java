package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityModifyMobileBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutModifyPhoneStepOneBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutModifyPhoneStepTwoBinding;
/**
 * 修改手机号
 */
public class ModifyMobileActivity extends BaseActivity {
    //防止计时器内存泄漏
    private ModifyMobileModal.StepOneModal oneModal;
    private ModifyMobileModal.StepTwoModal twoModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityModifyMobileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_mobile);
        //初始化当前的两个ViewStub
        binding.stubModifyOne.getViewStub().setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutModifyPhoneStepOneBinding binding1 = DataBindingUtil.bind(inflated);
                binding1.setStepOne(oneModal = new ModifyMobileModal.StepOneModal(stub,binding1,binding,ModifyMobileActivity.this));


            }
        });
        binding.stubModifyTwo.getViewStub().setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutModifyPhoneStepTwoBinding binding1 = DataBindingUtil.bind(inflated);
                binding1.setStepTwo(twoModal = new ModifyMobileModal.StepTwoModal(ModifyMobileActivity.this,binding1));
            }
        });

        binding.setModifyModal(new ModifyMobileModal(this));
        binding.stubModifyOne.getViewStub().inflate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(oneModal!=null)
            oneModal.stopTimer();
        if(twoModal!=null)
            twoModal.stopTimer();
    }
}
