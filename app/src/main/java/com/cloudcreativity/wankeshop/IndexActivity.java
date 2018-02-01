package com.cloudcreativity.wankeshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.cloudcreativity.wankeshop.loginAndRegister.LoginActivity;
import com.cloudcreativity.wankeshop.main.MainActivity;
import com.cloudcreativity.wankeshop.utils.SPUtils;

/**
 * 这是启动页
 */
public class IndexActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        final boolean isLogin = SPUtils.get().isLogin();
        if(isLogin){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                    startActivity(new Intent(IndexActivity.this, MainActivity.class));
                }
            },3000);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                    startActivity(new Intent(IndexActivity.this, LoginActivity.class));
                }
            },3000);
        }

    }
}
