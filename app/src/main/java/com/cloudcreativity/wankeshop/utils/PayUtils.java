package com.cloudcreativity.wankeshop.utils;

import android.app.Activity;
import android.content.Context;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * 这是支付工具包含微信、支付宝等
 */
public class PayUtils {

    public static int FLAG_PAY_BY_ALI = 0x12034;

    /**
     *
     * @param context 上下文
     * @param partnerId 商户id
     * @param prepayId 预支付订单id
     * @param nonceStr 特殊字符串
     * @param timeStamp 时间戳
     * @param sign 签名值
     */
    public static void payByWeXin(Context context,String partnerId,String prepayId,String packages,String nonceStr,String timeStamp,String sign){
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context,null);
        iwxapi.registerApp(AppConfig.WX_APP_ID);
        if(!iwxapi.isWXAppInstalled()||!iwxapi.isWXAppSupportAPI()){
            ToastUtils.showShortToast(context,"微信未安装或者当前版本不支持");
            return;
        }
        PayReq request = new PayReq();
        request.appId = AppConfig.WX_APP_ID;//微信开放平台的app id
        request.partnerId = partnerId;//商户id
        request.prepayId = prepayId;//预支付订单号
        request.packageValue = packages;//包类型
        request.nonceStr = nonceStr;//随机字符串
        request.timeStamp = timeStamp;//时间戳
        request.sign = sign;//签名值

        iwxapi.sendReq(request);
    }

    /**
     * 支付宝支付
     */
    public static void payByALiPay(final Activity context,final String orderInfo){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String,String> result = alipay.payV2(orderInfo,true);
                EventBus.getDefault().post(result);
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 余额支付
     */
    public static void payByBalance(){

    }
}
