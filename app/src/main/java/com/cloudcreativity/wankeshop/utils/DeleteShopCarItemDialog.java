package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.LayoutDeleteShopCarItemDialogBinding;

/**
 * 选择车牌dialog
 */
public class DeleteShopCarItemDialog {

    private Dialog dialog;

    public ObservableField<String> content = new ObservableField<>();

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void show(Context context, int number){

        content.set(String.format(context.getString(R.string.str_delete_shop_car_item_content),number));
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        LayoutDeleteShopCarItemDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_delete_shop_car_item_dialog,null,false);
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

    //取消
    public void onCancelClick(View view){
        dismiss();
        if(onClickListener!=null)
            onClickListener.onClick(false);
    }

    //确认点击
    public void onOkClick(View view){
        dismiss();
        //回掉事件
        if(onClickListener!=null)
            onClickListener.onClick(true);
    }



    //点击事件的回掉
    public interface OnClickListener{
        void onClick(boolean isOk);
    }
}
