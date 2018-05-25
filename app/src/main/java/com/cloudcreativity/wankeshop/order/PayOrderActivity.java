package com.cloudcreativity.wankeshop.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.base.PaySuccessActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityPayOrderBinding;
import com.cloudcreativity.wankeshop.utils.LogUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

/**
 * 支付订单页面
 */
public class PayOrderActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        ActivityPayOrderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_pay_order);
        //获取订单号和金额
        String orderNum = getIntent().getStringExtra("orderNum");
        float totalMoney = getIntent().getFloatExtra("totalMoney", 0.00f);
        binding.setPayModal(new PayOrderViewModal(this,this,binding,orderNum,totalMoney));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Map<String,String> result){
        if(result.containsKey("resultStatus")){
            //支付宝回掉
            if("9000".equals(result.get("resultStatus"))){
                //支付成功，跳转到支付成功页面
                startActivity(new Intent(this, PaySuccessActivity.class));
                finish();
            }else{
                ToastUtils.showShortToast(this,"支付失败");
            }
        }else if(result.containsKey("wechatPayResult")){
            //说明微信支付成功
            //startActivity(new Intent(this, PaySuccessActivity.class));
            finish();
        }
    }
}
