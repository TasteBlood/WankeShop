package com.cloudcreativity.wankeshop.userCenter.address;

/**
 * 这是区的Fragment ViewModal
 */

import android.app.Activity;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.databinding.FragmentStreetBinding;
import com.cloudcreativity.wankeshop.databinding.ItemStreetFragmentLayoutBinding;
import com.cloudcreativity.wankeshop.entity.address.StreetEntity;

public class StreetFragmentModal {


    public BaseBindingRecyclerViewAdapter<StreetEntity,ItemStreetFragmentLayoutBinding> streetAdapter;

    private Activity context;
    private StreetFragment fragment;
    private FragmentStreetBinding binding;

    private StreetEntity lastSelectEntity;
    private int lastPosition;

    StreetFragmentModal(Activity context, StreetFragment fragment, FragmentStreetBinding binding) {
        this.context = context;
        this.fragment = fragment;
        this.binding = binding;
        streetAdapter = new BaseBindingRecyclerViewAdapter<StreetEntity, ItemStreetFragmentLayoutBinding>(this.context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_street_fragment_layout;
            }

            @Override
            protected void onBindItem(ItemStreetFragmentLayoutBinding binding, final StreetEntity item, final int position) {
                binding.setStreet(item);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick(item,position);
                    }
                });
            }
        };

        if(TempAddress.streetEntities!=null&&!TempAddress.streetEntities.isEmpty())
            streetAdapter.getItems().addAll(TempAddress.streetEntities);
    }

    public void refreshData(){
        streetAdapter.getItems().clear();
        streetAdapter.getItems().addAll(TempAddress.streetEntities);
    }

    //条目点击事件
    private void onItemClick(StreetEntity entity,int position){
        //相同的点击没反应
        if(entity==lastSelectEntity)
            return;
        if(lastSelectEntity!=null){
            lastSelectEntity.setCheck(false);
            streetAdapter.notifyItemChanged(lastPosition);
        }
        entity.setCheck(true);
        lastSelectEntity = entity;
        lastPosition = position;
        streetAdapter.notifyItemChanged(position);
        //LogUtils.e("xuxiwu","position="+position+"--lastPos="+lastPosition);

        //将当前的街道保存
        TempAddress.streetEntity = entity;

        //处理所有的信息
        context.finish();
    }
}
