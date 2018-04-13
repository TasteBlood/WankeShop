package com.cloudcreativity.wankeshop.main.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.android.databinding.library.baseAdapters.BR;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentHomeSimpleBinding;
import com.cloudcreativity.wankeshop.databinding.ItemHomeCategoryLayoutBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutHomeCategoryDropdownBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;
import com.cloudcreativity.wankeshop.entity.GoodsWrapper;
import com.cloudcreativity.wankeshop.entity.LevelEntity;
import com.cloudcreativity.wankeshop.goods.GoodsDetailActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.cloudcreativity.wankeshop.databinding.ItemHomeGoodsListItemBinding;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是首页其他Fragment的ViewModal
 */
public class HomeSimpleViewModal {
    private BaseDialogImpl baseDialog;
    private HomeSimpleFragment fragment;
    private Context context;
    private FragmentHomeSimpleBinding binding;

    private LevelEntity firstLevel;
    private List<LevelEntity> secondLevels;

    //这是页面的UI变量
    public BaseBindingRecyclerViewAdapter<GoodsEntity,ItemHomeGoodsListItemBinding> adapter;
    public ObservableField<LevelEntity> category = new ObservableField<>();//这是分类
    public ObservableField<Boolean> hasData = new ObservableField<>();//这是数据状态


    private boolean sortPrice = true;// true  升序  false 降序
    private boolean sortSale = true;//true 升序  false 降序
    private String keyWords = "";//关键字  默认为空
    private int secondId = 0;//这是二级分类的id
    private int pageNum = 1;//页码

    HomeSimpleViewModal(BaseDialogImpl baseDialog, HomeSimpleFragment fragment, Context context, FragmentHomeSimpleBinding binding,
                        LevelEntity firstLevel, final List<LevelEntity> secondLevels) {
        this.baseDialog = baseDialog;
        this.fragment = fragment;
        this.context = context;
        this.binding = binding;

        this.firstLevel = firstLevel;
        this.secondLevels = secondLevels;

        this.binding.refreshHomeSimple.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(pageNum,secondLevels.get(0).getId(),1,1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(pageNum,secondLevels.get(0).getId(),1,1);
            }
        });

        //初始化二级分类，默认加入全部字段
        LevelEntity entity = new LevelEntity();
        entity.setCategoryName("全 部");
        this.secondLevels.add(0,entity);
        this.secondId = 0;
        this.category.set(this.secondLevels.get(0));

        adapter = new BaseBindingRecyclerViewAdapter<GoodsEntity, ItemHomeGoodsListItemBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_home_goods_list_item;
            }

            @Override
            protected void onBindItem(ItemHomeGoodsListItemBinding binding, final GoodsEntity item, int position) {
                binding.setGoodsItem(item);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,GoodsDetailActivity.class);
                        intent.putExtra("spuId",item.getId());
                        context.startActivity(intent);
                    }
                });
            }
        };

        hasData.set(true);

        this.binding.etKeywords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWords = s.toString();
            }
        });
    }

    //当搜索点击
    public void onSearchClick(View view){
        keyWords = binding.etKeywords.getText().toString().trim();
        loadData(pageNum,secondId,sortPrice?1:2,sortSale?1:2);
    }

    //当价格排序点击
    public void onPriceSortClick(View view){
        pageNum  =1;
        sortPrice = !sortPrice;
        binding.ivPriceState.setImageResource(sortPrice?R.mipmap.ic_sort_up:R.mipmap.ic_sort_down);
        loadData(pageNum,secondId,sortPrice?1:2,sortSale?1:2);
    }

    //当销量排序点击
    public void onSaleSortClick(View view){
        pageNum = 1;
        sortSale = !sortSale;
        binding.ivSaleState.setImageResource(sortSale?R.mipmap.ic_sort_up:R.mipmap.ic_sort_down);
        loadData(pageNum,secondId,sortPrice?1:2,sortSale?1:2);
    }

    //当类别被点击
    public void onCategoryClick(View view){
        displayDropDown();
    }

    /**
     * 检索数据
     * @param page 当前的页码
     * @param secondId 第二分类的id
     * @param sortByPrice 根据价格排序  升序 还是降序
     * @param sortBySale 根据销量排序 升序 还是降序
     */
    private void loadData(final int page, int secondId, int sortByPrice, int sortBySale){
        HttpUtils.getInstance().getHomeGoodsByCategory(page,firstLevel.getId(),secondId,sortByPrice,sortBySale,keyWords)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        GoodsWrapper wrapper = new Gson().fromJson(t, GoodsWrapper.class);
                        if(wrapper.getData()==null||wrapper.getData().isEmpty()){
                            if(page==1){
                                fragment.initialLoadDataSuccess();
                                hasData.set(false);
                                binding.refreshHomeSimple.finishRefreshing();
                                adapter.getItems().clear();
                            }else{
                                binding.refreshHomeSimple.finishLoadmore();
                            }
                        }else{
                            if(page==1){
                                fragment.initialLoadDataSuccess();
                                hasData.set(true);
                                binding.refreshHomeSimple.finishRefreshing();
                                adapter.getItems().clear();
                            }else{
                                binding.refreshHomeSimple.finishLoadmore();
                            }
                            adapter.getItems().addAll(wrapper.getData());
                            pageNum++;
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshHomeSimple.finishRefreshing();
                        }else{
                            binding.refreshHomeSimple.finishLoadmore();
                        }
                    }
                });
    }

    //显示下拉框
    private PopupWindow categoryWindow;
    private void displayDropDown(){
        if(categoryWindow!=null&&categoryWindow.isShowing()){
            categoryWindow.dismiss();
            categoryWindow = null;
            return;
        }
        categoryWindow = new PopupWindow(context);
        LayoutHomeCategoryDropdownBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_home_category_dropdown,null,false);
        binding.rcvHomeCategory.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.divider_line_1dp_grayf1f1f1));
        binding.rcvHomeCategory.addItemDecoration(itemDecoration);
        BaseBindingRecyclerViewAdapter<LevelEntity,ItemHomeCategoryLayoutBinding> levelAdapter = new BaseBindingRecyclerViewAdapter<LevelEntity,ItemHomeCategoryLayoutBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_home_category_layout;
            }

            @Override
            protected void onBindItem(ItemHomeCategoryLayoutBinding binding, final LevelEntity item, int position) {
                binding.setLevel(item);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        category.set(item);
                        secondId = item.getId();
                        categoryWindow.dismiss();
                        //还原所有的状态
                        pageNum = 1;
                        HomeSimpleViewModal.this.binding.ivSaleState.setImageResource(R.mipmap.ic_sort_up);
                        HomeSimpleViewModal.this.binding.ivPriceState.setImageResource(R.mipmap.ic_sort_up);
                        sortPrice = true;
                        sortSale = true;
                        loadData(pageNum,secondId,1,1);
                    }
                });
            }
        };
        levelAdapter.getItems().addAll(secondLevels);
        binding.rcvHomeCategory.setAdapter(levelAdapter);

        categoryWindow.setContentView(binding.getRoot());
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        categoryWindow.setWidth(metrics.widthPixels/3*2);
        categoryWindow.setHeight(metrics.heightPixels/2);

        categoryWindow.setOutsideTouchable(true);

        categoryWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        categoryWindow.showAsDropDown(HomeSimpleViewModal.this.binding.layoutCategory,0,10);
    }

    //初始化加载数据
    public void initialData(){
        loadData(pageNum,secondId,sortPrice?1:2,sortSale?1:2);
    }


}
