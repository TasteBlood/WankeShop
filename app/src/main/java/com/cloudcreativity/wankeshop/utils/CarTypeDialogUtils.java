package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.LayoutCarTypeDialogBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆类型选择对话框工具
 */
public class CarTypeDialogUtils {
    public static  String[] typeNames = {"小型汽车","三轮摩托车","电瓶车","二轮摩托车","四轮农用运输车","三轮农用运输车"};
    private int[] ids = {1,3,2,4,5,6};
    private int[] typePics = {R.mipmap.ic_car1,R.mipmap.ic_car4,R.mipmap.ic_car2,R.mipmap.ic_car3,R.mipmap.ic_car5,R.mipmap.ic_car6};

    private Dialog dialog;
    public BaseAdapter adapter;

    private OnCarTypeClickListener onCarTypeClickListener;

    public void setOnCarTypeClickListener(OnCarTypeClickListener onCarTypeClickListener) {
        this.onCarTypeClickListener = onCarTypeClickListener;
    }

    public void show(Context context){
        //初始化数据
        final List<Map<String,Object>> dataList = new ArrayList<>();
        for(int i=0;i<typeNames.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("name",typeNames[i]);
            map.put("image",typePics[i]);
            dataList.add(map);
        }
        adapter = new SimpleAdapter(context,dataList,R.layout.layout_item_car_type,new String[]{"image","name"},new int[]{R.id.iv_carType,R.id.iv_carTypeName});
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        LayoutCarTypeDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_car_type_dialog,
                null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        //window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels/4*3;
        dialog.show();
        binding.gvCarTypeDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onCarTypeClickListener!=null)
                    onCarTypeClickListener.onItemClick(ids[position],typeNames[position]);
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    public interface OnCarTypeClickListener{
         void onItemClick(int typeCode,String typeName);
    }
}
