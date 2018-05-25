package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.LayoutChooseShopItemNumberDialogBinding;
import com.cloudcreativity.wankeshop.entity.ShopCarItemEntity;

/**
 * 选择车牌dialog
 */
public class ChooseShopItemNumberDialogUtils {

    private Dialog dialog;
    public ShopCarItemEntity entity;
    private LayoutChooseShopItemNumberDialogBinding binding;
    private OnNumberSaveListener onNumberSaveListener;
    private Context context;
    public ObservableField<String> numberLimit = new ObservableField<>();

    public void setOnNumberSaveListener(OnNumberSaveListener onNumberSaveListener) {
        this.onNumberSaveListener = onNumberSaveListener;
    }

    public void show(Context context, ShopCarItemEntity entity){
        this.context = context;
        this.entity = entity;
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_choose_shop_item_number_dialog,null,false);
        binding.setUtils(this);
        //格式化配送区间
        if("-".equals(entity.getSku().getRegionOperator())){
            //说明是最小范围和最大范围间配送
            numberLimit.set(String.format(context.getResources().getString(R.string.str_number_limit_min),String.valueOf(entity.getSku().getMinQuality()),entity.getSpu().getUnit().getUnityName())
                    .concat(String.format(context.getResources().getString(R.string.str_number_limit_max),String.valueOf(entity.getSku().getMaxQuality()))));
        }else if(">=".equals(entity.getSku().getRegionOperator())){
            //大于最小配送范围
            numberLimit.set(String.format(context.getResources().getString(R.string.str_number_limit_min),String.valueOf(entity.getSku().getMinQuality()),entity.getSpu().getUnit().getUnityName())
                    .concat(String.format(context.getResources().getString(R.string.str_number_limit_max),String.valueOf(entity.getSku().getDepositNum()))));
        }else{
            numberLimit.set(String.format(context.getResources().getString(R.string.str_number_limit_min),"0",entity.getSpu().getUnit().getUnityName()));
        }


        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels/4*3-100;
        dialog.show();
    }

    public void dismiss(){
        if(dialog!=null)
            dialog.dismiss();
    }

    //这是增加数量点击
    public void onPlusClick(View view){
        String string = binding.tvNumber.getText().toString();
        if(TextUtils.isEmpty(string))
            return;
        int number =Integer.parseInt(string) ;
        number+=1;
        if(number>entity.getSku().getDepositNum()){
            //显示信息
            ToastUtils.showLongToast(view.getContext(),"数量不得超过"+entity.getSku().getDepositNum());
            return;
        }
        //当最大值为0时，还需要判断数量区间的范围
        if(number>entity.getSku().getMaxQuality()&&!">=".equals(entity.getSku().getRegionOperator())){
            //显示信息
            ToastUtils.showLongToast(view.getContext(),"数量不得超过"+entity.getSku().getMaxQuality());
            return;
        }

        binding.tvNumber.setText(String.valueOf(number));

    }

    //这是减少数量点击
    public void onMinusClick(View view){
        String string = binding.tvNumber.getText().toString();
        if(TextUtils.isEmpty(string))
            return;
        int number =Integer.parseInt(string) ;
        number-=1;
        if(number<entity.getSku().getMinQuality()){
            //显示信息
            ToastUtils.showLongToast(view.getContext(),"数量不得低于"+entity.getSku().getMinQuality());
            return;
        }

        binding.tvNumber.setText(String.valueOf(number));
    }


    public void afterTextChanged(Editable s) {
        if(TextUtils.isEmpty(s))
            return;
        int number = Integer.parseInt(s.toString().trim());
        if(number>entity.getSku().getDepositNum()){
            //显示信息
            ToastUtils.showLongToast(context,"数量不得超过"+entity.getSku().getDepositNum());
            binding.tvNumber.setText(String.valueOf(entity.getSku().getDepositNum()));
            binding.tvNumber.setSelection(binding.tvNumber.getText().length());
            return;
        }
        //当最大值为0时，还需要判断数量区间的范围
        if(number>entity.getSku().getMaxQuality()&&"-".equals(entity.getSku().getRegionOperator())){
            //显示信息
            ToastUtils.showLongToast(context,"数量不得超过"+entity.getSku().getMaxQuality());
            binding.tvNumber.setText(String.valueOf(entity.getSku().getMaxQuality()));
            binding.tvNumber.setSelection(binding.tvNumber.getText().length());
            return;
        }
        //数量小于配送范围时
        if(number<entity.getSku().getMinQuality()){
            //显示信息
            binding.tvNumber.setSelection(s.length());
            return;
        }
        binding.tvNumber.setSelection(s.length());
    }

    public void onSaveClick(View view){
        String trim = binding.tvNumber.getText().toString().trim();
        if(TextUtils.isEmpty(trim)){
            ToastUtils.showLongToast(view.getContext(),"数量不符合配送规范");
            return;
        }
        int number = Integer.parseInt(trim);
        if(number>entity.getSku().getDepositNum()){
            //显示信息
            ToastUtils.showLongToast(context,"数量不得超过"+entity.getSku().getDepositNum());
            return;
        }
        //当最大值为0时，还需要判断数量区间的范围
        if(number>entity.getSku().getMaxQuality()&&"-".equals(entity.getSku().getRegionOperator())){
            //显示信息
            ToastUtils.showLongToast(context,"数量不得超过"+entity.getSku().getMaxQuality());
            return;
        }
        //数量小于配送范围时
        if(number<entity.getSku().getMinQuality()){
            //显示信息
            ToastUtils.showLongToast(context,"数量不得低于"+entity.getSku().getMinQuality());
            return;
        }
        if(onNumberSaveListener!=null)
            onNumberSaveListener.onSaveClick(number);
        dismiss();
    }

    public interface OnNumberSaveListener{
          void onSaveClick(int number);
    }
}
