package com.cloudcreativity.wankeshop.loginAndRegister;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityForgetBinding;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 忘记密码的modal
 */
public class ForgetModal {
    private ForgetActivity context;
    private BaseDialogImpl dialog;
    private ActivityForgetBinding forgetBinding;
    private CountDownTimer timer;
    private String registerPhone;

    ForgetModal(ForgetActivity activity, BaseDialogImpl dialog, ActivityForgetBinding binding) {
        this.context = activity;
        this.dialog = dialog;
        this.forgetBinding = binding;
        timer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                forgetBinding.tvForgetSend.setText(
                        String.format(context.getResources().getString(R.string.str_register_time_count),
                                millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                forgetBinding.tvForgetSend.setEnabled(true);
                forgetBinding.tvForgetSend.setText(R.string.str_register_send_code);
            }
        };
    }

    public void onBackClick(View view){
        context.finish();
    }

    public void onSendClick(View view){
        registerPhone = forgetBinding.etForgetPhone.getText().toString().trim();
        if(TextUtils.isEmpty(registerPhone)||!StrUtils.isPhone(registerPhone)){
            ToastUtils.showShortToast(context,R.string.str_login_phone_format_error);
            return;
        }
        sendSms(registerPhone);
    }
    public void onSubmitClick(View view){
        String phone = forgetBinding.etForgetPhone.getText().toString().trim();
        String password1 = forgetBinding.etForgetPassword.getText().toString().trim();
        String password2 = forgetBinding.etForgetPassword2.getText().toString().trim();
        String sms = forgetBinding.etForgetVerifyCode.getText().toString().trim();
        if(TextUtils.isEmpty(password1)||password1.length()<6){
            ToastUtils.showShortToast(context,R.string.str_pwd_for_error);
            return;
        }
        if(TextUtils.isEmpty(password2)||password2.length()<6){
            ToastUtils.showShortToast(context,R.string.str_pwd_for_error);
            return;
        }
        if(TextUtils.isEmpty(phone)||!StrUtils.isPhone(phone)){
            ToastUtils.showShortToast(context,R.string.str_login_phone_format_error);
            return;
        }
        if(!registerPhone.equals(phone)){
            ToastUtils.showShortToast(context,R.string.str_phone_not_unit);
            return;
        }
        if(TextUtils.isEmpty(sms)){
            ToastUtils.showShortToast(context,R.string.str_sms_not_null);
            return;
        }
        submit(registerPhone,password1,sms);
    }

    //发送验证码
    private void sendSms(String mobile){
        HttpUtils.getInstance().registerGetVerRify(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(dialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        ToastUtils.showShortToast(context,R.string.str_register_code_sent);
                        forgetBinding.tvForgetSend.setEnabled(false);
                        startTimer();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

    }
    //忘记密码提交
    private void submit(String mobile,String password,String sms){
        HttpUtils.getInstance().editPassword(mobile,password,sms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(dialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        ToastUtils.showShortToast(context,R.string.str_edit_pwd_success);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                context.finish();
                            }
                        },500);
                    }
                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
    private void startTimer(){
        timer.start();
    }
    public void stopTimer(){
        if(timer!=null)
            timer.cancel();
    }
}
