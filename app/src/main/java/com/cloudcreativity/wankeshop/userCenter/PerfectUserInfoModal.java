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
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.utils.APIService;
import com.cloudcreativity.wankeshop.utils.AppConfig;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.LogUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import com.cloudcreativity.wankeshop.databinding.ActivityPerfectUserInfoBinding;
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
        context.openPictureDialog();
    }

    //生日选择
    public void selectBirth(View view){
        final TimePickerView pickerView = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = getTime(date);
                user.setBirthDay(time);
                binding.tvPerfectBirth.setText(time);
            }
        }).setType(new boolean[]{true,true,true,false,false,false})
                .setCancelText("取消")
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
        user.setUserName(userName);
        user.setEmail(email);
        user.setRealName(realName);
        user.setIdCard(IDCard);
        submit(user);
    }

    @BindingAdapter("imageUrl")
    public static void showAvatar(ImageView imageView, String url){
        if(TextUtils.isEmpty(url)){
            GlideUtils.loadCircle(imageView.getContext(), R.mipmap.ic_default_head,imageView);
        }else{
            GlideUtils.loadCircle(imageView.getContext(),url,imageView);
        }

    }

    //提交数据
    private void submit(UserEntity entity){
        //处理头像
        String headPic = entity.getHeadPic();
        if(!TextUtils.isEmpty(headPic)&&headPic.startsWith("http"))
            headPic = "";
        HttpUtils.getInstance().editInformation(SPUtils.get().getUid(),SPUtils.get().getToken(),
                entity.getUserName(),entity.getRealName(),entity.getPassword(),
                headPic,entity.getEmail(),entity.getSex(),entity.getIdCard(),entity.getBirthDay(),
                AppConfig.USER_TYPE_ONE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(context,true) {
                    @Override
                    public void onSuccess(String t) {
                        //更新本地的用户信息
                        UserEntity userEntity = new Gson().fromJson(t, UserEntity.class);
                        if(userEntity!=null){
                            SPUtils.get().putInt(SPUtils.Config.UID,userEntity.getId());
                            SPUtils.get().putString(SPUtils.Config.TOKEN,userEntity.getToken());
                            SPUtils.get().setUser(t);
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                context.finish();
                            }
                        },200);
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
}
