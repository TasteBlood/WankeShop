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
import com.cloudcreativity.wankeshop.databinding.FragmentWaitPayBinding;
import com.cloudcreativity.wankeshop.order.OrderDetailViewModal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 待付款的Fragment
 */
public class WaitPayFragment extends LazyFragment {
    private FragmentWaitPayBinding binding;
    private WaitPayViewModal viewModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(String msg){
        if(OrderDetailViewModal.MSG_CANCEL_ORDER.equals(msg)){
            binding.refreshWaitPay.startRefresh();
        }else if(OrderDetailViewModal.MSG_PAY_SUCCESS.equals(msg)){
            binding.refreshWaitPay.startRefresh();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wait_pay,container,false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.shape_list_item_10dp_tranparent));
        binding.rcvWaitPay.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.rcvWaitPay.addItemDecoration(itemDecoration);
        binding.setWaitPayModal(new WaitPayViewModal(this,this,context,binding));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
        binding.refreshWaitPay.startRefresh();
    }
}
