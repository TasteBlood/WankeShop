package com.cloudcreativity.wankeshop.userCenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.entity.AddressEntity;
import com.cloudcreativity.wankeshop.databinding.ItemAddressManageLayoutBinding;
import com.cloudcreativity.wankeshop.userCenter.address.EditAddressActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是地址列表适配器
 */
public class AddressAdapter extends BaseBindingRecyclerViewAdapter<AddressEntity,ItemAddressManageLayoutBinding> {
    private Context context;
    private BaseDialogImpl baseDialog;
    AddressAdapter(Context context,BaseDialogImpl baseDialog) {
        super(context);
        this.context = context;
        this.baseDialog = baseDialog;
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_address_manage_layout;
    }

    @Override
    protected void onBindItem(final ItemAddressManageLayoutBinding binding, final AddressEntity item, int position) {
        binding.setAddress(item);
        binding.executePendingBindings();
        //添加修改事件
        binding.tvAddressManageModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditAddressActivity.class);
                intent.putExtra("addressEntity",item);
                context.startActivity(intent);
            }
        });

        //删除事件
        binding.tvAddressManageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(item);
            }
        });

        //默认地址选择事件
        binding.cbAddressManageIsDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getIsDefault()==0&&binding.cbAddressManageIsDefault.isChecked()){
                    //说明就是选中，修改默认选中地址
                    HttpUtils.getInstance().editAddress(item.getId(),
                            Integer.parseInt(item.getProvinceId()),Integer.parseInt(item.getCityId()),Integer.parseInt(item.getAreaId()),
                            TextUtils.isEmpty(item.getStreetId())?0:Integer.parseInt(item.getStreetId()),
                            item.getAddressInfo(),item.getZipCode(),item.getReceiptName(),item.getReceiptMobile(),1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                @Override
                                public void onSuccess(String t) {
                                    //刷新界面
                                    EventBus.getDefault().post(AddressManageActivity.MSG_REFRESH_DATA);
                                }

                                @Override
                                public void onFail(ExceptionReason msg) {

                                }
                            });
                }
            }
        });

        //item点击事件
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    //显示删除的对话框
    private void showDeleteDialog(final AddressEntity entity){
        final Dialog dialog = new Dialog(context,R.style.myProgressDialogStyle);
        View content = View.inflate(context,R.layout.common_delete_dialog_layout,null);
        dialog.setContentView(content);
        dialog.setCanceledOnTouchOutside(true);
        content.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        content.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                dialog.dismiss();
                HttpUtils.getInstance().deleteAddress(entity.getId())
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<String>(baseDialog,true) {
                            @Override
                            public void onSuccess(String t) {
                                //刷新数据
                                EventBus.getDefault().post(AddressManageActivity.MSG_REFRESH_DATA);
                            }

                            @Override
                            public void onFail(ExceptionReason msg) {

                            }
                        });
            }
        });
        dialog.show();
    }

}
