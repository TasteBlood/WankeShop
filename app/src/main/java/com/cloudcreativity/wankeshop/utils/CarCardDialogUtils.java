package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.cloudcreativity.wankeshop.BR;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.LayoutCarCardDialogBinding;
/**
 * 选择车牌dialog
 */
public class CarCardDialogUtils {

    private Dialog dialog;
    private OnCarCardClickListener onCarCardClickListener;

    public static final  int blue_card = 1;
    public static final  int yellow_card = 2;
    public static final  int green_card = 3;
    public static final  int yellow_green_card = 4;


    public void setOnCarCardClickListener(CarCardDialogUtils.OnCarCardClickListener onCarCardClickListener) {
        this.onCarCardClickListener = onCarCardClickListener;
    }

    public void show(Context context){
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        LayoutCarCardDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_car_card_dialog,
                null,false);
        binding.setVariable(BR.utils,this);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels/4*3-100;
        dialog.show();
    }

    /**
     * 蓝色车牌
     * @param view
     */
    public void onBlueClick(View view){
        dialog.dismiss();
        dialog = null;
        if(onCarCardClickListener!=null)
            onCarCardClickListener.onItemClick(blue_card,"蓝牌");

    }

    /**
     * 黄色车牌
     * @param view
     */
    public void onYellowClick(View view){
        dialog.dismiss();
        dialog = null;
        if(onCarCardClickListener!=null)
            onCarCardClickListener.onItemClick(yellow_card,"黄牌");
    }

    /**
     * 绿色车牌
     * @param view
     */
    public void onGreenClick(View view){
        dialog.dismiss();
        dialog = null;
        if(onCarCardClickListener!=null)
            onCarCardClickListener.onItemClick(green_card,"绿牌");
    }

    /**
     * 黄色和绿色车牌
     * @param view
     */
    public void onYellowAndGreenClick(View view){
        dialog.dismiss();
        dialog = null;
        if(onCarCardClickListener!=null)
            onCarCardClickListener.onItemClick(yellow_green_card,"黄绿牌");
    }

    /**
     * 这是点击事件的回掉
     */
    public interface OnCarCardClickListener{
         void onItemClick(int cardId,String colorName);
    }
}
