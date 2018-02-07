package com.cloudcreativity.wankeshop.userCenter.address;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityAddAddressBinding;

/**
 * 添加收货地址
 */
public class AddAddressActivity extends BaseActivity {
    /**
     * 获取联系人的
     */
    public int REQUEST_CONTACT = 10086;
    private AddAddressModal addAddressModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddAddressBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address);
        addAddressModal = new AddAddressModal(this,binding);
        binding.setAddressModal(addAddressModal);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED)
            return;
        if(requestCode==REQUEST_CONTACT){
            //获取联系人的信息
            Uri data1 = data.getData();
            if(data1!=null){
                try {
                    Cursor cursor = managedQuery(data1, null, null, null, null);
                    cursor.moveToFirst();
                    // 获得DATA表中的名字
                    String username = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // 条件为联系人ID
                    String contactId = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
                    Cursor phone = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                    + contactId, null, null);
                    String userNumber = "";
                    while (phone.moveToNext()) {
                        userNumber = phone
                                .getString(phone
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phone.close();
                    addAddressModal.setContact(username, TextUtils.isEmpty(userNumber)?"":userNumber.replace(" ",""));
                }catch (Exception e){
                   e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CONTACT){
            if(permissions[0].equals(Manifest.permission.READ_CONTACTS)&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                addAddressModal.openContact(null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //清空临时保存的省市数据
        TempAddress.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //在这里判断当前的地址信息是否存在
        StringBuffer buffer = new StringBuffer();
        if(TempAddress.provinceEntity!=null)
            buffer.append(TempAddress.provinceEntity.getName());
        if(TempAddress.cityEntity!=null)
            buffer.append(TempAddress.cityEntity.getName());
        if(TempAddress.areaEntity!=null)
            buffer.append(TempAddress.areaEntity.getName());
        if(TempAddress.streetEntity!=null)
            buffer.append(TempAddress.streetEntity.getName());

        addAddressModal.setAddress(buffer.toString());
    }


}
