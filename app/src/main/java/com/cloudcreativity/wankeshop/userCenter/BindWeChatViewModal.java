package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.ObservableField;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import com.cloudcreativity.wankeshop.databinding.ActivityBindWxBinding;
import com.google.gson.Gson;

/**
 * 绑定微信ViewModal
 */
public class BindWeChatViewModal {
    private BindWeChatActivity context;
    private BaseDialogImpl baseDialog;
    private CountDownTimer timer;
    private ActivityBindWxBinding bindWxBinding;

    public ObservableField<String> avatar  =new ObservableField<>();
    public ObservableField<String> userName = new ObservableField<>();
    private String openId;

    BindWeChatViewModal(BindWeChatActivity context, BaseDialogImpl baseDialog,ActivityBindWxBinding bindWxBinding,String avatar,String userName,String openId) {
        this.context = context;
        this.baseDialog = baseDialog;
        this.bindWxBinding = bindWxBinding;
        this.avatar.set(avatar);
        this.openId = openId;
        this.userName.set(userName);
    }

    public void onBack(View view){
        context.finish();
    }

    //开启定时器
    private void startTimer(){
        bindWxBinding.tvBindWxSendCode.setEnabled(false);
        timer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                bindWxBinding.tvBindWxSendCode.setText(
                        String.format(context.getResources().getString(R.string.str_register_time_count),
                                millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                bindWxBinding.tvBindWxSendCode.setEnabled(true);
                bindWxBinding.tvBindWxSendCode.setText(R.string.str_register_send_code);
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
        String mobile = SPUtils.get().getUser().getMobile();
        if(TextUtils.isEmpty(mobile)||!StrUtils.isPhone(mobile)){
            ToastUtils.showShortToast(context,R.string.str_login_phone_format_error);
            return;
        }
        sendSms(mobile);
    }

    //解除绑定
    public void bindWx(View view){
        String sms = bindWxBinding.etBindWxCode.getText().toString().trim();
        if(TextUtils.isEmpty(sms)){
            ToastUtils.showShortToast(context,R.string.str_sms_not_null);
            return;
        }
        HttpUtils.getInstance().bindWeChat(sms,SPUtils.get().getUser().getMobile(),openId,userName.get(),avatar.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        ToastUtils.showShortToast(context,"已绑定");
                        //更新本地的用户信息
                        UserEntity userEntity = new Gson().fromJson(t, UserEntity.class);
                        if(userEntity!=null){
                            SPUtils.get().putInt(SPUtils.Config.UID,userEntity.getId());
                            SPUtils.get().putString(SPUtils.Config.TOKEN,userEntity.getToken());
                            SPUtils.get().setUser(t);
                        }
                        context.finish();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
}
