package com.cloudcreativity.wankeshop.order.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentFinishBinding;
import com.cloudcreativity.wankeshop.databinding.ItemOrderFinishBinding;
import com.cloudcreativity.wankeshop.entity.OrderEntity;
import com.cloudcreativity.wankeshop.entity.OrderEntityWrapper;
import com.cloudcreativity.wankeshop.goods.ShoppingCarActivity;
import com.cloudcreativity.wankeshop.main.ShoppingCarFragment;
import com.cloudcreativity.wankeshop.order.OrderDetailActivity;
import com.cloudcreativity.wankeshop.order.OrderDetailViewModal;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ReturnGoodsDialogUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 已完成ViewModal
 */
public class FinishViewModal {
    private Context context;
    private BaseDialogImpl baseDialog;
    private FragmentFinishBinding binding;

    public BaseBindingRecyclerViewAdapter<OrderEntity,ItemOrderFinishBinding> adapter;

    public ObservableField<Boolean> hasData = new ObservableField<>();

    private int pageNum = 1;

    FinishViewModal(Context context, BaseDialogImpl baseDialog, FragmentFinishBinding binding) {
        this.context = context;
        this.baseDialog = baseDialog;
        this.binding = binding;

        hasData.set(true);

        adapter = new BaseBindingRecyclerViewAdapter<OrderEntity, ItemOrderFinishBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_order_finish;
            }

            @Override
            protected void onBindItem(ItemOrderFinishBinding binding, OrderEntity item, int position) {
                binding.setItem(item);
                initLayout(binding,item,position);
            }
        };
        this.binding.refreshFinish.setOnRefreshListener(new RefreshListenerAdapter() {
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
    private void initLayout(ItemOrderFinishBinding binding,final OrderEntity item, int position) {
        binding.tvNumberAndPrice.setText(
                String.format(context.getString(R.string.str_number_unit_price),
                        item.getShoppingCart().getNum(),
                        Float.parseFloat(item.getPayMoney())));

        if(item.getRefundState()!=0){
            //这是退款完成
            binding.tvReturn.setVisibility(View.GONE);
            binding.tvState.setText(R.string.str_refund_success);
        }else{
            //这是退货完成
            if(item.getReturnState()==1){
                binding.tvReturn.setVisibility(View.GONE);
                binding.tvState.setText(R.string.str_return_goods_success);
            }else{
                binding.tvState.setText(R.string.str_completed);
                if(item.getIsNoReason()==1){
                    //支持7天无忧
                    if(StrUtils.isEnoughSevenDay(item.getCompleteTime())){
                        //说明还在7天之内，可以退货
                        binding.tvReturn.setVisibility(View.VISIBLE);
                    }else{
                        //不在7天之内，不可以退货
                        binding.tvReturn.setVisibility(View.GONE);
                    }
                }else{
                    //不支持
                    binding.tvReturn.setVisibility(View.GONE);
                }
            }
        }

        binding.tvReBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reBuy(item);
            }
        });

        binding.tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnGoods(item);
            }
        });

        binding.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrder(item);
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

    //再购买
    private void reBuy(final OrderEntity item) {
        HttpUtils.getInstance().orderReBuy(String.valueOf(item.getId()))
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

    //退换货
    private void returnGoods(final OrderEntity item) {
        ReturnGoodsDialogUtils utils = new ReturnGoodsDialogUtils();
        utils.setOnOkClickListener(new ReturnGoodsDialogUtils.OnOkClickListener() {
            @Override
            public void onOkClick(String reason,int number) {
                HttpUtils.getInstance().addReturnOrder(item.getId(),
                        item.getShopId(),
                        item.getShoppingCart().getSkuId(),
                        reason,
                        number)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<String>(baseDialog,true) {
                            @Override
                            public void onSuccess(String t) {
                                //发消息更新订单列表页面
                                ToastUtils.showShortToast(context,"已提交");
                                EventBus.getDefault().post(OrderDetailViewModal.MSG_RECEIVE_ORDER);
                            }

                            @Override
                            public void onFail(ExceptionReason msg) {

                            }
                        });
            }
        });

        utils.show(context,item,false);
    }

    /**
     * 支付订单
     * @param item 删除当前的订单
     */
    private void deleteOrder(final OrderEntity item) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("删除订单")
                .setMessage("此操作不可逆，确定删除这个订单吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        HttpUtils.getInstance().deleteOrders(String.valueOf(item.getId()))
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

    //加载数据
    //加载数据
    private void loadData(final int page){
        HttpUtils.getInstance().getOrderByType(page,2,3,1)
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
                                    binding.refreshFinish.finishRefreshing();
                                    adapter.getItems().clear();
                                }else{
                                    binding.refreshFinish.finishLoadmore();
                                }
                            }else{
                                if(page==1){
                                    hasData.set(true);
                                    binding.refreshFinish.finishRefreshing();
                                    adapter.getItems().clear();
                                    adapter.getItems().addAll(entities);
                                }else{
                                    binding.refreshFinish.finishLoadmore();
                                    adapter.getItems().addAll(entities);
                                }
                                pageNum++;
                            }
                        }catch (Exception e){
                            if(page==1){
                                binding.refreshFinish.finishRefreshing();
                            }else{
                                binding.refreshFinish.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshFinish.finishRefreshing();
                        }else{
                            binding.refreshFinish.finishLoadmore();
                        }
                    }
                });
    }
}
