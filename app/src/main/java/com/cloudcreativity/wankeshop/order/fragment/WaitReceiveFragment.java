package com.cloudcreativity.wankeshop.order.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentWaitReceiveBinding;
import com.cloudcreativity.wankeshop.order.OrderDetailViewModal;

import org.greenrobot.eventbus.Subscribe;

/**
 * 待收货Fragment
 */
public class WaitReceiveFragment extends LazyFragment {


    private FragmentWaitReceiveBinding binding;
    private WaitReceiveViewModal modal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(String msg){
        if(OrderDetailViewModal.MSG_PAY_SUCCESS.equals(msg)){
            binding.refreshWaitReceive.startRefresh();
        }else if(OrderDetailViewModal.MSG_RECEIVE_ORDER.equals(msg)){
            binding.refreshWaitReceive.startRefresh();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wait_receive,container,false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.shape_list_item_10dp_tranparent));
        binding.rcvWaitReceive.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.rcvWaitReceive.addItemDecoration(itemDecoration);
        binding.setReceiveModal(modal = new WaitReceiveViewModal(context,this,binding));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
        binding.refreshWaitReceive.startRefresh();
    }
}
