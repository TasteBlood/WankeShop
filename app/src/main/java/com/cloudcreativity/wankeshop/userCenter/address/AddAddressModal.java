package com.cloudcreativity.wankeshop.userCenter.address;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.ActivityAddAddressBinding;
import com.cloudcreativity.wankeshop.entity.address.ProvinceEntity;
import com.cloudcreativity.wankeshop.userCenter.AddressManageActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddAddressModal {
    private AddAddressActivity context;
    private ActivityAddAddressBinding binding;

     AddAddressModal(AddAddressActivity context, ActivityAddAddressBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void onBack(View view){
        context.finish();
    }
    //打开通讯录
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openContact(View view){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            context.startActivityForResult(intent,context.REQUEST_CONTACT);
        }else{
            context.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},context.REQUEST_CONTACT);
        }

    }
    //设置当前的联系人方式
    public void setContact(String name,String mobile){
         binding.etAddAddressName.setText(name);
         binding.etAddAddressPhone.setText(mobile);
    }

    //跳转到选择地址页面
    public void skipChooseAddress(View view){
         //先请求省列表
        HttpUtils.getInstance().getProvinces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(context,true) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<List<ProvinceEntity>>() {
                        }.getType();
                        List<ProvinceEntity> provinceEntities = new Gson().fromJson(t,type);
                        if(provinceEntities==null||provinceEntities.isEmpty()){
                            ToastUtils.showShortToast(context, R.string.str_no_data);
                        }else{
                            TempAddress.provinceEntities = provinceEntities;
                            context.startActivity(new Intent().setClass(context,AddressChooseActivity.class));
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

    }

    //设置选择的地址
    public void setAddress(String address){
         binding.tvAddAddressAddress.setText(address);
    }

    //保存地址
    public void onSave(View view){
        String name = binding.etAddAddressName.getText().toString().trim();
        String phone = binding.etAddAddressPhone.getText().toString().trim();
        String address = binding.tvAddAddressAddress.getText().toString().trim();
        String extra_address = binding.etAddAddressAddress.getText().toString().trim();
        boolean checked = binding.cbAddAddressIsDefault.isChecked();
        String postCode = binding.etAddAddressPostcode.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            ToastUtils.showShortToast(context,R.string.str_receipt_name_not_null);
            return;
        }
        if(TextUtils.isEmpty(phone)){
            ToastUtils.showShortToast(context,R.string.str_receipt_phone_not_null);
            return;
        }
        if(TextUtils.isEmpty(address)){
            ToastUtils.showShortToast(context,R.string.str_receipt_address_not_null);
            return;
        }
        if(TextUtils.isEmpty(extra_address)){
            ToastUtils.showShortToast(context,R.string.str_receipt_extra_address_not_null);
            return;
        }
        int provinceId = TempAddress.provinceEntity.getId();
        int cityId = TempAddress.cityEntity.getId();
        int areaId = TempAddress.areaEntity.getId();
        int streetId = 0;
        if(TempAddress.streetEntity!=null)
            streetId = TempAddress.streetEntity.getId();

        //保存数据
        save(provinceId,cityId,areaId,streetId,extra_address,postCode,name,phone,checked?1:0);

    }

    private void save(int provinceId,int cityId,int areaId,int streetId,
                      String addressInfo,String postCode,String receiptName,String receiptMobile,
                      int isDefault){
        HttpUtils.getInstance().addAddress(SPUtils.get().getUid(),SPUtils.get().getToken(),
                provinceId,cityId,areaId,streetId,addressInfo,postCode,receiptName,receiptMobile,isDefault)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(context,true) {
                    @Override
                    public void onSuccess(String t) {
                        ToastUtils.showShortToast(context,R.string.str_add_success);
                        EventBus.getDefault().post(AddressManageActivity.MSG_REFRESH_DATA);
                        context.finish();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

}
