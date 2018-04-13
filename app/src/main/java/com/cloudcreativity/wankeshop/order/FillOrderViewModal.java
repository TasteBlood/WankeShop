package com.cloudcreativity.wankeshop.order;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.ItemFillOrderGoodsLayoutBinding;
import com.cloudcreativity.wankeshop.entity.AddressEntity;
import com.cloudcreativity.wankeshop.entity.ShopCarItemEntity;
import com.cloudcreativity.wankeshop.main.ShoppingCarFragment;
import com.cloudcreativity.wankeshop.utils.ChooseReceiveAddressDialogUtils;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.StrUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是填写订单的ViewModal
 */
public class FillOrderViewModal {
    private FillOrderActivity context;
    private BaseDialogImpl baseDialog;


    public BaseBindingRecyclerViewAdapter<ShopCarItemEntity, ItemFillOrderGoodsLayoutBinding> adapter;

    public ObservableField<String> totalMoney = new ObservableField<>();

    private float final_money = 0.00f;

    //收货人信息

    public ObservableField<String> address = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();

    private int addressId = 0;

    private List<AddressEntity> addressEntities = new ArrayList<>();//保存地址信息

    FillOrderViewModal(FillOrderActivity context, BaseDialogImpl baseDialog, List<ShopCarItemEntity> entities) {
        this.context = context;
        this.baseDialog = baseDialog;
        adapter = new BaseBindingRecyclerViewAdapter<ShopCarItemEntity, ItemFillOrderGoodsLayoutBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_fill_order_goods_layout;
            }

            @Override
            protected void onBindItem(ItemFillOrderGoodsLayoutBinding binding, ShopCarItemEntity item, int position) {
                binding.setGoods(item);
                binding.tvPrice.setText(String.format(context.getString(R.string.str_rmb_character), Float.parseFloat(item.getSku().getSalePrice())));
            }
        };
        adapter.getItems().addAll(entities);

        //计算出总价
        for (ShopCarItemEntity entity : adapter.getItems()) {
            final_money += (entity.getNum() * Float.parseFloat(entity.getSku().getSalePrice()));
        }
        totalMoney.set(String.format(context.getString(R.string.str_rmb_character), final_money));

        loadAddress();

        //将页面滚动到顶部

    }

    public void onBack(View view) {
        context.finish();
    }

    /**
     * 加载收货地址信息
     */
    private void loadAddress() {
        HttpUtils.getInstance().getMyAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, true) {
                    @Override
                    public void onSuccess(String t) {
                        //显示默认地址
                        Type type = new TypeToken<List<AddressEntity>>() {
                        }.getType();
                        addressEntities = new Gson().fromJson(t, type);

                        for (AddressEntity entity : addressEntities) {
                            if (entity.getIsDefault() == 1) {
                                address.set(entity.formatAddress());
                                name.set(entity.getReceiptName());
                                phone.set(entity.formatPhone());
                                addressId = entity.getId();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    @BindingAdapter("itemPic")
    public static void displayImage(ImageView imageView, String url) {
        GlideUtils.load(imageView.getContext(), url, imageView);
    }

    /**
     * 选择收货地址
     */
    private ChooseReceiveAddressDialogUtils utils;

    public void onChooseAddressClick(View view) {
        utils = new ChooseReceiveAddressDialogUtils(context);
        utils.setOnItemClickListener(new ChooseReceiveAddressDialogUtils.OnItemClickListener() {
            @Override
            public void onItemClick(AddressEntity entity) {
                addressId = entity.getId();
                address.set(entity.formatAddress());
                name.set(entity.getReceiptName());
                phone.set(entity.formatPhone());
            }
        });
        utils.show(addressEntities);
    }

    //刷新数据
    public void updateAddress(BaseDialogImpl baseDialog) {
        if (utils != null)
            utils.updateData(baseDialog);
    }

    //提交订单
    public void submitOrders(View view){
        if(addressId<=0){
            ToastUtils.showShortToast(context,"收货人地址不能为空");
            return;
        }
        if(adapter.getItemCount()<=0){
            ToastUtils.showShortToast(context,"商品信息不能为空");
            return;
        }
        StringBuilder builder = new StringBuilder();
        for(ShopCarItemEntity entity:adapter.getItems()){
            builder.append(entity.getId()).append(",");
        }

        String ids = builder.substring(0,builder.length()-1);

        HttpUtils.getInstance().addOrder(ids,addressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        //跳转到支付页面，并且清空购物车
                        Intent intent = new Intent(context,PayOrderActivity.class);
                        intent.putExtra("orderNum",t);
                        intent.putExtra("totalMoney",final_money);
                        context.startActivity(intent);
                        //刷新购物车
                        EventBus.getDefault().post(ShoppingCarFragment.MSG_REFRESH_SHOP_CAR);
                        context.finish();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

    }
}
