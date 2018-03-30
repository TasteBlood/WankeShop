package com.cloudcreativity.wankeshop.main;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.widget.ImageView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentGroupBinding;
import com.cloudcreativity.wankeshop.entity.ShopEntity;
import com.cloudcreativity.wankeshop.entity.ShopWrapper;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import com.cloudcreativity.wankeshop.databinding.ItemShopLayoutBinding;
/**
 * 这是商铺的ViewModal
 */
public class GroupFragmentModal {

    private GroupFragment groupFragment;
    private Context context;
    private FragmentGroupBinding binding;
    private BaseDialogImpl baseDialog;
    private int pageNum = 1;

    public BaseBindingRecyclerViewAdapter<ShopEntity,ItemShopLayoutBinding> adapter;

    //这是是否有商铺信息
    public ObservableField<Boolean> hasShop = new ObservableField<>();

    GroupFragmentModal(GroupFragment fragment, FragmentGroupBinding binding,Context context) {
        this.groupFragment = fragment;
        this.baseDialog = fragment;
        this.context = context;
        this.binding = binding;
        this.binding.refreshGroup.setOnRefreshListener(refreshListenerAdapter);
        this.binding.refreshGroup.startRefresh();
        hasShop.set(true);

        adapter = new BaseBindingRecyclerViewAdapter<ShopEntity, ItemShopLayoutBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_shop_layout;
            }

            @Override
            protected void onBindItem(ItemShopLayoutBinding binding, ShopEntity item, int position) {
                binding.setShop(item);
            }
        };
    }

    //刷新加载事件
    private RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
            pageNum = 1;
            loadData(pageNum);
        }

        @Override
        public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
            loadData(pageNum);
        }
    };

    /**
     *
     * @param page 页码
     *             加载数据
     */
    private void loadData(final int page){
        HttpUtils.getInstance().getShops(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        ShopWrapper wrapper = new Gson().fromJson(t, ShopWrapper.class);
                        groupFragment.initialLoadDataSuccess();
                        if(wrapper.getData()==null||wrapper.getData().isEmpty()){
                            if(page==1){
                                adapter.getItems().clear();
                                binding.refreshGroup.finishRefreshing();
                                hasShop.set(false);
                            }else{
                                binding.refreshGroup.finishLoadmore();
                                ToastUtils.showShortToast(context, R.string.str_no_data);
                            }
                        }else{
                            if(page==1){
                                binding.refreshGroup.finishRefreshing();
                                adapter.getItems().clear();
                                adapter.getItems().addAll(wrapper.getData());
                            }else{
                                binding.refreshGroup.finishLoadmore();
                                adapter.getItems().addAll(wrapper.getData());
                            }
                            pageNum++;
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshGroup.finishRefreshing();
                        }else{
                            binding.refreshGroup.finishLoadmore();
                        }
                    }
                });
    }

    @BindingAdapter("shopIcon")
    public static void displayIcon(ImageView imageView,String url){
        GlideUtils.loadCircle(imageView.getContext(),url,imageView);
    }

    @BindingAdapter("shopPic")
    public static void displayPic(ImageView imageView, String url){
        GlideUtils.load(imageView.getContext(),url,imageView);
    }
}
