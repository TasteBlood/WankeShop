package com.cloudcreativity.wankeshop.order.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentWaitReceiveBinding;
import com.cloudcreativity.wankeshop.databinding.ItemOrderWaitReceiveBinding;
import com.cloudcreativity.wankeshop.entity.OrderEntity;
import com.cloudcreativity.wankeshop.entity.OrderEntityWrapper;
import com.cloudcreativity.wankeshop.entity.ShopEntity;
import com.cloudcreativity.wankeshop.order.OrderDetailActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
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

    private int currentPosition = 0;

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
    private void initLayout(ItemOrderWaitReceiveBinding binding, final OrderEntity item, final int position) {
        binding.tvNumberAndPrice.setText(
                String.format(context.getString(R.string.str_number_unit_price),
                        item.getShoppingCart().getNum(),
                        Float.parseFloat(item.getPayMoney())));

        binding.tvDelay.setVisibility(item.getIsDelay()==1?View.GONE:View.VISIBLE);

        binding.tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                contactSeller(item);
            }
        });

        binding.tvEnterReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterReceive(item);
            }
        });

        binding.tvDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelayReceiveDialog(item,position);
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
        HttpUtils.getInstance().getShopInfo(item.getShopId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onSuccess(String t) {
                        ShopEntity entity = new Gson().fromJson(t, ShopEntity.class);
                        int result = context.checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE);
                        if(result== PackageManager.PERMISSION_DENIED){
                            ((Activity)context).requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
                        }else{
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel://".concat(entity.getContactMobile())));
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //延迟收货
    private void showDelayReceiveDialog(final OrderEntity item,final int position){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("延长收货提示")
                .setMessage("每个订单只能延长收货一次，确定延长收货吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HttpUtils.getInstance().delayReceiveOrder(item.getId(),1)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                    @Override
                                    public void onSuccess(String t) {
                                        item.setIsDelay(1);
                                        adapter.notifyItemChanged(position);
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
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
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

    //打电话
    public void call(){
        contactSeller(adapter.getItems().get(currentPosition));
    }
}
