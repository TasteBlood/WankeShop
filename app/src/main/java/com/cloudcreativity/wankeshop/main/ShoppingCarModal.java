package com.cloudcreativity.wankeshop.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentShoppingcarBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;
import com.cloudcreativity.wankeshop.entity.ShopCarItemEntity;
import com.cloudcreativity.wankeshop.entity.ShopCarItemWrapper;
import com.cloudcreativity.wankeshop.goods.GoodsDetailActivity;
import com.cloudcreativity.wankeshop.order.FillOrderActivity;
import com.cloudcreativity.wankeshop.utils.ChooseShopItemNumberDialogUtils;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.DeleteShopCarItemDialog;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.cloudcreativity.wankeshop.databinding.ItemShoppingCarItemBinding;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是购物车ViewModal
 */
public class ShoppingCarModal {
    private ShoppingCarFragment fragment;
    private FragmentShoppingcarBinding binding;
    private BaseDialogImpl baseDialog;
    private Context context;
    public BaseBindingRecyclerViewAdapter<ShopCarItemEntity,ItemShoppingCarItemBinding> adapter;

    private int pageNum = 1;
    //购物车是否有商品
    public ObservableField<Boolean> hasItems = new ObservableField<>();

    //这是购物车的价格
    public ObservableField<String> totalPrice = new ObservableField<>();
    public ObservableField<String> totalNumber = new ObservableField<>();

    private float final_total_price = 0.00f;
    private int final_total_number = 0;

    //是否是全选
    private boolean isAllCheck = false;

    //是否是编辑
    public ObservableField<Boolean> isEdit = new ObservableField<>();

    private MyHandler handler = new MyHandler();

    public RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(TwinklingRefreshLayout refreshLayout) {
            binding.cbShoppingCarAll.setChecked(false);
            pageNum = 1;
            loadData(pageNum);
        }

        @Override
        public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
            loadData(pageNum);
        }
    };

    ShoppingCarModal(ShoppingCarFragment fragment, FragmentShoppingcarBinding binding, BaseDialogImpl baseDialog, Context context) {
        this.fragment = fragment;
        this.binding = binding;
        this.baseDialog = baseDialog;
        this.context = context;

        adapter = new BaseBindingRecyclerViewAdapter<ShopCarItemEntity, ItemShoppingCarItemBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_shopping_car_item;
            }

            @Override
            protected void onBindItem(ItemShoppingCarItemBinding binding, final ShopCarItemEntity item, int position) {
                    binding.setItem(item);
                    initialShopItems(binding,item,position);
                    binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, GoodsDetailActivity.class);
                            intent.putExtra("spuId",item.getSpuId());
                            context.startActivity(intent);
                        }
                    });
            }
        };
        
        binding.refreshShoppingCar.startRefresh();

        totalPrice.set(String.format(context.getString(R.string.str_shop_car_total_price),final_total_price));
        totalNumber.set(String.format(context.getString(R.string.str_make_order_now),final_total_number));
        binding.cbShoppingCarAll.setOnCheckedChangeListener(checkedChangeListener);

        isEdit.set(false);
        hasItems.set(true);
    }

    /**
     * 初始化每一个item的情况，内容及其复杂
     * @param binding
     */
    private void initialShopItems(final ItemShoppingCarItemBinding binding, final ShopCarItemEntity entity, final int position) {
        binding.tvShopCarPrice.setText(String.format(context.getString(R.string.str_rmb_character),Float.parseFloat(entity.getSku().getSalePrice())));
        //先判断是否可以购买
        if(entity.getSpu().getSpuStatus()==1||entity.getSku().getStatus()==1){
            //可购买
            binding.tvNotBuy.setVisibility(View.GONE);
        }else{
            //不可购买
            binding.tvNotBuy.setVisibility(View.VISIBLE);
        }

        if(entity.getNum()>entity.getSku().getDepositNum()){
            //就说明库存不足
            binding.tvNotEnough.setVisibility(View.VISIBLE);
        }else{
            //说明库存充足
            binding.tvNotEnough.setVisibility(View.GONE);
        }

        //这是数量点击，直接用对话框调整数量
        binding.tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseShopItemNumberDialogUtils utils = new ChooseShopItemNumberDialogUtils();
                utils.setOnNumberSaveListener(new ChooseShopItemNumberDialogUtils.OnNumberSaveListener() {
                    @Override
                    public void onSaveClick(int number) {
                        //在这里进行数量的调整
                        changeNumber(entity,number,position);
                    }
                });
                utils.show(context,entity);
            }
        });
        //这是plus方法点击
        binding.tvNumPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = entity.getNum();
                number+=1;
                if(number>entity.getSku().getDepositNum()){
                    //显示信息
                    ToastUtils.showLongToast(context,"数量不得超过"+entity.getSku().getDepositNum());
                    return;
                }
                //当最大值为0时，还需要判断数量区间的范围
                if(number>entity.getSku().getMaxQuality()&&!">=".equals(entity.getSku().getRegionOperator())){
                    //显示信息
                    ToastUtils.showLongToast(context,"数量不得超过"+entity.getSku().getMaxQuality());
                    return;
                }
                changeNumber(entity,number,position);
            }
        });

        //这是minus方法点击
        binding.tvNumMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = entity.getNum();
                number-=1;
                if(number<entity.getSku().getMinQuality()){
                    //显示信息
                    ToastUtils.showLongToast(context,"数量不得低于"+entity.getSku().getMinQuality());
                    return;
                }
                changeNumber(entity,number,position);
            }
        });

        //这是选择框的选中事件
        binding.cbShopCarItemCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //统计总价
                entity.isCheck = binding.cbShopCarItemCheck.isChecked();
                changePrice(binding.cbShopCarItemCheck.isChecked(),entity);
            }
        });
    }

    //这是修改总价的方法
    private void changePrice(final boolean isChecked, final ShopCarItemEntity entity) {
        if(isChecked){
            //说明就是增加价格
            baseDialog.showProgress("请稍后");
            new Thread(){
                @Override
                public void run() {
                    //说明有数据
                    float price = 0.00f;
                    int num = 0;
                    for(ShopCarItemEntity itemEntity:adapter.getItems()){
                        price+=(Float.parseFloat(itemEntity.getSku().getSalePrice())*itemEntity.getNum());
                        num+=itemEntity.getNum();
                    }
                    if(price==(final_total_price+entity.getNum()*Float.parseFloat(entity.getSku().getSalePrice()))){
                        //说明跟总价相等，即就是全选,但是如果是编辑状态，就不需要进行价格计算
                        Message msg = Message.obtain();
                        msg.what = MyHandler.MSG_ALL_CHECK;
                        Bundle bundle = new Bundle();
                        bundle.putFloat("total_price",price);
                        bundle.putInt("number",num);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain();
                        msg.what = MyHandler.MSG_PLUS_PRICE;
                        Bundle bundle = new Bundle();
                        bundle.putFloat("total_price",entity.getNum()*Float.parseFloat(entity.getSku().getSalePrice()));
                        bundle.putInt("number",entity.getNum());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }
            }
            .start();
        }else{
            //减少价格
            Message msg = Message.obtain();
            msg.what = MyHandler.MSG_MINUS_PRICE;
            Bundle bundle = new Bundle();
            bundle.putFloat("total_price",entity.getNum()*Float.parseFloat(entity.getSku().getSalePrice()));
            bundle.putInt("number",entity.getNum());
            msg.setData(bundle);
            handler.sendMessage(msg);
            if(isAllCheck){
                //如果是全选
                binding.cbShoppingCarAll.setOnCheckedChangeListener(null);
                binding.cbShoppingCarAll.setChecked(false);
                isAllCheck =false;
                binding.cbShoppingCarAll.setOnCheckedChangeListener(checkedChangeListener);
            }
        }
    }

    //修改数量的方法
    private void changeNumber(final ShopCarItemEntity entity, final int number, final int position) {
        HttpUtils.getInstance().editShoppingCarItem(entity.getId(),String.valueOf(number))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        //先根据以前的数据计算出价格
                        if(entity.isCheck){
                            int addNumber = number - entity.getNum();
                            float addPrice = addNumber*Float.parseFloat(entity.getSku().getSalePrice());
                            Message msg = Message.obtain();
                            msg.what = MyHandler.MSG_PLUS_PRICE;
                            Bundle bundle = new Bundle();
                            bundle.putFloat("total_price",addPrice);
                            bundle.putInt("number",addNumber);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                        entity.setNum(number);
                        adapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    private void loadData(final int page){
        HttpUtils.getInstance().getShoppingCarItems(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        List<ShopCarItemEntity> entities = new Gson().fromJson(t, ShopCarItemWrapper.class).getData();
                        if(entities==null||entities.isEmpty()){
                            //只在page等于1时，进行布局的切换而已
                            if(page==1){
                                hasItems.set(false);
                                binding.refreshShoppingCar.finishRefreshing();
                                adapter.getItems().clear();
                            }else{
                                binding.refreshShoppingCar.finishLoadmore();
                            }
                        }else{
                            if(page==1){
                                hasItems.set(true);
                                binding.refreshShoppingCar.finishRefreshing();
                                adapter.getItems().clear();
                                adapter.getItems().addAll(entities);
                            }else{
                                binding.refreshShoppingCar.finishLoadmore();
                                adapter.getItems().addAll(entities);
                            }
                            ShoppingCarModal.this.pageNum++;
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(pageNum==1){
                            binding.refreshShoppingCar.finishRefreshing();
                        }else{
                            binding.refreshShoppingCar.finishLoadmore();
                        }
                    }
                });
    }

    @BindingAdapter("shopItemPic")
    public static void displayImage(ImageView imageView,String url){
        GlideUtils.load(imageView.getContext(),url,imageView);
    }


    //这是自定义的Handler
    private  class MyHandler extends android.os.Handler{

        public static final int MSG_REFRESH_PRICE = 0x001;
        public static final int MSG_PLUS_PRICE = 0x002;
        public static final int MSG_MINUS_PRICE = 0x003;

        public static final int MSG_ALL_CHECK = 0x004;
        @Override
        public void handleMessage(Message msg) {
            //由于全选操作，开启子线程操作，所以在这里需要关闭对话框
            baseDialog.dismissProgress();
            switch (msg.what){
                //刷新价格
                case MSG_REFRESH_PRICE:
                    ShoppingCarModal.this.adapter.notifyDataSetChanged();
                    Bundle data = msg.getData();
                    final_total_price = data.getFloat("total_price");
                    final_total_number = data.getInt("number");
                    //ShoppingCarModal.this.totalPrice.set(String.format(ShoppingCarModal.this.context.getString(R.string.str_shop_car_total_price),final_total_price));
                    break;
                    //增加价格
                case MSG_PLUS_PRICE:
                    Bundle data1 = msg.getData();
                    final_total_price += data1.getFloat("total_price");
                    final_total_number += data1.getInt("number");
                    //ShoppingCarModal.this.totalPrice.set(String.format(ShoppingCarModal.this.context.getString(R.string.str_shop_car_total_price),final_total_price));
                    break;
                    //减少价格
                case MSG_MINUS_PRICE:
                    Bundle data2 = msg.getData();
                    final_total_price -= data2.getFloat("total_price");
                    final_total_number -= data2.getInt("number");
                    //ShoppingCarModal.this.totalPrice.set(String.format(ShoppingCarModal.this.context.getString(R.string.str_shop_car_total_price),final_total_price));
                    break;
                    //全选
                case MSG_ALL_CHECK:
                    isAllCheck = true;
                    Bundle data3 = msg.getData();
                    final_total_price = data3.getFloat("total_price");
                    final_total_number = data3.getInt("number");
                    binding.cbShoppingCarAll.setOnCheckedChangeListener(null);
                    binding.cbShoppingCarAll.setChecked(true);
                    binding.cbShoppingCarAll.setOnCheckedChangeListener(checkedChangeListener);
                    break;
            }
            totalPrice.set(String.format(context.getString(R.string.str_shop_car_total_price),final_total_price));
            totalNumber.set(String.format(context.getString(R.string.str_make_order_now),final_total_number));
        }
    }

    //这是自定义的全选监听
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isAllCheck = isChecked;
            if(adapter.getItems().size()==0)
                return;
            ShoppingCarModal.this.baseDialog.showProgress("请稍后");
            if(isChecked){
                //计算总价,开启子线程
                new Thread(){
                    @Override
                    public void run() {
                        //说明有数据
                        float price = 0.00f;
                        int num = 0;
                        for(ShopCarItemEntity itemEntity:adapter.getItems()){
                            price+=(Float.parseFloat(itemEntity.getSku().getSalePrice())*itemEntity.getNum());
                            num+=itemEntity.getNum();
                            itemEntity.isCheck = true;
                        }
                        Message msg = Message.obtain();
                        msg.what = MyHandler.MSG_REFRESH_PRICE;
                        Bundle bundle = new Bundle();
                        bundle.putFloat("total_price",price);
                        bundle.putInt("number",num);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }.start();
            }else{
                //不计算总价
                new Thread(){
                    @Override
                    public void run() {
                        //说明有数据
                        float price = 0.00f;
                        int num = 0;
                        for(ShopCarItemEntity itemEntity:adapter.getItems()){
                            itemEntity.isCheck = false;
                        }
                        Message msg = Message.obtain();
                        msg.what = MyHandler.MSG_REFRESH_PRICE;
                        Bundle bundle = new Bundle();
                        bundle.putFloat("total_price",price);
                        bundle.putInt("number",num);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        }
    };

    //编辑事件
    public void onEditClick(View view){
        if(isEdit.get()){
            //如果是编辑，取消编辑状态，还原所有的状态
            isEdit.set(false);
            totalPrice.set(String.format(context.getString(R.string.str_shop_car_total_price),final_total_price));
            totalNumber.set(String.format(context.getString(R.string.str_make_order_now),final_total_number));
            if(adapter.getItems()==null||adapter.getItems().isEmpty())
                binding.cbShoppingCarAll.setChecked(false);
        }else{
            //不是编辑，进入编辑状态
            isEdit.set(true);
        }
    }


    //这是删除的变量
    private float deletePrice = 0.00f;
    private int deleteNumber = 0;
    //删除事件
    public void onDeleteClick(View view){
        //删除事件，先统计出需要删除的商品
        final Vector<ShopCarItemEntity> ids = new Vector<>();
        //执行删除的方法，因为考虑到可能是多选，所以拼接id
        final StringBuilder builder = new StringBuilder();
        for(ShopCarItemEntity entity:adapter.getItems()){
            if(entity.isCheck){
                ids.add(entity);
                deleteNumber+=entity.getNum();
                deletePrice += (entity.getNum()*Float.parseFloat(entity.getSku().getSalePrice()));
                builder.append(entity.getId()).append(",");
            }
        }

        if(deleteNumber==0){
            ToastUtils.showShortToast(context,"请选择要删除的商品");
            return;
        }
        DeleteShopCarItemDialog dialog = new DeleteShopCarItemDialog();
        dialog.setOnClickListener(new DeleteShopCarItemDialog.OnClickListener() {
            @Override
            public void onClick(boolean isOk) {
                if(!isOk){
                    //清空变量信息
                    deletePrice = 0;
                    deleteNumber = 0;
                }else{
                    HttpUtils.getInstance().deleteShoppingCarItem(builder.substring(0,builder.length()-1))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                @Override
                                public void onSuccess(String t) {

                                    for(ShopCarItemEntity entity:ids){
                                        adapter.getItems().remove(entity);
                                    }
                                    //减少价格
                                    Message msg = Message.obtain();
                                    msg.what = MyHandler.MSG_MINUS_PRICE;
                                    Bundle bundle = new Bundle();
                                    bundle.putFloat("total_price",deletePrice);
                                    bundle.putInt("number",deleteNumber);
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);

                                    if(adapter.getItems()==null||adapter.getItems().isEmpty())
                                        hasItems.set(false);

                                    //清空变量信息
                                    deletePrice = 0;
                                    deleteNumber = 0;
                                }

                                @Override
                                public void onFail(ExceptionReason msg) {

                                }
                            });
                }
            }
        });
        dialog.show(context,deleteNumber);
    }


    //结算操作
    public void onSaveClick(View view){
        ArrayList<ShopCarItemEntity> finalData = new ArrayList<>();

        //结算操作
        for(ShopCarItemEntity entity : adapter.getItems()){
            //进行逻辑操作哦
            if(entity.isCheck){
                //先判断是否可以购买
                if(entity.getSpu().getSpuStatus()==0||entity.getSku().getStatus()==0){
                    //不可购买
                    ToastUtils.showShortToast(context,"清单中包含不可购买的商品");
                    return;
                }

                if(entity.getNum()>entity.getSku().getDepositNum()){
                    //就说明库存不足
                    ToastUtils.showShortToast(context,"清单中包含库存不足的商品");
                    return;
                }
                finalData.add(entity);
            }
        }

        //结算
        if(finalData.isEmpty())
            return;
        Intent intent = new Intent(context, FillOrderActivity.class);
        intent.putExtra("list",finalData);
        context.startActivity(intent);
    }

}
