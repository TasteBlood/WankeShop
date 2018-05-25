package com.cloudcreativity.wankeshop.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.entity.address.ProvinceEntity;
import com.cloudcreativity.wankeshop.main.MineFragmentModal;
import com.cloudcreativity.wankeshop.order.AllOrderActivity;
import com.cloudcreativity.wankeshop.order.OrderDetailViewModal;
import com.cloudcreativity.wankeshop.view.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * 支付成功Activity
 */
public class PaySuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //可能服务器异步通知会延迟，所以在这里做阻塞情况
        final ProgressDialog dialog = ProgressDialog.show(this,"提示","支付结果返回中，请稍后",true,false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                setContentView(R.layout.activity_pay_success);
                findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                findViewById(R.id.tv_skipOrder).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PaySuccessActivity.this, AllOrderActivity.class);
                        intent.putExtra("state", MineFragmentModal.STATE_WAIT_RECEIVE);
                        startActivity(intent);
                        finish();
                    }
                });
                EventBus.getDefault().post(OrderDetailViewModal.MSG_PAY_SUCCESS);
            }
        },5000);

    }
}
