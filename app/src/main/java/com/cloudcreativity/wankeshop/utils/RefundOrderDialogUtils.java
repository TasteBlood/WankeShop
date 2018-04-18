package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.LayoutRefundOrderDialogBinding;

/**
 * 申请退货的对话框工具
 */
public class RefundOrderDialogUtils {

    private Dialog dialog;
    private LayoutRefundOrderDialogBinding binding;
    private OnOkClickListener onOkClickListener;

    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    public void show(Context context){
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_refund_order_dialog,null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels/5*4;
        dialog.show();
    }

    public void dismiss(){
        if(dialog!=null)
            dialog.dismiss();
    }

    public void onOkClick(View view){
        String reason = binding.etReason.getText().toString().trim();
        if(TextUtils.isEmpty(reason)){
            return;
        }
        if(onOkClickListener!=null)
            onOkClickListener.onClick(reason);
        dismiss();
    }

    public interface OnOkClickListener{
        public void onClick(String reason);
    }

}
