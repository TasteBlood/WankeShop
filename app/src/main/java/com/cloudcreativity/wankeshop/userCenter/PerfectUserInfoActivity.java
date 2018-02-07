package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityPerfectUserInfoBinding;
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
}
