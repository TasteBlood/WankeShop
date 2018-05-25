package com.cloudcreativity.wankeshop.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentGoodsIndexBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

/**
 * 这是商品fragment
 * 因为考虑到，如果页面是从购物车中进入的，那么就得显示当前购物车中的状态
 */
public class GoodsIndexFragment extends LazyFragment {

    private GoodsIndexViewModal viewModal;
    private int spuId;

    //添加地址之后刷新页面
    public static final String MSG_REFRESH_ADDRESS = "msg_refresh_address";

    public synchronized static GoodsIndexFragment getInstance(GoodsEntity entity){
        GoodsIndexFragment fragment = new GoodsIndexFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity",entity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册eventBus监听事件
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentGoodsIndexBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods_index,container,false);
        binding.setIndexModal(viewModal = new GoodsIndexViewModal(this,context,(GoodsEntity) getArguments().getParcelable("entity"),binding));
        binding.rcvGoodsIndexSize.setLayoutManager(new GridLayoutManager(context,2,LinearLayoutManager.VERTICAL,false));
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initialLoadData() {

    }

    @Subscribe
    public void onEvent(String msg){
        if(MSG_REFRESH_ADDRESS.equals(msg)){
            viewModal.refreshAddress();
        }
    }

    //添加到购物车
    public Map<String,Object> onAddShopCar(){
        return viewModal.onAddShopCar();
    }
}
