package com.cloudcreativity.wankeshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.cloudcreativity.wankeshop.main.MainActivity;

/**
 * 这是启动页
 */
public class IndexActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
                startActivity(new Intent(IndexActivity.this, MainActivity.class));
            }
        },3000);
    }
}
