package com.cloudcreativity.wankeshop.loginAndRegister;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityLoginBinding;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.loginAndRegister.wxLogin.WxLoginBindMobileActivity;
import com.cloudcreativity.wankeshop.main.MainActivity;
import com.cloudcreativity.wankeshop.utils.AppConfig;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.LogUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 这是登录的ViewModal
 */
public class LoginModal {
    private LoginActivity context;
    private BaseDialogImpl baseDialog;
    private ActivityLoginBinding binding;
    private IWXAPI api;

    LoginModal(LoginActivity context , BaseDialogImpl baseDialog, ActivityLoginBinding binding) {
        this.baseDialog = baseDialog;
        this.binding = binding;
        this.context = context;
        api = WXAPIFactory.createWXAPI(context,AppConfig.WX_APP_ID,false);
        api.registerApp(AppConfig.WX_APP_ID);

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
        login(phone,password,0);
    }

    public void registerClick(View view){
        context.startActivity(new Intent(context,RegisterActivity.class));
    }

    public void forgetClick(View view){
        context.startActivity(new Intent().setClass(context,ForgetActivity.class));
    }

    private void login(String phone, String password,int type){
        HttpUtils.getInstance().login(phone,password,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String user) {
                        UserEntity userEntity = new Gson().fromJson(user, UserEntity.class);
                        //LogUtils.e("xuxiwu_debug_test",userEntity.toString());
                        if(userEntity!=null){
                            SPUtils.get().putInt(SPUtils.Config.UID,userEntity.getId());
                            SPUtils.get().putString(SPUtils.Config.TOKEN,userEntity.getToken());
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

    public void onWeChatLoginClick(View view){
        SendAuth.Req req = new SendAuth.Req();
        req.scope="snsapi_userinfo";
        req.state="wechat_sdk_微信登录";
        api.sendReq(req);
    }

    public void onWeChatCallBack(final String userName,final String avatar, final String openId){
        //检查是否绑定过手机号，如果绑定过就直接登录
        HttpUtils.getInstance().checkWxUser(openId,userName,avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        UserEntity userEntity = new Gson().fromJson(t, UserEntity.class);
                        if(userEntity==null||userEntity.getId()<=0){
                            Intent intent = new Intent(context, WxLoginBindMobileActivity.class);
                            intent.putExtra("userName",userName);
                            intent.putExtra("avatar",avatar);
                            intent.putExtra("openId",openId);
                            context.startActivity(intent);
                        }else{
                            //直接登录
                            login(userEntity.getMobile(),userEntity.getPassword(),1);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    public void onWeChatLoginCallback(String mobile,String password){
        login(mobile,password,0);
    }

    public void onBackClick(View view){
        context.finish();
    }
}
