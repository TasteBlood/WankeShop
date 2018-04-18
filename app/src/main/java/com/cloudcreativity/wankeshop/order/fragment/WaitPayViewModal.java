package com.cloudcreativity.wankeshop.order.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentWaitPayBinding;
import com.cloudcreativity.wankeshop.entity.BigOrderEntity;
import com.cloudcreativity.wankeshop.entity.OrderEntity;
import com.cloudcreativity.wankeshop.entity.ShopCarItemEntity;
import com.cloudcreativity.wankeshop.order.OrderDetailActivity;
import com.cloudcreativity.wankeshop.order.PayOrderActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import com.cloudcreativity.wankeshop.databinding.ItemOrderWaitPayBinding;
import com.cloudcreativity.wankeshop.databinding.ItemOrderPicBinding;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 待付款ViewModal
 */
public class WaitPayViewModal {
    private WaitPayFragment fragment;
    private BaseDialogImpl baseDialog;
    private Context context;
    private FragmentWaitPayBinding binding;

    public ObservableField<Boolean> hasData = new ObservableField<>();

    private int pageNum = 1;

    public BaseBindingRecyclerViewAdapter<BigOrderEntity,ItemOrderWaitPayBinding> adapter;
    /**
     * 这是下拉上拉的回掉
     */
    private RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(pageNum);
        }

        @Override
        public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
            loadData(pageNum);
        }
    };

    WaitPayViewModal(WaitPayFragment fragment, BaseDialogImpl baseDialog, Context context,FragmentWaitPayBinding binding) {
        this.fragment = fragment;
        this.baseDialog = baseDialog;
        this.context = context;
        this.binding = binding;
        this.binding.refreshWaitPay.setOnRefreshListener(refreshListenerAdapter);
        hasData.set(true);

        adapter = new BaseBindingRecyclerViewAdapter<BigOrderEntity, ItemOrderWaitPayBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_order_wait_pay;
            }

            @Override
            protected void onBindItem(ItemOrderWaitPayBinding binding, BigOrderEntity item, int position) {
                binding.setItem(item);
                initLayout(binding,item,position);
            }
        };
    }

    /**
     * 初始化item布局
     * @param binding viewBinding
     * @param item 数据
     * @param position 位置
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initLayout(ItemOrderWaitPayBinding binding, final BigOrderEntity item, int position) {
        if(item.getData().size()>1){
            //显示多内容布局
            binding.layoutMulti.setVisibility(View.VISIBLE);
            binding.layoutSingle.setVisibility(View.GONE);
            //首先计算出商品的数量
            int number = 0;
            float totalMoney = 0.00f;
            for(OrderEntity entity : item.getData()){
                number += entity.getShoppingCart().getNum();
                totalMoney += Float.parseFloat(entity.getPayMoney());
            }
            binding.tvNumberAndPrice.setText(
                    String.format(context.getString(R.string.str_number_unit_price), number, totalMoney));

            //展示商品的图片
            binding.rcvOrderItemNotPay.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

            //设置adapter
            BaseBindingRecyclerViewAdapter<OrderEntity,ItemOrderPicBinding> picAdapter = new BaseBindingRecyclerViewAdapter<OrderEntity, ItemOrderPicBinding>(context) {
                @Override
                protected int getLayoutResId(int viewType) {
                    return R.layout.item_order_pic;
                }

                @Override
                protected void onBindItem(ItemOrderPicBinding binding, OrderEntity item, int position) {
                    binding.setData(item);
                }
            };

            picAdapter.getItems().addAll(item.getData());
            binding.rcvOrderItemNotPay.setAdapter(picAdapter);
            binding.rcvOrderItemNotPay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_UP){
                        orderDetails(item);
                    }
                  return true;
                }
            });
        }else{
            //显示单一内容布局
            binding.layoutSingle.setVisibility(View.VISIBLE);
            binding.layoutMulti.setVisibility(View.GONE);

            ShopCarItemEntity cart = item.getData().get(0).getShoppingCart();
            binding.tvName.setText(cart.getSku().getSkuName());

            binding.tvNumberAndPrice.setText(
                    String.format(context.getString(R.string.str_number_unit_price),
                            cart.getNum(),
                            Float.parseFloat(item.getData().get(0).getPayMoney())));

        }

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消订单
                cancelOrder(item);
            }
        });

        binding.tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付订单
                payOrder(item);
            }
        });

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //订单详情
                orderDetails(item);
            }
        });

    }

    /**
     * 订单详情
     * @param item 当前的订单
     */
    private void orderDetails(BigOrderEntity item) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("order",item);
        context.startActivity(intent);
    }

    /**
     * 支付订单
     * @param item 当前的订单
     */
    private void payOrder(BigOrderEntity item) {
        //跳转到支付页面
        float money = 0.00f;
        for(OrderEntity entity:item.getData()){
            money += Float.parseFloat(entity.getPayMoney());
        }
        Intent intent = new Intent(context,PayOrderActivity.class);
        intent.putExtra("orderNum",item.getOnlyId());
        intent.putExtra("totalMoney",money);
        context.startActivity(intent);
    }

    /**
     * 取消订单
     * @param item 当前的订单
     */
    private void cancelOrder(final BigOrderEntity item) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("取消订单")
                .setMessage("客官三思而后行，后期可以在已取消订单中找回该订单，确定取消吗?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        StringBuilder builder = new StringBuilder();
                        for(OrderEntity entity:item.getData()){
                            builder.append(entity.getId()).append(",");
                        }
                        String ids = builder.substring(0, builder.length() - 1);

                        HttpUtils.getInstance().scrapOrders(ids)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                    @Override
                                    public void onSuccess(String t) {
                                        //发消息更新订单列表页面
                                        adapter.getItems().remove(item);
                                    }

                                    @Override
                                    public void onFail(ExceptionReason msg) {

                                    }
                                });
                    }
                }).create();
        dialog.show();
    }

    //加载数据
    private void loadData(final int page){
        //加载的是大订单，所以
        HttpUtils.getInstance().getOrdersByType(page,1,1,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        try{
                            Type type = new TypeToken<List<BigOrderEntity>>() {
                            }.getType();
                            List<BigOrderEntity> entities = new Gson().fromJson(t,type);
                            if(entities==null||entities.isEmpty()){
                                if(page==1){
                                    hasData.set(false);
                                    binding.refreshWaitPay.finishRefreshing();
                                    adapter.getItems().clear();
                                }else{
                                    binding.refreshWaitPay.finishLoadmore();
                                }
                            }else{
                                if(page==1){
                                    hasData.set(true);
                                    binding.refreshWaitPay.finishRefreshing();
                                    adapter.getItems().clear();
                                    adapter.getItems().addAll(entities);
                                }else{
                                    binding.refreshWaitPay.finishLoadmore();
                                    adapter.getItems().addAll(entities);
                                }
                                pageNum++;
                            }
                        }catch (Exception e){
                            if(page==1){
                                binding.refreshWaitPay.finishRefreshing();
                            }else{
                                binding.refreshWaitPay.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshWaitPay.finishRefreshing();
                        }else{
                            binding.refreshWaitPay.finishLoadmore();
                        }
                    }
                });
    }



}
