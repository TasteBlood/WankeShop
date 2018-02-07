package com.cloudcreativity.wankeshop.userCenter;

import android.content.Context;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.entity.AddressEntity;
import com.cloudcreativity.wankeshop.databinding.ItemAddressManageLayoutBinding;

/**
 * 这是地址列表适配器
 */
public class AddressAdapter extends BaseBindingRecyclerViewAdapter<AddressEntity,ItemAddressManageLayoutBinding> {
    AddressAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_address_manage_layout;
    }

    @Override
    protected void onBindItem(ItemAddressManageLayoutBinding binding, AddressEntity item, int position) {
        binding.setAddress(item);
        binding.executePendingBindings();
        //添加修改事件
        binding.tvAddressManageModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //删除事件
        binding.tvAddressManageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //item点击事件
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
