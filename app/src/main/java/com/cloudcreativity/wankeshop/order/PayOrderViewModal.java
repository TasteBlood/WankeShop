package com.cloudcreativity.wankeshop.order;

import android.databinding.ObservableField;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityPayOrderBinding;
import com.cloudcreativity.wankeshop.utils.AppConfig;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
        }else if(binding.cbWeChat.isChecked()){
            //微信支付
            IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, AppConfig.WX_APP_ID,true);
            //注册
            iwxapi.registerApp(AppConfig.WX_APP_ID);

            //检查是否支持支付
            if(!iwxapi.isWXAppInstalled()&&!iwxapi.isWXAppSupportAPI()){
                ToastUtils.showShortToast(context,"没有安装微信或不支持支付");
                return;
            }


        }else{
            //支付宝支付
        }
    }

}
