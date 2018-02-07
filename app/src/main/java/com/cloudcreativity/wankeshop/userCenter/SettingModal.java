package com.cloudcreativity.wankeshop.userCenter;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.receiver.MyBusinessReceiver;
import com.cloudcreativity.wankeshop.utils.CacheUtils;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是设置中心的ViewModal
 */
public class SettingModal {
    private SettingActivity context;
    private BaseDialogImpl baseDialog;


    public ObservableField<String> cache = new ObservableField<>();//缓存
    public ObservableField<Boolean> isOpen = new ObservableField<>();//推送开关
    public ObservableField<String> version = new ObservableField<>();//版本信息

    SettingModal(SettingActivity context, BaseDialogImpl baseDialog) {
        this.context = context;
        this.baseDialog = baseDialog;
        cache.set(CacheUtils.getTotalCacheSize(context));
        isOpen.set(false);
        //获取版本信息
        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version.set("V"+packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version.set("V1.0");
        }
    }

    //退出页面
    public void onBack(View view){
        context.finish();
    }

    //关于我们
    public void onAboutClick(View view){
        context.startActivity(new Intent().setClass(context,AboutActivity.class));
    }

    //清除缓存
    public void onClearCache(View view){
        clearCache();
    }

    //登录退出
    public void onLogoutClick(View view){
        logout();
    }

    //退出登录
    private void logout(){
        HttpUtils.getInstance().logout(SPUtils.get().getUid(),SPUtils.get().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        ToastUtils.showShortToast(context,"已退出");
                        Intent intent = new Intent(MyBusinessReceiver.ACTION_LOGOUT);
                        context.sendBroadcast(intent);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
    //清除缓存
    private void clearCache(){
        CacheUtils.clearCache(context);
        cache.set(CacheUtils.getTotalCacheSize(context));
    }

    //推送的开关
    public Switch.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isOpen.set(isChecked);
            openOrClose(isChecked);
        }
    };
    private void openOrClose(boolean isOpen){

    }

}
