package com.cloudcreativity.wankeshop.main;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.base.CommonWebActivity;
import com.cloudcreativity.wankeshop.collect.GoodsCollectActivity;
import com.cloudcreativity.wankeshop.collect.ShopCollectActivity;
import com.cloudcreativity.wankeshop.databinding.FragmentMineBinding;
import com.cloudcreativity.wankeshop.entity.ApplyLogisticsEntity;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.money.MoneyRecordsActivity;
import com.cloudcreativity.wankeshop.order.AllOrderActivity;
import com.cloudcreativity.wankeshop.userCenter.AddressManageActivity;
import com.cloudcreativity.wankeshop.userCenter.SettingActivity;
import com.cloudcreativity.wankeshop.userCenter.UserInformationActivity;
import com.cloudcreativity.wankeshop.userCenter.logistics.ApplyToLogistics2Activity;
import com.cloudcreativity.wankeshop.utils.ScanResultActivity;
import com.cloudcreativity.wankeshop.utils.AppConfig;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.PayWayDialogUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是MineFragment的ViewModal
 */
public class MineFragmentModal {

    private Activity context;
    private FragmentMineBinding binding;
    private BaseDialogImpl baseDialog;
    //这是用户数据
    public UserEntity user ;

    public static final int REQUEST_SCAN = 0x08;//这是扫码的请求
    public static final int RESULT_OK = 0xA1;//这是扫描后的结果

    //这是状态
    public static final String STATE_ALL = "state_all";
    public static final String STATE_WAIT_RECEIVE = "state_wait_receive";
    public static final String STATE_RETURN = "state_wait_return";

    MineFragmentModal(Activity context, FragmentMineBinding binding, BaseDialogImpl baseDialog) {
        this.context = context;
        this.binding = binding;
        this.baseDialog = baseDialog;
        user = SPUtils.get().getUser();
        binding.refreshMine.setOnRefreshListener(refreshListenerAdapter);
        switch (user.getType()){
            case AppConfig.USER_TYPE_TWO:
                binding.ivUserType.setImageResource(R.mipmap.ic_sorter);
                break;
                case AppConfig.USER_TYPE_THREE:
                    binding.ivUserType.setImageResource(R.mipmap.ic_logisticssmall2);
                    break;
                    case AppConfig.USER_TYPE_FOUR:
                        binding.ivUserType.setImageResource(R.mipmap.ic_stationagent);
                        break;
        }
    }

    //刷新事件
    private RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
           refreshLayout.postDelayed(new Runnable() {
               @Override
               public void run() {
                   refreshLayout.finishRefreshing();
               }
           },3000);
        }
    };


    @BindingAdapter("imageUrl")
    public static void showAvatar(ImageView imageView,String url){
        if(TextUtils.isEmpty(url)){
            GlideUtils.loadCircle(imageView.getContext(),R.mipmap.ic_default_head,imageView);
        }else{
            GlideUtils.loadCircle(imageView.getContext(),url,imageView);
        }
    }

    //personal center
    public void onUserInfoClick(View view){
        context.startActivity(new Intent().setClass(context, UserInformationActivity.class));
    }

    //scan click
    public void onScanClick(View view){
        context.startActivityForResult(new Intent().setClass(context, CaptureActivity.class),REQUEST_SCAN);
    }

    //setting click
    public void onSettingClick(View view){
        context.startActivity(new Intent().setClass(context, SettingActivity.class));
    }

    //all order click
    public void onAllOrderClick(View view){
        Intent intent = new Intent(context, AllOrderActivity.class);
        intent.putExtra("state",STATE_ALL);
        context.startActivity(intent);
    }

    //wait receive click
    public void onWaitReceiveClick(View view){
        Intent intent = new Intent(context, AllOrderActivity.class);
        intent.putExtra("state",STATE_WAIT_RECEIVE);
        context.startActivity(intent);
    }

    //return click
    public void onReturnClick(View view){
        Intent intent = new Intent(context, AllOrderActivity.class);
        intent.putExtra("state",STATE_RETURN);
        context.startActivity(intent);
    }

    //address click
    public void onAddressClick(View view){
        context.startActivity(new Intent().setClass(context, AddressManageActivity.class));
    }

    //recharge click
    public void onRechargeClick(View view){
//        new PayWayDialogUtils().show(context);
//        new CarCardDialogUtils().show(context);
        CommonWebActivity.startActivity(context,"百度一下","http://www.baidu.com");
    }

    //money records
    public void onMoneyClick(View view){
        context.startActivity(new Intent().setClass(context, MoneyRecordsActivity.class));
    }

    //goods collect click
    public void onGoodsCollectClick(View view){
        context.startActivity(new Intent().setClass(context, GoodsCollectActivity.class));
    }

    //shop collect click
    public void onShopCollectClick(View view){
        context.startActivity(new Intent().setClass(context, ShopCollectActivity.class));
    }

    //apply logistics click
    public void onApplyLogisticsCLick(View view){
        //先获取已经申请过的物流小二的信息
        HttpUtils.getInstance().getApplyToLogisticsInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        Intent intent = new Intent(context,ApplyToLogistics2Activity.class);
                        intent.putExtra("logisticsEntity",new Gson().fromJson(t, ApplyLogisticsEntity.class));
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        ToastUtils.showShortToast(context,"数据加载失败，请重试");
                    }
                });
    }

    //deal scan code result
    public void dealScanCode(String code){
        //目前是测试，全部到result界面
        Intent intent = new Intent(context, ScanResultActivity.class);
        intent.putExtra("result",code);
        context.startActivity(intent);
    }

}
