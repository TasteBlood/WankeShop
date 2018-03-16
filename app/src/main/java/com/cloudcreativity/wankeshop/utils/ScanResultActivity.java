package com.cloudcreativity.wankeshop.utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseApp;

/**
 * 这是扫描二维码结果的activity
 */
@SuppressLint("Registered")
public class ScanResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApp.app.addActivity(this);
        setContentView(R.layout.activity_scan_result);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = findViewById(R.id.tv_result);
        textView.setTextIsSelectable(true);
        textView.setText(getIntent().getStringExtra("result"));
    }
}
