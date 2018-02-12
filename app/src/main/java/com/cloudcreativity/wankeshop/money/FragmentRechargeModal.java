package com.cloudcreativity.wankeshop.money;

import android.content.Context;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentRechargeBinding;
import com.cloudcreativity.wankeshop.databinding.ItemMoneyListLayoutBinding;
import com.cloudcreativity.wankeshop.entity.MoneyEntity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是充值记录Fragment ViewModal
 */
public class FragmentRechargeModal {
    private BaseDialogImpl baseDialog;
    private Context context;
    private FragmentRechargeBinding binding;
    private LazyFragment lazyFragment;

    private int pageNum = 1;
    private int pageSize = 15;


    FragmentRechargeModal(BaseDialogImpl baseDialog, Context context, FragmentRechargeBinding binding,LazyFragment fragment) {
        this.baseDialog = baseDialog;
        this.context = context;
        this.binding = binding;
        this.lazyFragment = fragment;
        this.binding.refreshRecharge.setRefreshHeader(new BezierRadarHeader(this.context));
        this.binding.refreshRecharge.setRefreshFooter(new BallPulseFooter(this.context));
        this.binding.refreshRecharge.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData(pageNum);
            }
        });

        this.binding.refreshRecharge.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadData(pageNum);
            }
        });

        adapter = new BaseBindingRecyclerViewAdapter<MoneyEntity, ItemMoneyListLayoutBinding>(this.context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_money_list_layout;
            }

            @Override
            protected void onBindItem(ItemMoneyListLayoutBinding binding, MoneyEntity item, int position) {
                binding.setItem(item);
            }
        };
    }

    //加载数据
    private void loadData(final int page){
        //load data by page
        HttpUtils.getInstance().getMoneyList(SPUtils.get().getUid(),SPUtils.get().getToken(),1,page,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        binding.refreshRecharge.finishLoadMore();
                        binding.refreshRecharge.finishRefresh();
                        lazyFragment.initialLoadDataSuccess();
                        //处理数据
                        Type type = new TypeToken<List<MoneyEntity>>() {
                        }.getType();

                        List<MoneyEntity> moneyEntities = new Gson().fromJson(t,type);
                        if(moneyEntities==null||moneyEntities.isEmpty()){
                            ToastUtils.showShortToast(context,R.string.str_no_data);
                            if(page!=1){

                            }
                        }else{
                            if(page==1){
                                lazyFragment.initialLoadDataSuccess();
                                adapter.getItems().clear();
                                adapter.getItems().addAll(moneyEntities);
                            }else{
                                adapter.getItems().addAll(moneyEntities);
                            }
                            pageNum++;
                        }
                    }
                    @Override
                    public void onFail(ExceptionReason msg) {
                        binding.refreshRecharge.finishLoadMore();
                        binding.refreshRecharge.finishRefresh();
                    }
                });
    }

    public BaseBindingRecyclerViewAdapter<MoneyEntity,ItemMoneyListLayoutBinding> adapter;

}
