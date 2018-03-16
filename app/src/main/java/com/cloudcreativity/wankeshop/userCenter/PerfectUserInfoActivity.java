package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityPerfectUserInfoBinding;
import com.cloudcreativity.wankeshop.userCenter.address.TempAddress;

/**
 * 完善用户信息
 */
public class PerfectUserInfoActivity extends BaseActivity {

    private PerfectUserInfoModal userInfoModal;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPerfectUserInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_perfect_user_info);
        binding.setUserModal(userInfoModal = new PerfectUserInfoModal(this,binding));
    }

    //这边是图片处理成功
    @Override
    protected void onPhotoSuccess(String filePath) {
        userInfoModal.uploadImage(filePath);
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
            userInfoModal.setAddress(buffer.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TempAddress.clear();
    }
}
