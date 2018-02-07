package com.cloudcreativity.wankeshop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import com.cloudcreativity.wankeshop.base.BaseApp;
import com.cloudcreativity.wankeshop.loginAndRegister.LoginActivity;
import com.cloudcreativity.wankeshop.main.MainActivity;
import com.cloudcreativity.wankeshop.utils.SPUtils;

/**
 * 这是启动页
 */
public class IndexActivity extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApp.app.addActivity(this);
        setContentView(R.layout.activity_index);
        if(ContextCompat.checkSelfPermission(BaseApp.app,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(BaseApp.app,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            final boolean isLogin = SPUtils.get().isLogin();
            if(isLogin){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                        startActivity(new Intent(IndexActivity.this, MainActivity.class));
                    }
                },2000);
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                        startActivity(new Intent(IndexActivity.this, LoginActivity.class));
                    }
                },2000);
            }
        }else{
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode!=100)
            return;
        if(permissions[0].equals(Manifest.permission.CAMERA)&&permissions[1].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                final boolean isLogin = SPUtils.get().isLogin();
                if(isLogin){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                            startActivity(new Intent(IndexActivity.this, MainActivity.class));
                        }
                    },2000);
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                            startActivity(new Intent(IndexActivity.this, LoginActivity.class));
                        }
                    },2000);
                }
            }else{
                finish();
            }
        }
    }
}
