package com.cloudcreativity.wankeshop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.cloudcreativity.wankeshop.base.BaseApp;
import com.cloudcreativity.wankeshop.loginAndRegister.LoginActivity;
import com.cloudcreativity.wankeshop.main.MainActivity;
import com.cloudcreativity.wankeshop.utils.SPUtils;

/**
 * 这是启动页
 */
public class IndexActivity extends Activity {

    private View indexImage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApp.app.addActivity(this);
        setContentView(R.layout.activity_index);
        indexImage = findViewById(R.id.iv_index);
        if(ContextCompat.checkSelfPermission(BaseApp.app,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(BaseApp.app,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            final boolean isLogin = SPUtils.get().isLogin();
            startAnimation(isLogin);
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
                boolean isLogin = SPUtils.get().isLogin();
                startAnimation(isLogin);
            }else{
                finish();
            }
        }
    }

    private void startAnimation(final boolean isLogin){
        ScaleAnimation animation = new ScaleAnimation(1.0f,1.08f,1.0f,1.08f,
                ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onBackPressed();
                if(isLogin){
                    onBackPressed();
                    startActivity(new Intent(IndexActivity.this, MainActivity.class));
                }else{
                    onBackPressed();
                    startActivity(new Intent(IndexActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        indexImage.startAnimation(animation);
    }
}
