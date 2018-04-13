package com.cloudcreativity.wankeshop.shop;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.base.GridDividerItemDecoration;
import com.cloudcreativity.wankeshop.databinding.ActivityShopGoodsListBinding;
import com.cloudcreativity.wankeshop.entity.ShopEntity;
/**
 * 商铺商品列表
 */
public class ShopGoodsListActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShopEntity entity = (ShopEntity) getIntent().getSerializableExtra("shop");
        ActivityShopGoodsListBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_shop_goods_list);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.rcvShopGoods.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        binding.rcvShopGoods.addItemDecoration(new GridDividerItemDecoration(15,getResources().getColor(R.color.transparent)));
        binding.setShopListModal(new ShopGoodsListViewModal(this,this,binding,entity));
    }
}
