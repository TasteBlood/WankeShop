package com.cloudcreativity.wankeshop.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.loginAndRegister.LoginActivity;
import com.cloudcreativity.wankeshop.userCenter.AddressManageActivity;
import com.cloudcreativity.wankeshop.userCenter.SettingActivity;
import com.cloudcreativity.wankeshop.userCenter.UserInformationActivity;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.PayWayDialogUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;

/**
 * 这是MineFragment的ViewModal
 */
public class MineFragmentModal {

    private Context context;
    //这是用户数据
    public UserEntity user ;


    MineFragmentModal(Context context) {
        this.context = context;
        user = SPUtils.get().getUser();
    }

    @BindingAdapter("imageUrl")
    public static void showAvatar(ImageView imageView,String url){
        if(TextUtils.isEmpty(url)){
            GlideUtils.loadCircle(imageView.getContext(),R.mipmap.ic_default_head,imageView);
        }else{
            GlideUtils.loadCircle(imageView.getContext(),url,imageView);
        }
    }

    //personal center
    public void onUserInfoClick(View view){
        context.startActivity(new Intent().setClass(context, UserInformationActivity.class));
    }
    //setting click
    public void onSettingClick(View view){
        context.startActivity(new Intent().setClass(context, SettingActivity.class));
    }

    //address click
    public void onAddressClick(View view){
        context.startActivity(new Intent().setClass(context, AddressManageActivity.class));
    }

    //recharge click
    public void onRechargeClick(View view){
        new PayWayDialogUtils().show(context);
    }
}
