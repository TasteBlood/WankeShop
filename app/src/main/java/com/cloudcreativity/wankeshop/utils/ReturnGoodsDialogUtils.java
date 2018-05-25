package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.entity.OrderEntity;
import com.cloudcreativity.wankeshop.databinding.LayoutReturnOrderDialogBinding;

/**
 * 退货填写原因和数量dialog
 */
public class ReturnGoodsDialogUtils {

    private Dialog dialog;
    private OrderEntity entity;
    private LayoutReturnOrderDialogBinding binding;
    private OnOkClickListener onOkClickListener;

    //是否是全退
    public ObservableField<Boolean> isAll = new ObservableField<>();

    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    public void show(Context context, OrderEntity entity, boolean isAll){
        this.isAll.set(isAll);
        this.entity = entity;
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_return_order_dialog,null,false);
        binding.setUtils(this);
        if(!isAll)
            binding.tvMaxNumber.setText("(最多可退".concat(entity.getShoppingCart().getNum()+"").concat(")"));
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

        String reason = binding.tvReason.getText().toString().trim();
        if(TextUtils.isEmpty(reason)){
            return;
        }
        int number = 0;
        if(!isAll.get()){
            String numberStr = binding.tvNumber.getText().toString().trim();
            if(TextUtils.isEmpty(numberStr)){
                return;
            }
            number = Integer.parseInt(numberStr);
            if(number>entity.getShoppingCart().getNum())
                return;
        }
        if(onOkClickListener!=null)
            onOkClickListener.onOkClick(reason,number);

        dismiss();
    }

    public interface OnOkClickListener{
        public void onOkClick(String reason,int number);
    }

}
