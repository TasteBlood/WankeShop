package com.cloudcreativity.wankeshop.order.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentWaitReceiveBinding;
import com.cloudcreativity.wankeshop.databinding.ItemOrderWaitReceiveBinding;
import com.cloudcreativity.wankeshop.entity.OrderEntity;
import com.cloudcreativity.wankeshop.entity.OrderEntityWrapper;
import com.cloudcreativity.wankeshop.order.OrderDetailActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 待收货ViewModal
 */
public class WaitReceiveViewModal {
    private Context context;
    private BaseDialogImpl baseDialog;
    private FragmentWaitReceiveBinding binding;

    public BaseBindingRecyclerViewAdapter<OrderEntity,ItemOrderWaitReceiveBinding> adapter;

    public ObservableField<Boolean> hasData = new ObservableField<>();

    private int pageNum = 1;

    WaitReceiveViewModal(Context context, BaseDialogImpl baseDialog, FragmentWaitReceiveBinding binding) {
        this.context = context;
        this.baseDialog = baseDialog;
        this.binding = binding;

        hasData.set(true);

        adapter = new BaseBindingRecyclerViewAdapter<OrderEntity, ItemOrderWaitReceiveBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_order_wait_receive;
            }

            @Override
            protected void onBindItem(ItemOrderWaitReceiveBinding binding, OrderEntity item, int position) {
                binding.setItem(item);
                initLayout(binding,item,position);
            }
        };
        this.binding.refreshWaitReceive.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(pageNum);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(pageNum);
            }
        });
    }

    //初始化item布局
    private void initLayout(ItemOrderWaitReceiveBinding binding,final OrderEntity item, int position) {
        binding.tvNumberAndPrice.setText(
                String.format(context.getString(R.string.str_number_unit_price),
                        item.getShoppingCart().getNum(),
                        Float.parseFloat(item.getPayMoney())));

        binding.tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactSeller(item);
            }
        });

        binding.tvEnterReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterReceive(item);
            }
        });

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDetails(item);
            }
        });
    }

    //订单详情
    private void orderDetails(OrderEntity item) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("order",item);
        context.startActivity(intent);
    }

    //确认收货
    private void enterReceive(final OrderEntity item) {
        HttpUtils.getInstance().updateOrderState(item.getId(),3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                       adapter.getItems().remove(item);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //联系卖家
    private void contactSeller(OrderEntity item) {

    }

    //加载数据
    //加载数据
    private void loadData(final int page){
        //加载的是大订单，所以
        HttpUtils.getInstance().getOrderByType(page,2,2,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        try{
                            List<OrderEntity> entities = new Gson().fromJson(t, OrderEntityWrapper.class).getData();
                            if(entities==null||entities.isEmpty()){
                                if(page==1){
                                    hasData.set(false);
                                    binding.refreshWaitReceive.finishRefreshing();
                                    adapter.getItems().clear();
                                }else{
                                    binding.refreshWaitReceive.finishLoadmore();
                                }
                            }else{
                                if(page==1){
                                    hasData.set(true);
                                    binding.refreshWaitReceive.finishRefreshing();
                                    adapter.getItems().clear();
                                    adapter.getItems().addAll(entities);
                                }else{
                                    binding.refreshWaitReceive.finishLoadmore();
                                    adapter.getItems().addAll(entities);
                                }
                                pageNum++;
                            }
                        }catch (Exception e){
                            if(page==1){
                                binding.refreshWaitReceive.finishRefreshing();
                            }else{
                                binding.refreshWaitReceive.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshWaitReceive.finishRefreshing();
                        }else{
                            binding.refreshWaitReceive.finishLoadmore();
                        }
                    }
                });
    }
}
