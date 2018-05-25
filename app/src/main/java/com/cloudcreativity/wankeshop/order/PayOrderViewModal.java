package com.cloudcreativity.wankeshop.order;

import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.base.PaySuccessActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityPayOrderBinding;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.utils.AppConfig;
import com.cloudcreativity.wankeshop.utils.BalancePayDialogUtils;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.PayUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付订单VIewModal
 */
public class PayOrderViewModal {

    private PayOrderActivity context;
    private BaseDialogImpl baseDialog;
    private ActivityPayOrderBinding binding;
    private String orderNum;
    private float totalMoney;

    public ObservableField<String> finalMoneyDisplay = new ObservableField<>();

    PayOrderViewModal(PayOrderActivity context, BaseDialogImpl baseDialog,ActivityPayOrderBinding binding, String orderNum,float totalMoney) {
        this.context = context;
        this.baseDialog = baseDialog;
        this.binding = binding;
        this.orderNum = orderNum;
        this.totalMoney = totalMoney;

        binding.tvTotalPrice.setText(String.format(context.getString(R.string.str_rmb_character),totalMoney));
        binding.tvBalance.setText("(余额可用".concat(SPUtils.get().getUser().getBalance()).concat(")"));

        finalMoneyDisplay.set("余额支付".concat(String.format(context.getString(R.string.str_rmb_character),totalMoney)));
        binding.cbBalance.setChecked(true);

    }

    public void onBack(View view){
        context.finish();
    }

    //当余额点击
    public void onBalanceClick(View view){
        finalMoneyDisplay.set("余额支付".concat(String.format(context.getString(R.string.str_rmb_character),totalMoney)));
        binding.cbBalance.setChecked(true);
        binding.cbAliPay.setChecked(false);
        binding.cbWeChat.setChecked(false);
    }

    //当微信点击
    public void onWeChatClick(View view){
        finalMoneyDisplay.set("微信支付".concat(String.format(context.getString(R.string.str_rmb_character),totalMoney)));
        binding.cbWeChat.setChecked(true);
        binding.cbBalance.setChecked(false);
        binding.cbAliPay.setChecked(false);
    }

    //当支付宝点击
    public void onAliPayClick(View view){
        finalMoneyDisplay.set("支付宝支付".concat(String.format(context.getString(R.string.str_rmb_character),totalMoney)));
        binding.cbAliPay.setChecked(true);
        binding.cbBalance.setChecked(false);
        binding.cbWeChat.setChecked(false);
    }

    //当支付被点击
    public void onPayClick(View view){
        //发起支付了
        if(binding.cbBalance.isChecked()){
            //余额支付
            startBalancePay();
        }else if(binding.cbWeChat.isChecked()){
            //微信支付
            HttpUtils.getInstance().getWeiXinOrder(context.getString(R.string.app_name).concat("-").concat("购买商品"),
                    orderNum,
                    StrUtils.yuan2FenInt(0.01f),
                    2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            try {
                                JSONObject object = new JSONObject(t);
                                PayUtils.payByWeXin(context,
                                        object.getString("partnerNum"),
                                        object.getString("prepayid"),
                                        "Sign=WXPay",
                                        object.getString("nonceStr"),
                                        object.getString("timestamp"),
                                        object.getString("sign"));
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(context,"获取订单失败，请稍后重试");
                            }

                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });


        }else{
            //支付宝支付
            HttpUtils.getInstance().getALiPayOrder(context.getString(R.string.app_name).concat("-").concat("购买商品"),
                    context.getString(R.string.app_name).concat("-").concat("购买商品"),
                    orderNum,
                    0.1f,
                    2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            PayUtils.payByALiPay(context,t);
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });
        }
    }

    //开始余额支付
    private void startBalancePay() {
        if(Float.parseFloat(SPUtils.get().getUser().getBalance())<totalMoney){
            ToastUtils.showShortToast(context,"账户余额不足，请充值");
            return;
        }
        //发送验证码，并且进行支付
        HttpUtils.getInstance().registerGetVerRify(SPUtils.get().getUser().getMobile())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        BalancePayDialogUtils utils = new BalancePayDialogUtils();
                        utils.show(context,baseDialog);
                        utils.setOnOkClickListener(new BalancePayDialogUtils.OnOkClickListener() {
                            @Override
                            public void onClick(String sms) {
                                //进行余额支付
                                HttpUtils.getInstance().balancePay(orderNum,totalMoney,2,SPUtils.get().getUser().getMobile(),sms)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                            @Override
                                            public void onSuccess(String t) {
                                                //支付成功
                                                Intent intent = new Intent(context, PaySuccessActivity.class);
                                                context.startActivity(intent);
                                                EventBus.getDefault().post(OrderDetailViewModal.MSG_PAY_SUCCESS);
                                                context.finish();
                                            }

                                            @Override
                                            public void onFail(ExceptionReason msg) {
                                                //支付失败，请重试

                                            }
                                        });
                            }
                        });
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

    }

}
