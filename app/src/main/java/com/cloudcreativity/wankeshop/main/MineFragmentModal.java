package com.cloudcreativity.wankeshop.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.loginAndRegister.LoginActivity;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;

/**
 * 这是MineFragment的ViewModal
 */
public class MineFragmentModal {

    public int resouces = R.mipmap.start_index;
    private Context context;

    MineFragmentModal(Context context) {
        this.context = context;
    }

    @BindingAdapter("imageUrl")
    public static void showAvatar(ImageView imageView,int resource){
        GlideUtils.loadCircle(imageView.getContext(),resource,imageView);
    }

    public void onLogoutClick(View view){
        //模拟退出登录
        SPUtils.get().putBoolean(SPUtils.Config.IS_LOGIN,false);
    }
}
