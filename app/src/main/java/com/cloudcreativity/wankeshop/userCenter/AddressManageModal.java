package com.cloudcreativity.wankeshop.userCenter;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.RadioGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.ActivityAddressManageBinding;
import com.cloudcreativity.wankeshop.entity.AddressEntity;
import com.cloudcreativity.wankeshop.userCenter.address.AddAddressActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是地址列表的ViewModal
 */
public class AddressManageModal {

    private AddressManageActivity context;
    private ActivityAddressManageBinding addressManageBinding;
    public AddressAdapter addressAdapter;

    AddressManageModal(AddressManageActivity context, ActivityAddressManageBinding addressManageBinding) {
        this.context = context;
        this.addressManageBinding = addressManageBinding;
        addressAdapter = new AddressAdapter(context,context);
    }

    /**
     * 页面回退
     */
    public void onBack(View view){
        context.finish();
    }

    /**
     * 添加地址
     */
    public void onAddAddressClick(View view){
        context.startActivity(new Intent().setClass(context, AddAddressActivity.class));
    }

    /**
     * 这是RefreshLayout的监听
     */
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initialData();
        }
    };

    //初始化加载数据
    private void initialData(){
        HttpUtils.getInstance().getMyAddress(SPUtils.get().getUid(),SPUtils.get().getToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(context,false) {
                    @Override
                    public void onSuccess(String t) {
                        addressManageBinding.srlAddressManage.setRefreshing(false);
                        Type type = new TypeToken<List<AddressEntity>>() {
                        }.getType();
                        List<AddressEntity> addressEntities = new Gson().fromJson(t,type);
                        //在这里判断数据
                        if(addressEntities==null||addressEntities.isEmpty()){
                            //暂无数据
                            ToastUtils.showShortToast(context,R.string.str_no_data);
                            addressAdapter.getItems().clear();
                        }else{
                            //刷新界面
                            addressAdapter.getItems().clear();
                            addressAdapter.getItems().addAll(addressEntities);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        addressManageBinding.srlAddressManage.setRefreshing(false);
                    }
                });
    }

    //加载数据
    public void refreshData(){
        addressManageBinding.srlAddressManage.post(new Runnable() {
            @Override
            public void run() {
                addressManageBinding.srlAddressManage.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
    }

}
