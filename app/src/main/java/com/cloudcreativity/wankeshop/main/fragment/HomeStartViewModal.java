package com.cloudcreativity.wankeshop.main.fragment;

import android.content.Context;
import android.view.View;

import com.cloudcreativity.wankeshop.base.BaseDialogImpl;

/**
 * 这是首页第一个页面的ViewModal
 */
public class HomeStartViewModal {

    public HomeStartAdapter adapter;

    private BaseDialogImpl dialogImpl;

    private Context context;

    HomeStartViewModal(BaseDialogImpl dialogImpl,Context context) {
        this.dialogImpl = dialogImpl;
        this.context = context;
        adapter = new HomeStartAdapter(this.context);
    }
    //这是菜单的点击事件
    public void onMenuItemClick(View view){

    }
    //这是Banner的点击事件
    public void onBannerClick(){

    }
    //这是活动的点击事件
    public void onActivityClick(View view){

    }
}
