package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.CommonPayWayDialogLayoutBinding;
/**
 * 支付方式选择对话框工具
 */
public class PayWayDialogUtils {
    private  Dialog dialog;


    public void show(Context context){
        dialog = new Dialog(context, R.style.myDialogStyleAnim);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        CommonPayWayDialogLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.common_pay_way_dialog_layout,null,false);
        binding.setModal(this);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = context.getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(attributes);
        dialog.show();
    }

    /**
     * 支付宝点击
     * @param view
     */
    public void onAliPayClick(View view){
        ToastUtils.showShortToast(view.getContext(),"支付宝发起支付");
        dialog.dismiss();
        dialog = null;
    }

    /**
     * 微信支付点击
     * @param view
     */
    public void onWechatClick(View view){
        ToastUtils.showShortToast(view.getContext(),"微信发起支付");
        dialog.dismiss();
        dialog = null;
    }

    /**
     * 余额支付点击
     * @param view
     */
    public void onBalanceClick(View view){
        ToastUtils.showShortToast(view.getContext(),"余额发起支付");
        dialog.dismiss();
        dialog = null;
    }

    /**
     * 取消点击
     * @param view
     */
    public void onCancelClick(View view){
        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }

    }
}
