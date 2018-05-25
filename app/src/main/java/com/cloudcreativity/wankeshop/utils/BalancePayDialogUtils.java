package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.LayoutBalancePayDialogBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 退货填写原因和数量dialog
 */
public class BalancePayDialogUtils implements DialogInterface.OnCancelListener{

    private Dialog dialog;
    private LayoutBalancePayDialogBinding binding;

    private CountDownTimer timer;

    private BaseDialogImpl baseDialog;

    private OnOkClickListener onOkClickListener;

    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    public void show(Context context, BaseDialogImpl baseDialog){
        this.baseDialog = baseDialog;

        dialog = new Dialog(context, R.style.myDialogStyleAnim);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_balance_pay_dialog,null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels;
        binding.tvHints.setText("已为尾号是".concat(SPUtils.get().getUser().getMobile().substring(7,11)).concat("的手机号发送验证码，请注意查收"));
        dialog.show();
        startTimer(context);
    }

    public void dismiss(){
        if(dialog!=null)
            dialog.dismiss();
    }

    //重新发送验证码点击
    public void onReSendClick(View view){
        sendSms(view.getContext(),SPUtils.get().getUser().getMobile());
    }

    //确定点击
    public void onOkClick(View view){
        //进行支付
        String sms = binding.etSms.getText().toString().trim();
        if(TextUtils.isEmpty(sms)){
            ToastUtils.showShortToast(view.getContext(),"验证码不能为空");
            return;
        }
        if(onOkClickListener!=null)
            onOkClickListener.onClick(sms);

        dialog.dismiss();
    }

    //关闭
    public void close(View view){
        dialog.dismiss();
    }

    private void startTimer(final Context context){
        if(timer!=null)
            return;
        binding.tvSendAgain.setEnabled(false);
        timer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvSendAgain.setText(
                        String.format(context.getResources().getString(R.string.str_register_time_count),
                                millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                timer = null;
                binding.tvSendAgain.setEnabled(false);
                binding.tvSendAgain.setText("重新发送");
            }
        };
        timer.start();

    }

    private void stopTimer(){
        if(timer!=null)
            timer.cancel();
        timer = null;

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        stopTimer();
    }

    //发送验证码
    private void sendSms(final Context context , String mobile){
        HttpUtils.getInstance().registerGetVerRify(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        ToastUtils.showShortToast(context,R.string.str_register_code_sent);
                        binding.tvSendAgain.setEnabled(false);
                        startTimer(context);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

    }

    public interface OnOkClickListener{
        public void onClick(String sms);
    }
}
