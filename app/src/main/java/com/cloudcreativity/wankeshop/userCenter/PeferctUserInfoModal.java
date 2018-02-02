package com.cloudcreativity.wankeshop.userCenter;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;

/**
 * 这是完善信息的ViewModal
 */
public class PeferctUserInfoModal {

    private PerfectUserInfoActivity context;
    //这是用户数据
    public UserEntity user;

    PeferctUserInfoModal(PerfectUserInfoActivity context) {
        this.context = context;
        user = SPUtils.get().getUser();
    }

    public void onBack(View view){
        context.finish();
    }

    //保存操作
    public void onSave(View view){

    }

    @BindingAdapter("imageUrl")
    public static void showAvatar(ImageView imageView, String url){
        if(TextUtils.isEmpty(url)){
            GlideUtils.loadCircle(imageView.getContext(), R.mipmap.ic_default_head,imageView);
        }else{
            GlideUtils.loadCircle(imageView.getContext(),url,imageView);
        }

    }
}
