package com.cloudcreativity.wankeshop.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.base.PaySuccessActivity;
import com.cloudcreativity.wankeshop.order.OrderDetailViewModal;
import com.cloudcreativity.wankeshop.utils.AppConfig;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付回掉页面
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        //...
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (baseResp.errCode){
                case 0:
                    ToastUtils.showShortToast(this,"支付成功");
                    startActivity(new Intent(this, PaySuccessActivity.class));
                    Map<String,String> result = new HashMap<>();
                    result.put("wechatPayResult","success");
                    EventBus.getDefault().post(result);
                    break;
                case 1:
                    ToastUtils.showShortToast(this,"支付失败"+baseResp.errCode);
                    break;
                case 2:
                    ToastUtils.showShortToast(this,"支付失败，用户取消");
                    break;
            }
            finish();
        }
    }
}
