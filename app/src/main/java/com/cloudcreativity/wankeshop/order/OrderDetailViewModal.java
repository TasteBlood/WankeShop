package com.cloudcreativity.wankeshop.order;

import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ActivityOrderDetailBinding;
import com.cloudcreativity.wankeshop.entity.AddressEntity;
import com.cloudcreativity.wankeshop.entity.BigOrderEntity;
import com.cloudcreativity.wankeshop.entity.OrderEntity;
import com.cloudcreativity.wankeshop.entity.ReturnOrderEntity;
import com.cloudcreativity.wankeshop.entity.ShopCarItemEntity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ReturnGoodsDialogUtils;
import com.google.gson.Gson;
import com.cloudcreativity.wankeshop.databinding.ItemFillOrderGoodsLayoutBinding;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是订单详情ViewModal
 */
public class OrderDetailViewModal {
    private OrderDetailActivity context;
    private BaseDialogImpl baseDialog;
    private ActivityOrderDetailBinding binding;
    private Serializable order;


    //这些都是显示的信息
    public ObservableField<String> orderState = new ObservableField<>();//订单状态

    public ObservableField<String> returnOrderNum = new ObservableField<>();//退货单号

    public ObservableField<String> name = new ObservableField<>();//收货人姓名
    public ObservableField<String> phone = new ObservableField<>();//收货人电话
    public ObservableField<String> address = new ObservableField<>();//收货人地址

    public ObservableField<String> orderNumber = new ObservableField<>();//订单号
    public ObservableField<String> orderDate = new ObservableField<>();//下单时间
    public ObservableField<String> orderPyWay = new ObservableField<>();//支付方式

    public ObservableField<String> money = new ObservableField<>();//支付总额

    public BaseBindingRecyclerViewAdapter<OrderEntity,ItemFillOrderGoodsLayoutBinding> adapter;


    //这是消息

    public static final String MSG_DELETE_ORDER = "msg_delete_order";//删除
    public static final String MSG_CANCEL_ORDER = "msg_cancel_order";//取消
    public static final String MSG_RETURN_ORDER = "msg_return_order";//退货
    public static final String MSG_RECEIVE_ORDER = "msg_receive_order";//收货
    public static final String MSG_PAY_SUCCESS = "msg_pay_success";//支付成功

    OrderDetailViewModal(OrderDetailActivity context, BaseDialogImpl baseDialog,Serializable order,ActivityOrderDetailBinding binding) {
        this.context = context;
        this.baseDialog = baseDialog;
        this.order = order;
        this.binding = binding;

        //展示商品信息
        adapter = new BaseBindingRecyclerViewAdapter<OrderEntity, ItemFillOrderGoodsLayoutBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_fill_order_goods_layout;
            }

            @Override
            protected void onBindItem(ItemFillOrderGoodsLayoutBinding binding, OrderEntity item, int position) {
                binding.setGoods(item.getShoppingCart());
                binding.tvPrice.setText(String.format(context.getString(R.string.str_rmb_character), Float.parseFloat(item.getShoppingCart().getSku().getSalePrice())));
            }
        };

        /*
         * 这么操作的原因已经在activity中说明
         */
        int addressId = 0;
        if(order instanceof BigOrderEntity){
            addressId = ((BigOrderEntity) order).getData().get(0).getAddressId();
        }else if(order instanceof OrderEntity){
            addressId = ((OrderEntity) order).getAddressId();
        }else if(order instanceof ReturnOrderEntity){
            addressId = ((ReturnOrderEntity) order).getAddressId();
        }

        loadAddress(addressId);

    }

    public void onBack(View view){
        context.finish();
    }

    /**
     *
     * @param addressId 地址id
     *                  加载收货地址信息
     */
    private void loadAddress(int addressId){
        HttpUtils.getInstance().getAddressDetail(addressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        //展示全部的数据
                        AddressEntity entity = new Gson().fromJson(t, AddressEntity.class);
                        name.set(entity.getReceiptName());
                        phone.set(entity.formatPhone());
                        address.set(entity.formatAddress());
                        /*
                         * 这么操作的原因已经在activity中说明
                         */
                        if(order instanceof BigOrderEntity){
                            displayBigOrder((BigOrderEntity)order);
                        }else if(order instanceof OrderEntity){
                            displayOrder((OrderEntity)order);
                        }else if(order instanceof ReturnOrderEntity){
                            displayReturnOrder((ReturnOrderEntity)order);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        context.finish();
                    }
                });
    }

    //显示退货的订单
    private void displayReturnOrder(ReturnOrderEntity order) {
        orderNumber.set(order.getOrder().getOrderNum());
        orderDate.set(order.getOrder().getCreateTime());
        returnOrderNum.set(order.getReturnNum());
        if(order.getOrder().getPayWay()==1){
            //微信支付
            orderPyWay.set("微信支付");
        }else if(order.getOrder().getPayWay()==2){
            //支付宝支付
            orderPyWay.set("支付宝支付");
        }else if(order.getOrder().getPayWay()==3){
            //账户余额方式
            orderPyWay.set("账户余额");
        }else{
            orderPyWay.set("其他支付");
        }

        money.set(String.format(context.getString(R.string.str_rmb_character),Float.parseFloat(order.getOrder().getPayMoney())));
        //展示状态
        if(order.getReturnState()==3){
            //已退货
            orderState.set(context.getString(R.string.str_completed));
            binding.tvDelete.setVisibility(View.VISIBLE);

        }else if(order.getReturnState()==1||order.getReturnState()==2){
            //退货中
            orderState.set(context.getString(R.string.str_returning));
            binding.tvContact.setVisibility(View.VISIBLE);
        }

        adapter.getItems().add(order.getOrder());
    }

    //显示订单
    private void displayOrder(OrderEntity order) {
        //小定单一定是这两种情况 待收货 shipState = 1|2  已完成 shipState=3

        orderNumber.set(order.getOrderNum());
        orderDate.set(order.getCreateTime());

        if(order.getPayWay()==1){
            //微信支付
            orderPyWay.set("微信支付");
        }else if(order.getPayWay()==2){
            //支付宝支付
            orderPyWay.set("支付宝支付");
        }else if(order.getPayWay()==3){
            //账户余额方式
            orderPyWay.set("账户余额");
        }else{
            orderPyWay.set("其他支付");
        }

        money.set(String.format(context.getString(R.string.str_rmb_character),Float.parseFloat(order.getPayMoney())));
        //展示状态
        if(order.getShipState()==1||order.getShipState()==2){
            //待收货
            orderState.set("待收货".concat("(").concat(order.getShipState()==1?"未发货":"已发货").concat(")"));
            if(order.getShipState()==1){
                binding.tvContact.setVisibility(View.VISIBLE);
            }
            binding.tvEnterReceive.setVisibility(View.VISIBLE);

        }else if(order.getShipState()==3){
            //完成
            orderState.set("已完成");
            binding.tvDelete.setVisibility(View.VISIBLE);
            binding.tvReturn.setVisibility(View.VISIBLE);
            binding.tvReBuy.setVisibility(View.VISIBLE);
        }

        adapter.getItems().add(order);

    }

    //显示大订单
    private void displayBigOrder(BigOrderEntity bigOrder) {
        //一定是这两种情况  已取消 state=0  未支付 payState=1
        orderPyWay.set("未支付");
        List<OrderEntity> shopCarItemEntities  = new ArrayList<>();
        //显示信息
        orderNumber.set(bigOrder.getOnlyId());
        float totalMoney = 0.00f;
        for(int i=0;i<bigOrder.getData().size();i++){
            OrderEntity orderEntity = bigOrder.getData().get(i);
            shopCarItemEntities.add(orderEntity);
            if(i==0){
                //这是第一个数据
                orderDate.set(orderEntity.getCreateTime());
                if(orderEntity.getState()==0){
                    //已经废弃的订单
                    orderState.set("已取消");
                    binding.tvDelete.setVisibility(View.VISIBLE);
                    binding.tvReBuy.setVisibility(View.VISIBLE);
                }else if(orderEntity.getPayState()==1){
                    //未支付
                    orderState.set("未支付");
                    binding.tvCancel.setVisibility(View.VISIBLE);
                    binding.tvGoPay.setVisibility(View.VISIBLE);
                }
            }
            totalMoney+=Float.parseFloat(orderEntity.getPayMoney());
        }
        money.set(String.format(context.getString(R.string.str_rmb_character),totalMoney));

        //展示图片
        adapter.getItems().addAll(shopCarItemEntities);
    }


    //删除订单
    public void onDeleteClick(View view){
        if(order instanceof ReturnOrderEntity){
            HttpUtils.getInstance().deleteReturnOrders(String.valueOf(((ReturnOrderEntity)order).getId()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            EventBus.getDefault().post(MSG_RETURN_ORDER);
                            context.finish();
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });
        }else{
            StringBuilder builder = new StringBuilder();
            for(OrderEntity entity:adapter.getItems()){
                builder.append(entity.getId()).append(",");
            }
            String ids = builder.substring(0, builder.length() - 1);

            HttpUtils.getInstance().deleteOrders(ids)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            //发消息更新订单列表页面
                            EventBus.getDefault().post(MSG_DELETE_ORDER);
                            context.finish();
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });
        }
    }

    //取消订单
    public void onCancelClick(View view){
        StringBuilder builder = new StringBuilder();
        for(OrderEntity entity:adapter.getItems()){
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
                        EventBus.getDefault().post(MSG_CANCEL_ORDER);
                        context.finish();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //联系卖家
    public void onContactSellerClick(View view){

    }

    //退货
    public void onReturnClick(View view){

        ReturnGoodsDialogUtils utils = new ReturnGoodsDialogUtils();
        utils.setOnOkClickListener(new ReturnGoodsDialogUtils.OnOkClickListener() {
            @Override
            public void onOkClick(String reason, int number) {
                HttpUtils.getInstance().addReturnOrder(((OrderEntity) order).getId(),
                        ((OrderEntity) order).getShopId(),
                        ((OrderEntity) order).getShoppingCart().getSkuId(),
                        reason,
                        number)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<String>(baseDialog,true) {
                            @Override
                            public void onSuccess(String t) {
                                //发消息更新订单列表页面
                                EventBus.getDefault().post(MSG_RETURN_ORDER);
                                context.finish();
                            }

                            @Override
                            public void onFail(ExceptionReason msg) {

                            }
                        });
            }
        });

        utils.show(context, (OrderEntity) order);

    }

    //确定收货
    public void onEnterReceiveClick(View view){

        HttpUtils.getInstance().updateOrderState(((OrderEntity)order).getId(),3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                            EventBus.getDefault().post(MSG_RECEIVE_ORDER);
                            context.finish();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //再次购买
    public void onReBuyClick(View view){

    }

    //去支付
    public void onPayClick(View view){
        //跳转到支付页面
        float money = 0.00f;
        for(OrderEntity entity:((BigOrderEntity)order).getData()){
            money += Float.parseFloat(entity.getPayMoney());
        }
        Intent intent = new Intent(context,PayOrderActivity.class);
        intent.putExtra("orderNum",((BigOrderEntity)order).getOnlyId());
        intent.putExtra("totalMoney",money);
        context.startActivity(intent);
    }
}