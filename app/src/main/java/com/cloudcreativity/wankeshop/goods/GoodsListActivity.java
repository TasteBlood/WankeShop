package com.cloudcreativity.wankeshop.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.base.GridDecoration;
import com.cloudcreativity.wankeshop.databinding.ActivityGoodsListBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;

/**
 * 这是商品列表
 */
public class GoodsListActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGoodsListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_list);
        binding.rcvGoodsList.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        binding.rcvGoodsList.addItemDecoration(new GridDecoration(this,
                5,
                this.getResources().getColor(R.color.transparent)) {
            @Override
            public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
                boolean[] booleans = {false, false, false, false};
                    //因为给 RecyclerView 添加了 header，所以原本的 position 发生了变化
                    //position 为 0 的地方实际上是 header，真正的列表 position 从 1 开始
                    switch (itemPosition % 2) {
                        case 0:
                            //每一行第二个只显示左边距和下边距
                            booleans[0] = true;
                            booleans[3] = true;
                            break;
                        case 1:
                            //每一行第一个显示右边距和下边距
                            booleans[2] = true;
                            booleans[3] = true;
                            break;
                }
                return booleans;
            }
        });
        GoodsListViewModal modal;
        binding.setGoodsModal(modal = new GoodsListViewModal(this,
                getIntent().getStringExtra("title"),
                getIntent().getStringExtra("style"),
                getIntent().getStringExtra("type"),
                binding,
                getIntent().<GoodsEntity>getParcelableArrayListExtra("list")));
        binding.refreshGoodsList.setOnRefreshListener(modal.refreshListenerAdapter);
    }
}
