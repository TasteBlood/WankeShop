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
import com.cloudcreativity.wankeshop.databinding.FragmentFinishBinding;
import com.cloudcreativity.wankeshop.order.OrderDetailViewModal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 已完成Fragment
 */
public class FinishFragment extends LazyFragment {

    private FragmentFinishBinding binding;
    private FinishViewModal modal;

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

        if(OrderDetailViewModal.MSG_DELETE_ORDER.equals(msg)){
            binding.refreshFinish.startRefresh();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finish,container,false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.shape_list_item_10dp_tranparent));
        binding.rcvFinish.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.rcvFinish.addItemDecoration(itemDecoration);
        binding.setFinishModal(modal = new FinishViewModal(context,this,binding));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
        binding.refreshFinish.startRefresh();
    }
}
