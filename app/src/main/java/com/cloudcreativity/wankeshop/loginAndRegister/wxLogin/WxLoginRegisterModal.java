package com.cloudcreativity.wankeshop.loginAndRegister.wxLogin;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityWxLoginRegisterBinding;
import com.cloudcreativity.wankeshop.entity.address.ProvinceEntity;
import com.cloudcreativity.wankeshop.userCenter.address.AddressChooseActivity;
import com.cloudcreativity.wankeshop.userCenter.address.TempAddress;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 微信登录注册ViewModal
 */
public class WxLoginRegisterModal {
    private WxLoginRegisterActivity context;
    private BaseDialogImpl baseDialog;
    private ActivityWxLoginRegisterBinding binding;
    private String mobile;
    private String userName;
    private String avatar;
    private String openId;
    private String password;

    WxLoginRegisterModal(WxLoginRegisterActivity context,
                                ActivityWxLoginRegisterBinding binding,
                                String mobile, String userName, String avatar, String openId) {
        this.context = context;
        this.baseDialog = context;
        this.binding = binding;
        this.mobile = mobile;
        this.userName = userName;
        this.avatar = avatar;
        this.openId = openId;
    }

    public void onBack(View view){
        context.finish();
    }

    //选择地址点击
    public void onChooseAddressClick(View view){
        skipChooseAddress();
    }

    //保存信息
    public void onSaveClick(View view){
        password = this.binding.etBindRegisterPwd.getText().toString().trim();
        if(TextUtils.isEmpty(password)||password.length()<6){
            ToastUtils.showShortToast(context,R.string.str_login_password_error);
            return;
        }
        String provinceId = null;
        String cityId = null;
        String areaId = null;
        if(TempAddress.provinceEntity==null){
            ToastUtils.showShortToast(context,R.string.str_choose_location_area);
            return;
        }
        provinceId = String.valueOf(TempAddress.provinceEntity.getId());
        cityId = String.valueOf(TempAddress.cityEntity.getId());
        areaId = String.valueOf(TempAddress.areaEntity.getId());

        HttpUtils.getInstance().saveUserForWx(mobile,password,provinceId,cityId,areaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        //先注册保存，然后进行与微信绑定
                        bindWxAndUser(mobile,openId);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

    }

    //地址信息的回掉
    public void onAddressSuccess(String address){
        this.binding.tvBindRegisterAddress.setText(address);
    }

    //跳转到选择地址页面
    private void skipChooseAddress(){
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
                        ToastUtils.showShortToast(context,"获取地区数据失败");
                    }
                });

    }

    //将账号和微信openId进行绑定
    private void bindWxAndUser(final String mobile, String openId){
        HttpUtils.getInstance().bindWxUser(openId,mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        //绑定成功，进行登录操作
                        Map<String,Object> map = new HashMap<>();
                        map.put("type","wx_login");
                        map.put("mobile",mobile);
                        map.put("password",password);
                        EventBus.getDefault().post(map);
                        context.finish();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
}
