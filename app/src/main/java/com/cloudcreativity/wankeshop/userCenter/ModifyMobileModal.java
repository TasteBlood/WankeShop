package com.cloudcreativity.wankeshop.userCenter;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.ActivityModifyMobileBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutModifyPhoneStepOneBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutModifyPhoneStepTwoBinding;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是修改手机号的ViewModal
 */
public class ModifyMobileModal {
    private ModifyMobileActivity modifyMobileActivity;

    ModifyMobileModal(ModifyMobileActivity context) {
        modifyMobileActivity = context;
    }

    public void onBack(View view) {
        modifyMobileActivity.finish();
    }

    /**
     * 这是第一步
     */
    public static class StepOneModal {
        private ViewStub stub;
        private LayoutModifyPhoneStepOneBinding stepOneBinding;
        private ActivityModifyMobileBinding activityModifyMobileBinding;
        private CountDownTimer timer;
        private ModifyMobileActivity context;

        StepOneModal(ViewStub stub, final LayoutModifyPhoneStepOneBinding binding,
                     final ActivityModifyMobileBinding activityModifyMobileBinding,
                     final ModifyMobileActivity context) {
            this.stub = stub;
            this.stepOneBinding = binding;
            this.context = context;
            this.activityModifyMobileBinding = activityModifyMobileBinding;
            timer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    stepOneBinding.tvModifyCodeSend.setText(
                            String.format(context.getResources().getString(R.string.str_register_time_count),
                                    millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    stepOneBinding.tvModifyCodeSend.setEnabled(true);
                    stepOneBinding.tvModifyCodeSend.setText(R.string.str_register_send_code);
                }
            };
        }

        public void onSendClick(View view) {
            sendSms();
        }

        public void onNext(View view) {
            String sms = stepOneBinding.etModifyVerifyCodeOld.getText().toString().trim();
            if (TextUtils.isEmpty(sms)) {
                ToastUtils.showShortToast(context, R.string.str_sms_not_null);
                return;
            }
            next(sms);
        }

        //发送验证码
        private void sendSms() {
            HttpUtils.getInstance().modifyMobileOne(SPUtils.get().getUid(), SPUtils.get().getToken(), false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(context, true) {
                        @Override
                        public void onSuccess(String t) {
                            ToastUtils.showShortToast(context, R.string.str_register_code_sent);
                            stepOneBinding.tvModifyCodeSend.setEnabled(false);
                            startTimer();
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });

        }

        //下一步
        private void next(String sms) {
            HttpUtils.getInstance().modifyMobileTwo(SPUtils.get().getUid(), SPUtils.get().getToken(), sms)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(context, true) {
                        @Override
                        public void onSuccess(String t) {
                            //成功就进行转场
                            stub.setVisibility(View.GONE);
                            activityModifyMobileBinding.stubModifyTwo.getViewStub().inflate();
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });
        }

        private void startTimer() {
            timer.start();
        }

        public void stopTimer() {
            if (timer != null)
                timer.cancel();
        }
    }

    /**
     * 这是第二步
     */
    public static class StepTwoModal {
        private LayoutModifyPhoneStepTwoBinding stepTwoBinding;
        private CountDownTimer timer;
        private ModifyMobileActivity context;
        private String newMobile;

        public StepTwoModal(final ModifyMobileActivity context, LayoutModifyPhoneStepTwoBinding binding) {
            this.context = context;
            this.stepTwoBinding = binding;
            timer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    stepTwoBinding.tvModifyCodeSend.setText(
                            String.format(context.getResources().getString(R.string.str_register_time_count), millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    stepTwoBinding.tvModifyCodeSend.setEnabled(true);
                    stepTwoBinding.tvModifyCodeSend.setText(R.string.str_register_send_code);
                }
            };
        }

        public void onSendClick(View view) {
            newMobile = stepTwoBinding.etModifyPhoneNew.getText().toString().trim();
            if (TextUtils.isEmpty(newMobile) || !StrUtils.isPhone(newMobile)) {
                ToastUtils.showShortToast(context, R.string.str_login_phone_format_error);
                return;
            }
            sendSms(newMobile);
        }

        public void onNext(View view) {
            String phone = stepTwoBinding.etModifyPhoneNew.getText().toString().trim();
            String sms = stepTwoBinding.etModifyVerifyCodeNew.getText().toString().trim();
            if (TextUtils.isEmpty(phone) || !newMobile.equals(phone)) {
                ToastUtils.showShortToast(context, R.string.str_phone_not_unit);
                return;
            }
            if (TextUtils.isEmpty(sms)) {
                ToastUtils.showShortToast(context, R.string.str_sms_not_null);
                return;
            }
            save(newMobile, sms);
        }

        //发送验证码
        private void sendSms(String mobile) {
            HttpUtils.getInstance().modifyMobileThree(SPUtils.get().getUid(), SPUtils.get().getToken(), mobile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(context, true) {
                        @Override
                        public void onSuccess(String t) {
                            ToastUtils.showShortToast(context, R.string.str_register_code_sent);
                            stepTwoBinding.tvModifyCodeSend.setEnabled(false);
                            startTimer();
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });

        }

        //下一步
        private void save(String mobile, String sms) {
            HttpUtils.getInstance().modifyMobileFour(SPUtils.get().getUid(), SPUtils.get().getToken(), mobile, sms)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(context, true) {
                        @Override
                        public void onSuccess(String t) {
                            //这里保存数据
                            context.finish();
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });
        }

        private void startTimer() {
            timer.start();
        }

        public void stopTimer() {
            if (timer != null)
                timer.cancel();
        }
    }
}
