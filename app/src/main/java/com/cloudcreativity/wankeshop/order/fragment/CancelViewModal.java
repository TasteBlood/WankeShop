package com.cloudcreativity.wankeshop.order.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentCancelBinding;
import com.cloudcreativity.wankeshop.databinding.ItemOrderPicBinding;
import com.cloudcreativity.wankeshop.databinding.ItemOrderCancelBinding;
import com.cloudcreativity.wankeshop.entity.BigOrderEntity;
import com.cloudcreativity.wankeshop.entity.OrderEntity;
import com.cloudcreativity.wankeshop.entity.ShopCarItemEntity;
import com.cloudcreativity.wankeshop.goods.ShoppingCarActivity;
import com.cloudcreativity.wankeshop.main.ShoppingCarFragment;
import com.cloudcreativity.wankeshop.order.OrderDetailActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 取消ViewModal
 */
public class CancelViewModal {
    private CancelFragment fragment;
    private BaseDialogImpl baseDialog;
    private Context context;
    private FragmentCancelBinding binding;

    public ObservableField<Boolean> hasData = new ObservableField<>();

    private int pageNum = 1;

    public BaseBindingRecyclerViewAdapter<BigOrderEntity,ItemOrderCancelBinding> adapter;
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

    CancelViewModal(CancelFragment fragment, BaseDialogImpl baseDialog, Context context,FragmentCancelBinding binding) {
        this.fragment = fragment;
        this.baseDialog = baseDialog;
        this.context = context;
        this.binding = binding;
        this.binding.refreshCancel.setOnRefreshListener(refreshListenerAdapter);
        hasData.set(true);

        adapter = new BaseBindingRecyclerViewAdapter<BigOrderEntity, ItemOrderCancelBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_order_cancel;
            }

            @Override
            protected void onBindItem(ItemOrderCancelBinding binding, BigOrderEntity item, int position) {
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
    private void initLayout(ItemOrderCancelBinding binding, final BigOrderEntity item, int position) {
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
            binding.rcvOrderItemCancel.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

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
            binding.rcvOrderItemCancel.setAdapter(picAdapter);
            binding.rcvOrderItemCancel.setOnTouchListener(new View.OnTouchListener() {
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

        binding.tvReBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新购买
                reBuy(item);
            }
        });

        binding.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                deleteOrder(item);
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
     * @param item 删除当前的订单
     */
    private void deleteOrder(final BigOrderEntity item) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("删除订单")
                .setMessage("此操作不可逆，确定删除这个订单吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        StringBuilder builder = new StringBuilder();
                        for(OrderEntity entity:item.getData()){
                            builder.append(entity.getId()).append(",");
                        }
                        String ids = builder.substring(0, builder.length() - 1);

                        HttpUtils.getInstance().deleteOrders(ids)
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
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 取消订单
     * @param item 重新购买
     */
    private void reBuy(final BigOrderEntity item) {
        StringBuilder ids = new StringBuilder();
        for(BigOrderEntity entity : adapter.getItems()){
             for(OrderEntity orderEntity:entity.getData()){
                 ids.append(orderEntity.getId()).append(",");
             }
        }
        if(TextUtils.isEmpty(ids)){
            return;
        }
        HttpUtils.getInstance().orderReBuy(ids.substring(0,ids.length()-1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        if("0".equals(t)){
                            //重新购买失败
                            ToastUtils.showShortToast(context,"该商品暂时无法购买");
                            Intent intent = new Intent(context,OrderDetailActivity.class);
                            intent.putExtra("order",item);
                            context.startActivity(intent);
                        }else if("1".equals(t)){
                            //重新购买成功，跳转到购物车
                            EventBus.getDefault().post(ShoppingCarFragment.MSG_REFRESH_SHOP_CAR);
                            Intent intent = new Intent(context, ShoppingCarActivity.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //加载数据
    private void loadData(final int page){
        //加载的是大订单，所以
        HttpUtils.getInstance().getOrdersByType(page,1,1,0)
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
                                    binding.refreshCancel.finishRefreshing();
                                    adapter.getItems().clear();
                                }else{
                                    binding.refreshCancel.finishLoadmore();
                                }
                            }else{
                                if(page==1){
                                    hasData.set(true);
                                    binding.refreshCancel.finishRefreshing();
                                    adapter.getItems().clear();
                                    adapter.getItems().addAll(entities);
                                }else{
                                    binding.refreshCancel.finishLoadmore();
                                    adapter.getItems().addAll(entities);
                                }
                                pageNum++;
                            }
                        }catch (Exception e){
                            if(page==1){
                                binding.refreshCancel.finishRefreshing();
                            }else{
                                binding.refreshCancel.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshCancel.finishRefreshing();
                        }else{
                            binding.refreshCancel.finishLoadmore();
                        }
                    }
                });
    }
}
