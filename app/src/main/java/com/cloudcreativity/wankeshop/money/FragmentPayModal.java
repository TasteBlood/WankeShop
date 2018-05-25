package com.cloudcreativity.wankeshop.money;

import android.content.Context;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentMoneyPayBinding;
import com.cloudcreativity.wankeshop.databinding.ItemMoneyPayListLayoutBinding;
import com.cloudcreativity.wankeshop.entity.MoneyEntity;
import com.cloudcreativity.wankeshop.entity.MoneyWrapper;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是支付Fragment ViewModal
 */
public class FragmentPayModal {
    private BaseDialogImpl baseDialog;
    private Context context;
    private FragmentMoneyPayBinding binding;
    private LazyFragment lazyFragment;

    private int pageNum = 1;
    private int pageSize = 15;

    FragmentPayModal(BaseDialogImpl baseDialog, Context context, FragmentMoneyPayBinding binding, LazyFragment fragment) {
        this.baseDialog = baseDialog;
        this.context = context;
        this.binding = binding;
        this.lazyFragment = fragment;
        adapter = new BaseBindingRecyclerViewAdapter<MoneyEntity, ItemMoneyPayListLayoutBinding>(this.context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_money_pay_list_layout;
            }

            @Override
            protected void onBindItem(ItemMoneyPayListLayoutBinding binding, MoneyEntity item, int position) {
                binding.setItem(item);
                if(1==item.getPayWay()){
                    binding.tvPayWay.setText("微信支付");
                }else if(2==item.getPayWay()){
                    binding.tvPayWay.setText("支付宝支付");
                }else if(3==item.getPayWay()){
                    binding.tvPayWay.setText("余额支付");
                }else{
                    binding.tvPayWay.setText("其他支付");
                }
            }
        };

        this.binding.refreshPay.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum=1;
                loadData(pageNum);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(pageNum);
            }
        });

    }

    //加载数据
    private void loadData(final int page){
        //load data by page
        HttpUtils.getInstance().getMoneyList(3,page,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        //处理数据
                        List<MoneyEntity> moneyEntities = new Gson().fromJson(t, MoneyWrapper.class).getData();
                        if(moneyEntities==null||moneyEntities.isEmpty()){
                            ToastUtils.showShortToast(context,R.string.str_no_data);
                            if(page==1){
                                binding.refreshPay.finishRefreshing();
                            }else{
                                binding.refreshPay.finishLoadmore();
                            }
                        }else{
                            if(page==1){
                                adapter.getItems().clear();
                                adapter.getItems().addAll(moneyEntities);
                                binding.refreshPay.finishRefreshing();
                                lazyFragment.initialLoadDataSuccess();
                            }else{
                                adapter.getItems().addAll(moneyEntities);
                                binding.refreshPay.finishLoadmore();
                            }
                            pageNum++;
                        }
                    }
                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshPay.finishRefreshing();
                        }else{
                            binding.refreshPay.finishLoadmore();
                        }
                    }
                });
    }

    public BaseBindingRecyclerViewAdapter<MoneyEntity,ItemMoneyPayListLayoutBinding> adapter;
}
