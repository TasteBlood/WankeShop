package com.cloudcreativity.wankeshop.userCenter;

import android.content.Intent;
import android.view.View;

import com.cloudcreativity.wankeshop.base.CommonWebActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityUserinformationBinding;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.utils.SPUtils;

/**
 * 这是UserInformation的ViewModal
 */
public class UserInformationModal {
    private UserInformationActivity context;
    private ActivityUserinformationBinding binding;
    public UserEntity userEntity;

    UserInformationModal(UserInformationActivity context, ActivityUserinformationBinding binding) {
        this.context = context;
        this.binding = binding;
        userEntity = SPUtils.get().getUser();
    }

    public void onBack(View view){
        context.finish();
    }

    public void onEditClick(View view){
        context.startActivity(new Intent().setClass(context,PerfectUserInfoActivity.class));
    }

    //更新用户数据
    public void updateUser(UserEntity userEntity){
        this.userEntity = userEntity;
        binding.invalidateAll();
    }

    //跳转到绑定微信页面
    public void bindWechat(View view){
        CommonWebActivity.startActivity(context,"关注公众号","file:///android_asset/contact_public_code.html");
    }
}
