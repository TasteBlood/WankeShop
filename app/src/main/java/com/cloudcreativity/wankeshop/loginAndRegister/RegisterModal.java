package com.cloudcreativity.wankeshop.loginAndRegister;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.base.CommonWebActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityRegisterBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutRegisterStepOneBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutRegisterStepThreeBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutRegisterStepTwoBinding;
import com.cloudcreativity.wankeshop.entity.address.ProvinceEntity;
import com.cloudcreativity.wankeshop.userCenter.address.AddressChooseActivity;
import com.cloudcreativity.wankeshop.userCenter.address.TempAddress;
import com.cloudcreativity.wankeshop.utils.APIService;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 注册的ViewModal
 */
public class RegisterModal {

    private static ActivityRegisterBinding activityRegisterBinding;
    private static String registerPhone;
    private static BaseDialogImpl baseDialog;
    public RegisterModal(ActivityRegisterBinding binding, BaseDialogImpl impl) {
        activityRegisterBinding = binding;
        baseDialog = impl;
        activityRegisterBinding.stubRegisterOne.getViewStub().inflate();
    }

    /**
     * 这是第一个Stub的modal
     */
    public  static class StubOneModal{
        private RegisterActivity context;
        private LayoutRegisterStepOneBinding binding;
        private ViewStub stub;
        private boolean isAgreeUserProtocol;

        StubOneModal(RegisterActivity context, LayoutRegisterStepOneBinding binding, ViewStub stub) {
            this.context = context;
            this.binding = binding;
            this.stub = stub;
        }

        public void onBackClick(View view){
            context.finish();
        }
        public void onNextClick(View view){
            String phone = binding.etRegisterOnePhone.getText().toString().trim();
            if(TextUtils.isEmpty(phone)|| !StrUtils.isPhone(phone)){
                ToastUtils.showShortToast(context, R.string.str_login_phone_format_error);
                return;
            }
            if(!isAgreeUserProtocol){
                ToastUtils.showShortToast(context,R.string.str_agree_user_protocol);
                return;
            }
            checkPhone(phone);
            registerPhone = phone;
        }
        private void checkPhone(String phone){
            HttpUtils.getInstance().registerFirst(phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            stub.setVisibility(View.GONE);
                            activityRegisterBinding.stubRegisterTwo.getViewStub().inflate();
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });
        }

        //这是是否同意用户协议
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isAgreeUserProtocol = isChecked;
        }
        //这是查看用户协议
        public void lookUserProtocol(View view){
            CommonWebActivity.startActivity(context,"用户使用协议", APIService.HOST+"help/user_protocol.html");
        }

    }

    /**
     * 这是第二个Stub的modal
     */
    public static class StubTwoModal{
        private RegisterActivity context;
        private LayoutRegisterStepTwoBinding binding;
        private ViewStub stub;

        StubTwoModal(RegisterActivity context, LayoutRegisterStepTwoBinding binding, ViewStub stub) {
            this.context = context;
            this.binding = binding;
            this.stub = stub;
        }

        public void onBackClick(View view){
            context.finish();
        }

        public void progressChanged(SeekBar seekBar,int progress, boolean fromUser){
            if(fromUser&&progress==100){
                binding.skbRegisterTwoCheckout.setThumb(context.getResources().getDrawable(R.mipmap.ic_seekbar_thumb_selected));
                binding.skbRegisterTwoCheckout.setEnabled(false);
                binding.tvRegisterTwoInfo.setText(R.string.str_register_seek_pass);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stub.setVisibility(View.GONE);
                        activityRegisterBinding.stubRegisterThree.getViewStub().inflate();
                    }
                },1000);
            }
        }
    }

    /**
     * 这是第三个Stub的modal
     */
    public static class StubThreeModal{
        private RegisterActivity context;
        private LayoutRegisterStepThreeBinding threeBinding;
        private ViewStub stub;
        private CountDownTimer timer;
        
        StubThreeModal(final RegisterActivity context, LayoutRegisterStepThreeBinding binding, ViewStub stub) {
            this.context = context;
            this.threeBinding = binding;
            this.stub = stub;
            timer = new CountDownTimer(60*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    threeBinding.tvRegisterThreeSend.setText(
                            String.format(context.getResources().getString(R.string.str_register_time_count),
                                    millisUntilFinished/1000));
                }

                @Override
                public void onFinish() {
                    threeBinding.tvRegisterThreeSend.setEnabled(true);
                    threeBinding.tvRegisterThreeSend.setText(R.string.str_register_send_code);
                }
            };
        }

        public void onBackClick(View view){
            context.finish();
        }
        
        public void onSendClick(View view){
            sendSms(registerPhone);
        }


        public void onRegisterClick(View view){
            String password = threeBinding.etRegisterThreePassword.getText().toString().trim();
            String sms = threeBinding.etRegisterThreeVerifyCode.getText().toString().trim();
            if(TextUtils.isEmpty(password)||password.length()<6){
                ToastUtils.showShortToast(context,R.string.str_pwd_for_error);
                return;
            }
            if(TextUtils.isEmpty(sms)){
                ToastUtils.showShortToast(context,R.string.str_sms_not_null);
                return;
            }
            if(TempAddress.provinceEntity==null){
                ToastUtils.showShortToast(context,"所在地区不能为空");
                return;
            }
            register(registerPhone,password,sms);
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
                            threeBinding.tvRegisterThreeSend.setEnabled(false);
                            startTimer();
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });

        }
        //注册提交
        private void register(String mobile,String password,String sms){
            HttpUtils.getInstance().registerFinalStep(mobile,password,sms,
                    String.valueOf(TempAddress.provinceEntity.getId()),
                    String.valueOf(TempAddress.cityEntity.getId()),
                    String.valueOf(TempAddress.areaEntity.getId()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            ToastUtils.showShortToast(context,R.string.str_register_success);
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

        //跳转到选择地址页面
        public void skipChooseAddress(View view){
            //先请求省列表
            HttpUtils.getInstance().getProvinces()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(context,true) {
                        @Override
                        public void onSuccess(String t) {
                            Type type = new TypeToken<List<ProvinceEntity>>() {
                            }.getType();
                            List<ProvinceEntity> provinceEntities = new Gson().fromJson(t,type);
                            if(provinceEntities==null||provinceEntities.isEmpty()){
                                ToastUtils.showShortToast(context, R.string.str_no_data);
                            }else{
                                TempAddress.provinceEntities = provinceEntities;
                                context.startActivity(new Intent().setClass(context,AddressChooseActivity.class));
                            }
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {
                            ToastUtils.showShortToast(context,"获取地区数据失败");
                        }
                    });

        }

        public void setAddress(String address){
            threeBinding.tvRegisterThreeAddress.setText(address);
        }
    }

}
