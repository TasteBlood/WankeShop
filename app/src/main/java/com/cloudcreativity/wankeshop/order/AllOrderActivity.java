package com.cloudcreativity.wankeshop.order;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityAllOrderBinding;
import com.cloudcreativity.wankeshop.main.MineFragment;
import com.cloudcreativity.wankeshop.main.MineFragmentModal;
import com.cloudcreativity.wankeshop.utils.ToastUtils;

/**
 * 全部订单页面
 */
public class AllOrderActivity extends BaseActivity {

    private AllOrderViewModal allModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAllOrderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_all_order);
        binding.tabAllOrder.setupWithViewPager(binding.vpAllOrder);
        binding.vpAllOrder.setOffscreenPageLimit(5);
        allModal = new AllOrderViewModal(this, binding);
        binding.setAllModal(allModal);
        String state = getIntent().getStringExtra("state");
        if(MineFragmentModal.STATE_WAIT_RECEIVE.equals(state)){
            //选中待收货订单
           binding.getRoot().postDelayed(new Runnable() {
               @Override
               public void run() {
                   binding.vpAllOrder.setCurrentItem(1);
               }
           },200);
        }else if(MineFragmentModal.STATE_RETURN.equals(state)){
            //选中退换货订单
            binding.getRoot().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.vpAllOrder.setCurrentItem(4);
                }
            },200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(Manifest.permission.CALL_PHONE.equals(permissions[0])&& PackageManager.PERMISSION_GRANTED==grantResults[0]){
                //同意了,联系卖家只有在待收货 和 退货中存在
                allModal.getCurrentFragment().onRequestPermissionsResult(requestCode,permissions,grantResults);

            }else{
                ToastUtils.showShortToast(this,"系统权限禁止，请在设置中打开权限");
            }
        }
    }
}
