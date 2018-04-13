package com.cloudcreativity.wankeshop.collect;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityShopCollectBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

/**
 * 商铺收藏
 */
public class ShopCollectActivity extends BaseActivity {

    public static final String MSG_REMOVE_COLLECT_ITEM = "msg_remove_collect_item";
    public static final String MSG_REFRESH_COLLECT = "msg_refresh_collect_item";

    private ActivityShopCollectBinding binding;
    private ShopCollectModal collectModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_collect);
        binding.rcvCollectShop.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.shape_list_item_10dp_tranparent));
        binding.rcvCollectShop.addItemDecoration(decoration);
        binding.setShopCollectModal(collectModal = new ShopCollectModal(this,binding));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Map<String,Object> msg){
        if(msg==null)
            return;
        if(msg.containsKey("msg_collect")){
            if(MSG_REMOVE_COLLECT_ITEM.equals(msg.get("msg_collect").toString())){
                //说明有id
                collectModal.onRemoveItem(Integer.parseInt(msg.get("id").toString()));
            }else if(MSG_REFRESH_COLLECT.equals(msg.get("msg_collect").toString())){
                binding.refreshShopCollect.startRefresh();
            }
        }
    }
}
