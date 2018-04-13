package com.cloudcreativity.wankeshop.goods;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityDetailBinding;
import com.cloudcreativity.wankeshop.utils.ToastUtils;

/**
 * 商品详情
 */
public class GoodsDetailActivity extends BaseActivity {

    private GoodsDetailViewModal goodsDetailViewModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.tabGoodsDetail.setupWithViewPager(binding.vpGoodsDetail);
        binding.setDetailModal(goodsDetailViewModal = new GoodsDetailViewModal(this,binding,getIntent().getIntExtra("spuId",0)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(Manifest.permission.CALL_PHONE.equals(permissions[0])&& PackageManager.PERMISSION_GRANTED==grantResults[0]){
                //拨打电话
                goodsDetailViewModal.call();
            }else{
                ToastUtils.showShortToast(this,"手机已禁止拨打电话，请在设置中打开权限");
            }
        }
    }
}
