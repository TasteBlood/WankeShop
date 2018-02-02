package com.cloudcreativity.wankeshop.loginAndRegister;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityLoginBinding;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.main.MainActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.LogUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 这是登录的ViewModal
 */
public class LoginModal {
    private LoginActivity context;
    private BaseDialogImpl baseDialog;
    private ActivityLoginBinding binding;

    LoginModal(LoginActivity context , BaseDialogImpl baseDialog, ActivityLoginBinding binding) {
        this.baseDialog = baseDialog;
        this.binding = binding;
        this.context = context;
    }

    public void loginClick(View view){
        String phone = binding.etLoginPhone.getText().toString().trim();
        String password = binding.etLoginPassword.getText().toString().trim();
        if(TextUtils.isEmpty(phone)|| !StrUtils.isPhone(phone)){
            ToastUtils.showShortToast(context,R.string.str_login_phone_format_error);
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.showShortToast(context,R.string.str_login_password_error);
            return;
        }
        login(phone,password);
    }
    public void registerClick(View view){
        context.startActivity(new Intent(context,RegisterActivity.class));
    }
    public void forgetClick(View view){
        context.startActivity(new Intent().setClass(context,ForgetActivity.class));
    }
    private void login(String phone, String password){
        HttpUtils.getInstance().login(phone,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String user) {
                        UserEntity userEntity = new Gson().fromJson(user, UserEntity.class);
                        if(userEntity!=null){
                            SPUtils.get().putInt(SPUtils.Config.UID,userEntity.id);
                            SPUtils.get().putString(SPUtils.Config.TOKEN,userEntity.token);
                            SPUtils.get().setUser(user);
                            SPUtils.get().putBoolean(SPUtils.Config.IS_LOGIN,true);
                            context.startActivity(new Intent().setClass(context, MainActivity.class));
                            context.finish();
                        }
            }

            @Override
            public void onFail(ExceptionReason msg) {

            }
        });
    }
}
