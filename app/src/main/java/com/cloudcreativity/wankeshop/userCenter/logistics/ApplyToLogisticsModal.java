package com.cloudcreativity.wankeshop.userCenter.logistics;

import android.content.Intent;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityApplyToLogistics2Binding;
import com.cloudcreativity.wankeshop.entity.ApplyLogisticsEntity;
import com.cloudcreativity.wankeshop.entity.address.ProvinceEntity;
import com.cloudcreativity.wankeshop.userCenter.address.AddressChooseActivity;
import com.cloudcreativity.wankeshop.userCenter.address.TempAddress;
import com.cloudcreativity.wankeshop.utils.AppConfig;
import com.cloudcreativity.wankeshop.utils.CarCardDialogUtils;
import com.cloudcreativity.wankeshop.utils.CarTypeDialogUtils;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.LogUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.cloudcreativity.wankeshop.databinding.LayoutApplyStepOneViewstubBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutApplyStepTwoViewstubBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutApplyStepThreeViewstubBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是物流小二申请页面的ViewModal
 */
public class ApplyToLogisticsModal {
    private ApplyToLogistics2Activity context;
    private ActivityApplyToLogistics2Binding activityApplyToLogistics2Binding;

    /**
     * 用来控制页面显示
     */
    public static ObservableField<Boolean> one = new ObservableField<>();
    public static ObservableField<Boolean> two = new ObservableField<>();
    public static ObservableField<Boolean> three = new ObservableField<>();

    //这是最终提交的表单数据
    private static String provinceId;
    private static String cityId;
    private static String areaId;
    private static String realName;//真实姓名
    private static String IDCardNumber;//身份证号
    private static String emergName;//紧急联系人姓名
    private static String emergPhone;//紧急联系人电话号码
    private static String carCardColor;//车牌颜色
    private static String carCardNumber;//车牌号码
    private static String carType;//车辆类型
    private static String IDCardPath;//身份证照片路径
    private static String driverLicensePath;//驾驶证照片路径
    private static String driverLicense2path;//行驶证照片路径
    private static String carPhotoPath;//车辆45度照片路径
    private static String operationLicensePath;//营运证照片路径

     static int currentType = 1;//当前的上传照片是哪一个,默认是身份证

     final static int TYPE_IDCARD = 1;//身份证
     final static int TYPE_DRIVER = 2;//驾驶证
     final static int TYPE_DRIVER2 = 3;//行驶证
     final static int TYPE_CAR = 4;//车辆
     final static int TYPE_OPERATION = 5;//营运证


    //这是数据
    private static ApplyLogisticsEntity logisticsEntity;

    ApplyToLogisticsModal(ApplyToLogistics2Activity context, ActivityApplyToLogistics2Binding activityApplyToLogistics2Binding,ApplyLogisticsEntity entity) {
        this.context = context;
        this.activityApplyToLogistics2Binding = activityApplyToLogistics2Binding;
        logisticsEntity = entity;
        this.activityApplyToLogistics2Binding.stubApplyOne.getViewStub().inflate();
        one.set(true);
        two.set(false);
        three.set(false);
    }

    public void onBack(View view){
        context.finish();
    }

    /**
     * 清空静态变量
     */
    public void onDestroy(){
        one = null;
        two = null;
        three = null;
        provinceId = null;
        cityId = null;
        areaId = null;
        realName = null;
        IDCardNumber = null;
        emergName = null;
        emergPhone = null;
        carCardColor = null;
        carCardNumber = null;
        carType = null;
        IDCardPath = null;
        driverLicensePath = null;
        driverLicense2path = null;
        carPhotoPath = null;
        operationLicensePath = null;
        logisticsEntity = null;

        System.gc();
    }
    /**
     * 第一步
     */
    public static class StepOneModal{
        private ActivityApplyToLogistics2Binding binding;
        private LayoutApplyStepOneViewstubBinding thisBinding;
        private ApplyToLogistics2Activity context;

        StepOneModal(ActivityApplyToLogistics2Binding binding, LayoutApplyStepOneViewstubBinding thisBinding, ApplyToLogistics2Activity context) {
            this.binding = binding;
            this.thisBinding = thisBinding;
            this.context = context;
            if(logisticsEntity!=null){
                thisBinding.tvApplyStepOneCity.setText(logisticsEntity.getProvinceName()+logisticsEntity.getCityName()+logisticsEntity.getAreaName());
                thisBinding.tvApplyStepOneIDCard.setText(logisticsEntity.getIdCard());
                thisBinding.tvApplyStepOneName.setText(logisticsEntity.getName());
                thisBinding.tvApplyStepOneOtherName.setText(logisticsEntity.getEmerConName());
                thisBinding.tvApplyStepOneOtherPhone.setText(logisticsEntity.getEmerConMobile());
            }
        }

        public void onNextClick(View view){
            realName = thisBinding.tvApplyStepOneName.getText().toString().trim();
            IDCardNumber = thisBinding.tvApplyStepOneIDCard.getText().toString().trim();
            emergName = thisBinding.tvApplyStepOneOtherName.getText().toString().trim();
            emergPhone = thisBinding.tvApplyStepOneOtherPhone.getText().toString().trim();
            if(logisticsEntity==null&&TempAddress.provinceEntity==null){
                ToastUtils.showShortToast(context,"请选择所属区域");
                return;
            }
            if(logisticsEntity!=null){
                provinceId = String.valueOf(logisticsEntity.getProvinceId());
                cityId = String.valueOf(logisticsEntity.getCityId());
                areaId = String.valueOf(logisticsEntity.getAreaId());
            }else{
                provinceId = String.valueOf(TempAddress.provinceEntity.getId());
                cityId = String.valueOf(TempAddress.cityEntity.getId());
                areaId = String.valueOf(TempAddress.areaEntity.getId());
            }

            if(TextUtils.isEmpty(realName)){
                ToastUtils.showShortToast(context,"真实姓名不能为空");
                return;
            }
            if(TextUtils.isEmpty(realName)|| !StrUtils.isIDCard(IDCardNumber)){
                ToastUtils.showShortToast(context,R.string.str_idcard_format_error);
                return;
            }
            if(TextUtils.isEmpty(emergName)){
                ToastUtils.showShortToast(context,"紧急联系人姓名不能为空");
                return;
            }
            if(TextUtils.isEmpty(emergPhone)|| !StrUtils.isPhone(emergPhone)){
                ToastUtils.showShortToast(context,R.string.str_login_phone_format_error);
                return;
            }
            binding.stubApplyOne.getViewStub().setVisibility(View.GONE);
            binding.stubApplyTwo.getViewStub().inflate();
            two.set(true);
        }

        //选择城市的点击事件
        public void onChooseAddressClick(View view){
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

        //设置数据
        public void setAddress(String address){
            thisBinding.tvApplyStepOneCity.setText(address);
        }
    }

    /**
     * 第二步
     */
    public static class StepTwoModal{
        private ActivityApplyToLogistics2Binding binding;
        private LayoutApplyStepTwoViewstubBinding thisBinding;

        StepTwoModal(ActivityApplyToLogistics2Binding binding, LayoutApplyStepTwoViewstubBinding thisBinding) {
            this.binding = binding;
            this.thisBinding = thisBinding;

            //初始化信息
            if(logisticsEntity!=null){
                thisBinding.etApplyStepTwoCarCode.setText(logisticsEntity.getCarNum());
                //车牌颜色
                switch (logisticsEntity.getCarType()){
                    case CarCardDialogUtils.blue_card:
                        thisBinding.tvApplyStepTwoCarColor.setText("蓝牌");
                        break;
                    case CarCardDialogUtils.yellow_card:
                        thisBinding.tvApplyStepTwoCarColor.setText("黄牌");
                        break;
                    case CarCardDialogUtils.green_card:
                        thisBinding.tvApplyStepTwoCarColor.setText("绿牌");
                        break;
                    case CarCardDialogUtils.yellow_green_card:
                        thisBinding.tvApplyStepTwoCarColor.setText("黄绿牌");
                        break;
                }
                //车辆类型
                thisBinding.tvApplyStepTwoCarType.setText(CarTypeDialogUtils.typeNames[logisticsEntity.getCarType()]);
                carCardColor = String.valueOf(logisticsEntity.getLinceColor());
                carType = String.valueOf(logisticsEntity.getCarType());
            }
        }

        public void onNextClick(View view){
            if(TextUtils.isEmpty(carCardColor)){
                ToastUtils.showShortToast(view.getContext(),"请选择车牌颜色");
                return;
            }
            if(TextUtils.isEmpty(carType)){
                ToastUtils.showShortToast(view.getContext(),"请选择车辆类型");
                return;
            }
            carCardNumber = thisBinding.etApplyStepTwoCarCode.getText().toString().trim();
            if(TextUtils.isEmpty(carCardNumber)){
                ToastUtils.showShortToast(view.getContext(),"车牌号不能为空");
                return;
            }
            binding.stubApplyTwo.getViewStub().setVisibility(View.GONE);
            binding.stubApplyThree.getViewStub().inflate();
            three.set(true);
        }

        //车牌颜色点击
        public void onCarCardColorClick(View view){
            CarCardDialogUtils dialogUtils = new CarCardDialogUtils();
            dialogUtils.setOnCarCardClickListener(new CarCardDialogUtils.OnCarCardClickListener() {
                @Override
                public void onItemClick(int cardId, String colorName) {
                    thisBinding.tvApplyStepTwoCarColor.setText(colorName);
                    carCardColor = String.valueOf(cardId);
                }
            });
            dialogUtils.show(view.getContext());
        }
        //车辆类型点击
        public void onCarTypeClick(View view){
            CarTypeDialogUtils dialogUtils = new CarTypeDialogUtils();
            dialogUtils.setOnCarTypeClickListener(new CarTypeDialogUtils.OnCarTypeClickListener() {
                @Override
                public void onItemClick(int typeCode, String typeName) {
                    thisBinding.tvApplyStepTwoCarType.setText(typeName);
                    carType = String.valueOf(typeCode);
                }
            });
            dialogUtils.show(view.getContext());
        }
    }

    /**
     * 第三步
     */
    public static class StepThreeModal{

        private BaseDialogImpl dialog;
        private LayoutApplyStepThreeViewstubBinding thisBinding;

        StepThreeModal(BaseDialogImpl dialog, LayoutApplyStepThreeViewstubBinding thisBinding) {
            this.dialog = dialog;
            this.thisBinding = thisBinding;
            //初始化信息
            if(logisticsEntity!=null){
                //身份证正面照
                if(!TextUtils.isEmpty(logisticsEntity.getIdCardFront())){
                    GlideUtils.loadThumbs(thisBinding.getRoot().getContext(),logisticsEntity.getIdCardFront(),thisBinding.ivApplyStepThreeIDCard);
                    IDCardPath = logisticsEntity.getIdCardFront();
                }
                //驾驶证
                if(!TextUtils.isEmpty(logisticsEntity.getDriverLicenseFront())){
                    GlideUtils.loadThumbs(thisBinding.getRoot().getContext(),logisticsEntity.getDriverLicenseFront(),thisBinding.ivApplyStepThreeDriverLicense);
                    driverLicensePath = logisticsEntity.getDriverLicenseFront();
                }
                //行驶证
                if(!TextUtils.isEmpty(logisticsEntity.getSituationLicenseFront())){
                    GlideUtils.loadThumbs(thisBinding.getRoot().getContext(),logisticsEntity.getSituationLicenseFront(),thisBinding.ivApplyStepThreeDriverLicense2);
                    driverLicense2path = logisticsEntity.getSituationLicenseFront();
                }
                //车辆45°照片
                if(!TextUtils.isEmpty(logisticsEntity.getCarPhoto1())){
                    GlideUtils.loadThumbs(thisBinding.getRoot().getContext(),logisticsEntity.getCarPhoto1(),thisBinding.ivApplyStepThreeCarPhoto);
                    carPhotoPath = logisticsEntity.getCarPhoto1();
                }
                //营运证照片
                if(!TextUtils.isEmpty(logisticsEntity.getOperateLicenseFront())){
                    GlideUtils.loadThumbs(thisBinding.getRoot().getContext(),logisticsEntity.getOperateLicenseFront(),thisBinding.ivApplyStepThreeOperationLicense);
                    operationLicensePath = logisticsEntity.getOperateLicenseFront();
                }
            }
        }

        public void onSaveClick(View view){
            if(TextUtils.isEmpty(IDCardPath)){
                ToastUtils.showShortToast(view.getContext(),"请上传身份证正面照");
                return;
            }
            if(TextUtils.isEmpty(carPhotoPath)){
                ToastUtils.showShortToast(view.getContext(),"请上传车辆45度照片");
                return;
            }
            //提交信息
            HttpUtils.getInstance().applyToLogistics(
                    realName,SPUtils.get().getUser().getMobile(),provinceId,cityId,areaId,emergName,emergPhone,carCardColor,carType,carCardNumber,
                    IDCardNumber,IDCardPath,driverLicensePath,driverLicense2path,operationLicensePath,carPhotoPath)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DefaultObserver<String>(dialog,true) {
                @Override
                public void onSuccess(String t) {
                    //提交成功

                }

                @Override
                public void onFail(ExceptionReason msg) {
                    ToastUtils.showShortToast(thisBinding.getRoot().getContext(),"提交失败，请重试");
                }
            });
        }

        public void onSelectPhotoClick(View view){
            switch (view.getId()){
                case R.id.iv_applyStepThree_IDCard:
                    currentType = TYPE_IDCARD;
                    break;
                case R.id.iv_applyStepThree_DriverLicense:
                    currentType = TYPE_DRIVER;
                    break;
                case R.id.iv_applyStepThree_DriverLicense2:
                    currentType = TYPE_DRIVER2;
                    break;
                case R.id.iv_applyStepThree_carPhoto:
                    currentType = TYPE_CAR;
                    break;
                case R.id.iv_applyStepThree_OperationLicense:
                    currentType = TYPE_OPERATION;
                    break;
            }
            dialog.openPictureDialog(false);
        }

        //上传图片
        public void uploadImage(final String url){
            dialog.showProgress("上传中");
            HttpUtils.getInstance().getQiNiuToken()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(dialog,false) {
                        @Override
                        public void onSuccess(String t) {
                            final Configuration config = new Configuration.Builder()
                                    .chunkSize(1024 * 1024)        // 分片上传时，每片的大小。 默认256K
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
                                            String path = key.substring(0,key.length());
                                            //刷新页面,不用，因为图片的域名需要后台拼接
                                            //binding.invalidateAll();
                                            switch (currentType){
                                                case TYPE_IDCARD:
                                                    IDCardPath = path;
                                                    displayImage(url,thisBinding.ivApplyStepThreeIDCard);
                                                    break;
                                                case TYPE_DRIVER:
                                                    driverLicensePath = path;
                                                    displayImage(url,thisBinding.ivApplyStepThreeDriverLicense);
                                                    break;
                                                case TYPE_DRIVER2:
                                                    driverLicense2path = path;
                                                    displayImage(url,thisBinding.ivApplyStepThreeDriverLicense2);
                                                    break;
                                                case TYPE_CAR:
                                                    carPhotoPath = path;
                                                    displayImage(url,thisBinding.ivApplyStepThreeCarPhoto);
                                                    break;
                                                case TYPE_OPERATION:
                                                    operationLicensePath = path;
                                                    displayImage(url,thisBinding.ivApplyStepThreeOperationLicense);
                                                    break;
                                            }
                                        }
                                    },null);
                            dialog.dismissProgress();
                        }
                        @Override
                        public void onFail(ExceptionReason msg) {
                            ToastUtils.showShortToast(thisBinding.getRoot().getContext(),"上传失败");
                        }
                    });
        }
    }
    //显示图片
    private static void displayImage(String path, ImageView imageView){
        GlideUtils.loadFileThumbs(imageView.getContext(),new File(path),imageView);
    }
}
