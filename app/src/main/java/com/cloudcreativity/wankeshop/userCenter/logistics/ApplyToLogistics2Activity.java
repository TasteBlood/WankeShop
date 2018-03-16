package com.cloudcreativity.wankeshop.userCenter.logistics;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;

import com.cloudcreativity.wankeshop.BR;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityApplyToLogistics2Binding;
import com.cloudcreativity.wankeshop.databinding.LayoutApplyStepOneViewstubBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutApplyStepThreeViewstubBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutApplyStepTwoViewstubBinding;
import com.cloudcreativity.wankeshop.entity.ApplyLogisticsEntity;
import com.cloudcreativity.wankeshop.userCenter.address.TempAddress;

/**
 * 申请成为物流小二页面
 */
public class ApplyToLogistics2Activity extends BaseActivity {

    private ActivityApplyToLogistics2Binding binding;
    private ApplyToLogisticsModal modal;
    private ApplyToLogisticsModal.StepOneModal stepOneModal;//选择城市区使用
    private ApplyToLogisticsModal.StepThreeModal stepThreeModal;//选择照片使用

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_to_logistics2);

        binding.stubApplyOne.getViewStub().setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutApplyStepOneViewstubBinding oneViewstubBinding = DataBindingUtil.bind(inflated);
                oneViewstubBinding.setVariable(BR.stepOne,stepOneModal = new ApplyToLogisticsModal.StepOneModal(binding,oneViewstubBinding,ApplyToLogistics2Activity.this));
            }
        });

        binding.stubApplyTwo.getViewStub().setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutApplyStepTwoViewstubBinding twoViewstubBinding = DataBindingUtil.bind(inflated);
                twoViewstubBinding.setVariable(BR.stepTwo,new ApplyToLogisticsModal.StepTwoModal(binding,twoViewstubBinding));
            }
        });

        binding.stubApplyThree.getViewStub().setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutApplyStepThreeViewstubBinding threeViewstubBinding = DataBindingUtil.bind(inflated);
                threeViewstubBinding.setVariable(BR.stepThree,stepThreeModal = new ApplyToLogisticsModal.StepThreeModal(ApplyToLogistics2Activity.this,threeViewstubBinding));
            }
        });
        ApplyLogisticsEntity entity = (ApplyLogisticsEntity) getIntent().getSerializableExtra("logisticsEntity");
        binding.setVariable(BR.modal,modal = new ApplyToLogisticsModal(this,binding,entity));

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //在这里获取城市的数据
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
            stepOneModal.setAddress(buffer.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁城市数据
        TempAddress.clear();
    }

    @Override
    protected void onPhotoSuccess(String filePath) {
        //在这里上传图片
        stepThreeModal.uploadImage(filePath);
    }
}
