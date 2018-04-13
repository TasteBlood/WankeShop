package com.cloudcreativity.wankeshop.order.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import com.cloudcreativity.wankeshop.databinding.FragmentReturnOrderBinding;
import com.cloudcreativity.wankeshop.entity.ReturnOrderEntity;
import com.cloudcreativity.wankeshop.entity.ReturnOrderWrapper;
import com.cloudcreativity.wankeshop.entity.ShopEntity;
import com.cloudcreativity.wankeshop.order.OrderDetailActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.cloudcreativity.wankeshop.databinding.ItemOrderReturnBinding;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 退货ViewModal
 */
public class ReturnOrderViewModal {

    public ObservableField<Boolean> hasData = new ObservableField<>();
    public BaseBindingRecyclerViewAdapter<ReturnOrderEntity,ItemOrderReturnBinding> adapter;

    private Context context;
    private BaseDialogImpl baseDialog;
    private FragmentReturnOrderBinding binding;
    private int pageNum =1;

    private int currentPosition;

    ReturnOrderViewModal(Context context, BaseDialogImpl baseDialog, FragmentReturnOrderBinding binding) {
        this.context = context;
        this.baseDialog = baseDialog;
        this.binding = binding;

        this.binding.refreshReturn.setOnRefreshListener(new RefreshListenerAdapter() {
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

        hasData.set(true);

        adapter = new BaseBindingRecyclerViewAdapter<ReturnOrderEntity, ItemOrderReturnBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_order_return;
            }

            @Override
            protected void onBindItem(ItemOrderReturnBinding binding, final ReturnOrderEntity item, final int position) {
                binding.setItem(item);
                binding.tvNumberAndPrice.setText(
                        String.format(context.getString(R.string.str_number_unit_price),
                                item.getOrder().getShoppingCart().getNum(),
                                Float.parseFloat(item.getOrder().getPayMoney())));

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderDetails(item);
                    }
                });

                binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(item);
                    }
                });

                binding.tvContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition = position;
                        contact(item);
                    }
                });
            }
        };
    }

    //联系卖家
    private void contact(ReturnOrderEntity item) {
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

    //删除订单
    private void deleteOrder(final ReturnOrderEntity item) {
        HttpUtils.getInstance().deleteReturnOrders(String.valueOf(item.getId()))
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

    //订单详情
    private void orderDetails(ReturnOrderEntity item) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("order",item);
        context.startActivity(intent);
    }

    private void loadData(final int page){
        HttpUtils.getInstance().getReturnOrders(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        List<ReturnOrderEntity> entities =  new Gson().fromJson(t, ReturnOrderWrapper.class).getData();
                        if(entities==null||entities.isEmpty()){
                            if(page==1){
                                binding.refreshReturn.finishRefreshing();
                                hasData.set(false);
                                adapter.getItems().clear();
                            }else{
                                binding.refreshReturn.finishLoadmore();
                            }
                        }else{
                            if(page==1){
                                binding.refreshReturn.finishRefreshing();
                                hasData.set(true);
                                adapter.getItems().clear();
                                adapter.getItems().addAll(entities);
                            }else{
                                binding.refreshReturn.finishLoadmore();
                                adapter.getItems().addAll(entities);
                            }
                            pageNum++;
                        }

                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshReturn.finishRefreshing();
                        }else{
                            binding.refreshReturn.finishLoadmore();
                        }
                    }
                });
    }

    public void call(){
        contact(adapter.getItems().get(currentPosition));
    }
}
