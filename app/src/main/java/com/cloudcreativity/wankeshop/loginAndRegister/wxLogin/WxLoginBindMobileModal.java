package com.cloudcreativity.wankeshop.loginAndRegister.wxLogin;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityWxLoginBindMobileBinding;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.main.MainActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 微信登录绑定手机号ViewModal
 */
public class WxLoginBindMobileModal {
    private WxLoginBindMobileActivity context;
    private BaseDialogImpl baseDialog;
    private ActivityWxLoginBindMobileBinding binding;
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> avatar = new ObservableField<>();
    private String openId;
    private CountDownTimer timer;
    private String mobile;

    WxLoginBindMobileModal(WxLoginBindMobileActivity context,ActivityWxLoginBindMobileBinding binding,String userName,String avatar,String openId) {
        this.context = context;
        this.baseDialog = context;
        this.binding = binding;
        this.userName.set(userName);
        this.avatar.set(avatar);
        this.openId = openId;
    }

    public void onBack(View view){
        context.finish();
    }

    public void onNextStep(View view){
        //验证手机号，下一步
        final String mobile = this.binding.etBindMobileMobile.getText().toString().trim();
        if(TextUtils.isEmpty(mobile)||!StrUtils.isPhone(mobile)||!this.mobile.equals(mobile)){
            ToastUtils.showShortToast(context, R.string.str_login_phone_format_error);
            return;
        }
        final String code = this.binding.etBindMobileCode.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            ToastUtils.showShortToast(context, "验证码不能为空");
            return;
        }
        HttpUtils.getInstance().checkWxUserMobile(mobile,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        UserEntity userEntity = new Gson().fromJson(t, UserEntity.class);
                        if(userEntity==null||userEntity.getId()<=0){
                            //信息为空，需要进行简单注册
                            Intent intent = new Intent(context,WxLoginRegisterActivity.class);
                            intent.putExtra("userName",userName.get());
                            intent.putExtra("avatar",avatar.get());
                            intent.putExtra("mobile",mobile);
                            intent.putExtra("openId",openId);
                            context.startActivity(intent);
                            context.finish();
                        }else{
                            //直接跳转到登录接口
                            bindWxAndUser(mobile,openId,userEntity.getPassword());
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

    }

    @BindingAdapter("imageUrl")
    public static void displayAvatar(ImageView imageView,String imageUrl){
        GlideUtils.loadCircle(imageView.getContext(),imageUrl,imageView);
    }

    //开启定时器
    private void startTimer(){
        binding.tvBindRegisterSendCode.setEnabled(false);
        timer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvBindRegisterSendCode.setText(
                        String.format(context.getResources().getString(R.string.str_register_time_count),
                                millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                binding.tvBindRegisterSendCode.setEnabled(true);
                binding.tvBindRegisterSendCode.setText(R.string.str_register_send_code);
            }
        };
        timer.start();
    }

    //发送验证码
    private void sendSms(String mobile){
        HttpUtils.getInstance().registerGetVerRify(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        ToastUtils.showShortToast(context,R.string.str_register_code_sent);
                        startTimer();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

    }
    //取消定时器
    public void cancelTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    //发送验证码点击
    public void onSendCodeClick(View view){
        mobile = this.binding.etBindMobileMobile.getText().toString().trim();
        if(TextUtils.isEmpty(mobile)||!StrUtils.isPhone(mobile)){
            ToastUtils.showShortToast(context,R.string.str_login_phone_format_error);
            return;
        }
        sendSms(mobile);
    }

    private void login(String phone, String password){
        HttpUtils.getInstance().login(phone,password,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new DefaultObserver<String>(baseDialog,true) {
            @Override
            public void onSuccess(String user) {
                UserEntity userEntity = new Gson().fromJson(user, UserEntity.class);
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

    //将账号和微信openId进行绑定
    private void bindWxAndUser(final String mobile, String openId,final String password){
        HttpUtils.getInstance().bindWxUser(openId,mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        //绑定成功，进行登录操作
                        login(mobile,password);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
}
