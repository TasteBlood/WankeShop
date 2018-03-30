package com.cloudcreativity.wankeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.android.databinding.library.baseAdapters.BR;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ItemChooseReceiveAddressBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutDialogChooseReceiveAddressBinding;
import com.cloudcreativity.wankeshop.entity.AddressEntity;
import com.cloudcreativity.wankeshop.userCenter.address.AddAddressActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择收货地址dialog
 */
public class ChooseReceiveAddressDialogUtils {

    private Dialog dialog;

    public BaseBindingRecyclerViewAdapter<AddressEntity,ItemChooseReceiveAddressBinding> adapter;

    private Context context;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ChooseReceiveAddressDialogUtils(Context context) {
        this.context = context;
        adapter = new BaseBindingRecyclerViewAdapter<AddressEntity, ItemChooseReceiveAddressBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_choose_receive_address;
            }

            @Override
            protected void onBindItem(ItemChooseReceiveAddressBinding binding, final AddressEntity item, int position) {
                    binding.setAddress(item);
                    binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(onItemClickListener!=null)
                                onItemClickListener.onItemClick(item);

                            dismiss();
                        }
                    });
            }
        };
    }

    public void show(List<AddressEntity> addressEntities){
        dialog = new Dialog(context, R.style.myDialogStyleAnim);
        dialog.setCanceledOnTouchOutside(true);
        LayoutDialogChooseReceiveAddressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_dialog_choose_receive_address,
                null,false);
        binding.setVariable(BR.dialogUtils,this);
        binding.rcvChooseAddress.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.divider_line_1dp_grayf1f1f1));
        binding.rcvChooseAddress.addItemDecoration(itemDecoration);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels;
        window.getAttributes().height = context.getResources().getDisplayMetrics().heightPixels/2;
        if(addressEntities!=null)
            adapter.getItems().addAll(addressEntities);
        dialog.show();
    }

    public void dismiss(){
        if(dialog!=null)
            dialog.dismiss();
        dialog = null;
    }

    public void onCloseClick(View view){
        dismiss();
    }

    public void onAddAddressClick(View view){
        //跳转到添加地址页面
        view.getContext().startActivity(new Intent(view.getContext(), AddAddressActivity.class));
    }

    //刷新数据
    public void updateData(BaseDialogImpl baseDialog) {
        HttpUtils.getInstance().getMyAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<List<AddressEntity>>() {
                        }.getType();
                        List<AddressEntity> addressEntities = new Gson().fromJson(t,type);
                        adapter.getItems().clear();
                        adapter.getItems().addAll(addressEntities);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        //加载失败，就销毁当前的对话框
                        dismiss();
                    }
                });
    }

    /**
     * 这是点击回掉事件
     */
    public interface OnItemClickListener{
        public void onItemClick(AddressEntity entity);
    }
}
