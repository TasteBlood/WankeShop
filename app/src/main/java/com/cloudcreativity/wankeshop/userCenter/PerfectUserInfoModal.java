package com.cloudcreativity.wankeshop.userCenter;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bigkoo.pickerview.TimePickerView;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.CommonWebActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityPerfectUserInfoBinding;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.entity.address.ProvinceEntity;
import com.cloudcreativity.wankeshop.receiver.MyBusinessReceiver;
import com.cloudcreativity.wankeshop.userCenter.address.AddressChooseActivity;
import com.cloudcreativity.wankeshop.userCenter.address.TempAddress;
import com.cloudcreativity.wankeshop.utils.AppConfig;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.LogUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * 这是完善信息的ViewModal
 */
public class PerfectUserInfoModal {

    private PerfectUserInfoActivity context;
    private ActivityPerfectUserInfoBinding binding;

    //这是用户数据
    public UserEntity user;

    PerfectUserInfoModal(PerfectUserInfoActivity context,ActivityPerfectUserInfoBinding binding) {
        this.context = context;
        this.binding = binding;
        user = SPUtils.get().getUser();
    }

    public void onBack(View view){
        context.finish();
    }

    //上传照片
    public void selectPhoto(View view){
        context.openPictureDialog(true);
    }

    //生日选择
    public void selectBirth(View view){
        Calendar calendar = Calendar.getInstance();
        Calendar calendarEnd = (Calendar) calendar.clone();
        calendar.set(Calendar.YEAR,1900);
        final TimePickerView pickerView = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = getTime(date);
                user.setBirthDay(time);
                binding.tvPerfectBirth.setText(time);
            }
        }).setType(new boolean[]{true,true,true,false,false,false})
                .setCancelText("取消")
                .setRangDate(calendar,calendarEnd)
                .setDate(calendarEnd)
                .setCancelColor(context.getResources().getColor(R.color.gray_717171))
                .setSubmitText("确定")
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setOutSideCancelable(true)
                .isCenterLabel(true)
                .build();
        pickerView.show();
    }

    //性别选择
    public RadioGroup.OnCheckedChangeListener sexChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_ganderFemale:
                    user.setSex(1);
                    break;
                case R.id.rb_genderMale:
                    user.setSex(0);
                    break;
            }
        }
    };

    //保存操作
    public void onSave(View view){
        String IDCard = binding.etPerfectIDCard.getText().toString().trim();
        if(!TextUtils.isEmpty(IDCard)&&!StrUtils.isIDCard(IDCard)){
            ToastUtils.showShortToast(context,R.string.str_idcard_format_error);
            return;
        }
        String email = binding.etPerfectMail.getText().toString().trim();
        if(!TextUtils.isEmpty(email)&&!StrUtils.isEmail(email)){
            ToastUtils.showShortToast(context,R.string.str_email_format_error);
            return;
        }
        String realName = binding.etPerfectRealName.getText().toString().trim();
        String userName = binding.etPerfectUsername.getText().toString().trim();
        String shopName = binding.etPerfectShopName.getText().toString().trim();
        user.setUserName(userName);
        user.setEmail(email);
        user.setRealName(realName);
        user.setIdCard(IDCard);
        user.setStoreName(shopName);
        submit(user);
    }

    //提交数据
    private void submit(UserEntity entity){
        //处理头像
        String headPic = entity.getHeadPic();
        if(!TextUtils.isEmpty(headPic)&&headPic.startsWith("http"))
            headPic = "";

        HttpUtils.getInstance().editInformation(
                entity.getUserName(),entity.getRealName(),entity.getPassword(),
                headPic,entity.getEmail(),entity.getSex(),entity.getIdCard(),entity.getBirthDay(),
                entity.getType(),entity.getProvinceId(),entity.getCityId(),entity.getAreaId(),entity.getStoreName())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(context,true) {
                    @Override
                    public void onSuccess(String t) {
                        //更新本地的用户信息
                        UserEntity userEntity = new Gson().fromJson(t, UserEntity.class);
                        if(!SPUtils.get().getUser().getAreaId().equals(userEntity.getAreaId())){
                            //发现当前当前的地址发生变化，提示重新登录
                            ToastUtils.showShortToast(context,"所在区域发生变化，需重新登录");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.setAction(MyBusinessReceiver.ACTION_RE_LOGIN);
                                    context.sendBroadcast(intent);
                                }
                            },500);
                        }else{
                                SPUtils.get().putInt(SPUtils.Config.UID,userEntity.getId());
                                SPUtils.get().putString(SPUtils.Config.TOKEN,userEntity.getToken());
                                SPUtils.get().setUser(t);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    context.finish();
                                }
                            },200);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //获取时间
    private String getTime(Date date){
        DateFormat format = SimpleDateFormat.getDateInstance(2,Locale.CHINA);
        return format.format(date);
    }

    //上传图片
    public void uploadImage(final String url){
        context.showProgress("上传中");
        HttpUtils.getInstance().getQiNiuToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(context,false) {
                    @Override
                    public void onSuccess(String t) {
                        LogUtils.e("xuxiwu",t);
                        final Configuration config = new Configuration.Builder()
                                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                                .connectTimeout(10)           // 链接超时。默认10秒
                                .useHttps(true)               // 是否使用https上传域名
                                .responseTimeout(60)          // 服务器响应超时。默认60秒
                                //.recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
                                //.recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                .build();

                        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
                        UploadManager uploadManager = new UploadManager(config);
                        uploadManager.put(new File(url),
                                String.format(AppConfig.FILE_NAME,System.currentTimeMillis(),"jpg"),
                                t,
                                new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                context.dismissProgress();
                                user.setHeadPic(key.substring(0,key.length()));
                                //刷新页面,不用，因为后台的图片地址不确定
                                //binding.invalidateAll();
                                GlideUtils.loadCircle(context,new File(url),binding.ivPerfectHead);
                            }
                        },null);
                    }
                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //跳转到修改手机号
    public void toModifyMobile(View view){
        context.startActivity(new Intent().setClass(context,ModifyMobileActivity.class));
    }

    //获取地址
    public void setAddress(String address){
        //设置地址
        this.binding.tvPerfectAddress.setText(address);
        user.setProvinceId(String.valueOf(TempAddress.provinceEntity.getId()));
        user.setCityId(String.valueOf(TempAddress.cityEntity.getId()));
        user.setAreaId(String.valueOf(TempAddress.areaEntity.getId()));
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
                        ToastUtils.showShortToast(context,"获取地区数据失败");
                    }
                });

    }

    //跳转到绑定微信页面
    public void bindWechat(View view){
        CommonWebActivity.startActivity(context,"关注公众号","file:///android_asset/contact_public_code.html");
    }

    /**
     * 这是微信授权的回掉
     * @param avatar 头像
     * @param userName 昵称
     * @param openId openId
     */
    public void onWeChatCallback(String avatar,String userName,String openId){
        Intent intent = new Intent(context,BindWeChatActivity.class);
        intent.putExtra("avatar",avatar);
        intent.putExtra("userName",userName);
        intent.putExtra("openId",openId);
        context.startActivity(intent);
    }
}
