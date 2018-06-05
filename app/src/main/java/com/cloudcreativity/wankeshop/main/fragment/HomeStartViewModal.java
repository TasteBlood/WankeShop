package com.cloudcreativity.wankeshop.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.base.CommonWebActivity;
import com.cloudcreativity.wankeshop.databinding.FragmentHomeStartBinding;
import com.cloudcreativity.wankeshop.databinding.ItemHoneNavLayoutBinding;
import com.cloudcreativity.wankeshop.entity.BannerEntity;
import com.cloudcreativity.wankeshop.entity.ChanelWebEntity;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;
import com.cloudcreativity.wankeshop.entity.GoodsWrapper;
import com.cloudcreativity.wankeshop.entity.HomeNavEntity;
import com.cloudcreativity.wankeshop.entity.HomeNavWrapper;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.cloudcreativity.wankeshop.goods.GoodsListActivity;
import com.cloudcreativity.wankeshop.goods.GoodsListForBannerActivity;
import com.cloudcreativity.wankeshop.main.HomeFragment;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.cloudcreativity.wankeshop.utils.UpdateManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是首页第一个页面的ViewModal
 */
public class HomeStartViewModal {

    private HomeStartFragment fragment;

    private BaseDialogImpl dialogImpl;

    private Context context;

    private FragmentHomeStartBinding binding;

    private int pageNum = 1;
    public BaseBindingRecyclerViewAdapter<HomeNavEntity,ItemHoneNavLayoutBinding> navAdapter;
    private int gridWidth;

    public HomeStartRecyclerAdapter goodsAdapter;

    private List<GoodsEntity> entities = new ArrayList<>();

    private UserEntity userEntity;

    //这是刷新加载的事件监听
    public RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(TwinklingRefreshLayout refreshLayout) {
            //检查APP热更新
            UpdateManager.checkVersion(context,dialogImpl);

            //刷新首页的数据
            EventBus.getDefault().post(HomeFragment.MSG_REFRESH);

            pageNum = 1;
            loadBanner();
            loadNavMenuItems();
            loadGoodsList(pageNum);
        }

        @Override
        public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
            loadGoodsList(pageNum);
        }
    };

    HomeStartViewModal(BaseDialogImpl dialogImpl,Context context,FragmentHomeStartBinding binding,HomeStartFragment fragment) {
        this.dialogImpl = dialogImpl;
        this.context = context;
        this.binding = binding;
        this.fragment = fragment;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        //根据内外边距计算出gridWidth
        gridWidth = (int) ((metrics.widthPixels-20*metrics.density)/4);

        navAdapter = new BaseBindingRecyclerViewAdapter<HomeNavEntity, ItemHoneNavLayoutBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_hone_nav_layout;
            }

            @Override
            protected void onBindItem(final ItemHoneNavLayoutBinding binding, final HomeNavEntity item, int position) {
                //在这里动态设置高宽
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                binding.getRoot().setLayoutParams(params);
                binding.setNavItem(item);
                binding.ivNew.setVisibility(View.GONE);
                if(userEntity!=null){
                    if("公告".equals(item.getName())){
                        if(userEntity.getIsNotice()==1){
                            binding.ivNew.setVisibility(View.VISIBLE);
                        }else{
                            binding.ivNew.setVisibility(View.GONE);
                        }

                        binding.getRoot().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onNavItemClick(item);
                                //在这里进行数据请求，修改状态
                                updateState(userEntity.getIsNews(),userEntity.getIsRecruit(),userEntity.getIsFinance(),0);
                                binding.ivNew.setVisibility(View.GONE);
                            }
                        });

                    }else if("招聘".equals(item.getName())){
                        if(userEntity.getIsRecruit()==1){
                            binding.ivNew.setVisibility(View.VISIBLE);
                        }else{
                            binding.ivNew.setVisibility(View.GONE);
                        }

                        binding.getRoot().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onNavItemClick(item);
                                //在这里进行数据请求，修改状态
                                updateState(userEntity.getIsNews(),0,userEntity.getIsFinance(),userEntity.getIsNotice());
                                binding.ivNew.setVisibility(View.GONE);
                            }
                        });

                    }else if("新闻".equals(item.getName())){
                        if(userEntity.getIsNews()==1){
                            binding.ivNew.setVisibility(View.VISIBLE);
                        }else{
                            binding.ivNew.setVisibility(View.GONE);
                        }

                        binding.getRoot().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onNavItemClick(item);
                                //在这里进行数据请求，修改状态
                                updateState(0,userEntity.getIsRecruit(),userEntity.getIsFinance(),userEntity.getIsNotice());
                                binding.ivNew.setVisibility(View.GONE);
                            }
                        });

                    }else if("金融".equals(item.getName())){
                        if(userEntity.getIsFinance()==1){
                            binding.ivNew.setVisibility(View.VISIBLE);
                        }else{
                            binding.ivNew.setVisibility(View.GONE);
                        }

                        binding.getRoot().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onNavItemClick(item);
                                //在这里进行数据请求，修改状态
                                updateState(userEntity.getIsNews(),userEntity.getIsRecruit(),0,userEntity.getIsNotice());
                                binding.ivNew.setVisibility(View.GONE);
                            }
                        });

                    }else{
                        binding.getRoot().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                binding.ivNew.setVisibility(View.GONE);
                                onNavItemClick(item);
                            }
                        });
                    }
                }


            }
        };

        //模拟添加假数据，加载头布局
        entities.add(new GoodsEntity());

        goodsAdapter = new HomeStartRecyclerAdapter(entities,this);
    }

    //初始化加载数据
    public void initialLoadData(){
        pageNum = 1;
        loadBanner();
        loadNavMenuItems();
        loadGoodsList(pageNum);
    }
    //这是活动的点击事件
    public void onActivityClick(View view){

    }

    //加载banner
    private void loadBanner(){
        HttpUtils.getInstance().getCarouseList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(dialogImpl,false) {
                    @Override
                    public void onSuccess(String t) {
                        binding.refreshHomeStart.finishRefreshing();
                        fragment.initialLoadDataSuccess();
                        Type type = new TypeToken<List<BannerEntity>>() {
                        }.getType();
                        final List<BannerEntity> bannerEntities = new Gson().fromJson(t,type);
                        //开始加载banner图
                       try{
                           goodsAdapter.getStartTopBinding().homeTopBanner.setPages(new CBViewHolderCreator() {
                               @Override
                               public Object createHolder() {
                                   return new BannerImageHolder();
                               }
                           },bannerEntities)
                                   //设置指示器是否可见
                                   .setPointViewVisible(true)
                                   //设置自动切换（同时设置了切换时间间隔）
                                   .startTurning(2000)
                                   //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                                   .setPageIndicator(new int[]{R.drawable.banner_dot_5dp_normal, R.drawable.banner_dot_5dp})
                                   //设置指示器的方向（左、中、右）
                                   .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                                   //设置点击监听事件
                                   .setOnItemClickListener(new OnItemClickListener() {
                                       @Override
                                       public void onItemClick(int position) {
                                           onBannerClick(bannerEntities.get(position));
                                       }
                                   })
                                   //设置手动影响（设置了该项无法手动切换）
                                   .setManualPageable(true);

                       }catch (Exception e){

                       }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //加载导航操作菜单
    private void loadNavMenuItems(){
        HttpUtils.getInstance().getHomeNavList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(dialogImpl,false) {
                    @Override
                    public void onSuccess(String t) {
                        HomeNavWrapper homeNavWrapper = new Gson().fromJson(t, HomeNavWrapper.class);
                        if(homeNavWrapper.getResultlist()!=null&&!homeNavWrapper.getResultlist().isEmpty()){
                            //展示nav数据
                            userEntity = homeNavWrapper.getUser();
                            navAdapter.getItems().clear();
                            navAdapter.getItems().addAll(homeNavWrapper.getResultlist());
                        }else{
                            navAdapter.getItems().clear();
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //加载商品列表
    private void loadGoodsList(final int pageNum){
        HttpUtils.getInstance().getHomeGoodsList(pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(dialogImpl,false) {
                    @Override
                    public void onSuccess(String t) {

                        GoodsWrapper goodsWrapper = new Gson().fromJson(t, GoodsWrapper.class);
                        if(goodsWrapper.getData()==null||goodsWrapper.getData().isEmpty()){
                            ToastUtils.showShortToast(context, R.string.str_no_data);
                            if(HomeStartViewModal.this.pageNum==1){
                                binding.refreshHomeStart.finishRefreshing();
                                entities.clear();
                                entities.add(new GoodsEntity());
                            }else{
                                binding.refreshHomeStart.finishLoadmore();
                            }
                        }else{
                            //添加数据
                            if(pageNum==1){
                                entities.clear();
                                entities.add(new GoodsEntity());
                                binding.refreshHomeStart.finishRefreshing();
                            }else{
                                binding.refreshHomeStart.finishLoadmore();
                            }
                            entities.addAll(goodsWrapper.getData());
                            HomeStartViewModal.this.pageNum++;
                        }
                        goodsAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(HomeStartViewModal.this.pageNum==1){
                            binding.refreshHomeStart.finishRefreshing();
                        }else{
                            binding.refreshHomeStart.finishLoadmore();
                        }
                    }
                });
    }

    @BindingAdapter("imageNavUrl")
    public static void loadImage(ImageView imageView,String url){
        GlideUtils.load(imageView.getContext(),url,imageView);
    }

    //这是navItem点击事件
    private void onNavItemClick(final HomeNavEntity entity){
        //在这里进行数据请求
        HttpUtils.getInstance().getChanelContent(1,entity.getStyle(),entity.getSort())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(dialogImpl,true) {
                    @Override
                    public void onSuccess(String t) {
                        if("0".equals(entity.getStyle())){
                            //跳转到h5页面
                            ChanelWebEntity webEntity = new Gson().fromJson(t, ChanelWebEntity.class);
                            CommonWebActivity.startActivity(context,webEntity.getTypeName(),webEntity.getUrl());
                        }else if("2".equals(entity.getStyle())){
                            //商户信息

                        }else{
                            //商品信息
                            GoodsWrapper goodsWrapper = new Gson().fromJson(t, GoodsWrapper.class);
                            Intent intent = new Intent(context, GoodsListActivity.class);
                            intent.putExtra("style",entity.getStyle());
                            intent.putExtra("type",entity.getSort());
                            intent.putExtra("title",entity.getName());
                            intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) goodsWrapper.getData());
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //这是banner点击事件
    private void onBannerClick(final BannerEntity entity){
        HttpUtils.getInstance().getBannerContent(1,entity.getKeyWords(),String.valueOf(entity.getThirdClassId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(dialogImpl,true) {
                    @Override
                    public void onSuccess(String t) {
                        GoodsWrapper wrapper = new Gson().fromJson(t, GoodsWrapper.class);
                        if(wrapper.getData()!=null||!wrapper.getData().isEmpty()){
                            //跳转到商品列表界面
                            Intent intent = new Intent(context, GoodsListForBannerActivity.class);
                            intent.putExtra("title",entity.getTitle());
                            intent.putExtra("keyWords",entity.getKeyWords());
                            intent.putExtra("thirdClassId",String.valueOf(entity.getThirdClassId()));
                            intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) wrapper.getData());
                            context.startActivity(intent);
                        }else{
                            ToastUtils.showShortToast(context,R.string.str_no_data);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //更新点击nav item的状态
    private void updateState(int newsState,int recruitState,int financeState,int noticeState){
        HttpUtils.getInstance().updateUserForIsNew(noticeState,newsState,recruitState,financeState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(dialogImpl,false) {
                    @Override
                    public void onSuccess(String t) {

                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

}
