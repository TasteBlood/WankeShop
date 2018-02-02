package com.cloudcreativity.wankeshop.userCenter;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;

/**
 * 这是UserInformation的ViewModal
 */
public class UserInformationModal {
    private UserInformationActivity context;
    private BaseDialogImpl baseDialog;
    public UserEntity userEntity;

    UserInformationModal(UserInformationActivity context, BaseDialogImpl baseDialog) {
        this.context = context;
        this.baseDialog = baseDialog;
        userEntity = SPUtils.get().getUser();
    }

    public void onBack(View view){
        context.finish();
    }

    public void onEditClick(View view){
        context.startActivity(new Intent().setClass(context,PerfectUserInfoActivity.class));
    }

    @BindingAdapter("imageUrl")
    public static void showAvatar(ImageView imageView, String url){
        if(TextUtils.isEmpty(url)){
            GlideUtils.loadCircle(imageView.getContext(), R.mipmap.ic_default_head,imageView);
        }else{
            GlideUtils.loadCircle(imageView.getContext(),url,imageView);
        }

    }
}
