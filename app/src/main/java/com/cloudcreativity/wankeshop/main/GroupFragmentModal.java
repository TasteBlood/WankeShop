package com.cloudcreativity.wankeshop.main;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import com.cloudcreativity.wankeshop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是拼团的ViewModal
 */
public class GroupFragmentModal {

    public ArrayAdapter<String> arrayAdapter;

    GroupFragmentModal(Context context) {
        List<String> strings = new ArrayList<>();
        for(int i=0;i<30;i++){
            strings.add("I am data "+(i+1));
        }
        arrayAdapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,strings);
    }

    //这是筛选按钮的点击事件
    public void onDropTabClick(View view){
        switch (view.getId()){
                //类别
            case R.id.ll_groupCategoryDropDown:
                break;
                //品牌
            case R.id.ll_groupLabelDropDown:
                break;
                //排序
            case R.id.ll_groupSortDropDown:
                break;
        }
    }

    //显示dropDialog
    private void showDropWindow(){

    }

    //隐藏dropDialog
    private void dismissWindow(){

    }
}
