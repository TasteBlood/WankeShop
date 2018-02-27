package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.receiver.MyBusinessReceiver;
import com.cloudcreativity.wankeshop.databinding.LayoutDialogUserAuthErrorBinding;
/**
 * 用户权限出错对话框
 */
public class AuthDialogUtils {
    private Dialog dialog;


    public void show(Context context){
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        LayoutDialogUserAuthErrorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_dialog_user_auth_error,null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }


    public void onCancelClick(View view){
        dialog.dismiss();
        dialog = null;

    }

    public void onLoginClick(View view){
        //重新登录
        dialog.dismiss();
        dialog = null;
        Intent intent = new Intent(MyBusinessReceiver.ACTION_RE_LOGIN);
        view.getContext().sendBroadcast(intent);

    }
}
