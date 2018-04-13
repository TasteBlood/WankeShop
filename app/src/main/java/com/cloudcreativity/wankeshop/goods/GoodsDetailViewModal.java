package com.cloudcreativity.wankeshop.goods;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.collect.GoodsCollectActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityDetailBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;
import com.cloudcreativity.wankeshop.entity.ShopEntity;
import com.cloudcreativity.wankeshop.main.ShoppingCarFragment;
import com.cloudcreativity.wankeshop.shop.ShopGoodsListActivity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是商品详情ViewModal
 */
public class GoodsDetailViewModal {

    private GoodsDetailActivity context;

    private BaseDialogImpl baseDialog;

    public FragmentPagerAdapter pagerAdapter;

    private ActivityDetailBinding binding;

    //两个Fragment
    private GoodsIndexFragment indexFragment;
    private GoodsDetailFragment detailFragment;

    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    private int spuId;
    private GoodsEntity goodsEntity;
    private ShopEntity shopEntity;

    //是否收藏
    public ObservableField<Boolean> isCollect = new ObservableField<>();

    GoodsDetailViewModal(GoodsDetailActivity context,ActivityDetailBinding binding, int spuId) {
        this.context = context;
        this.baseDialog = context;
        this.binding = binding;
        this.spuId = spuId;
        pagerAdapter = new FragmentPagerAdapter(context.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        };
        loadGoodsInfo(spuId);
        isCollect.set(false);
    }

    public void onBack(View view){
        context.finish();
    }

    public void onBottomMenuClick(View view){
        switch (view.getId()){
                //联系卖家，目前只有打电话
            case R.id.cb_goodsDetailContactShop:
                contactShop();
                break;
                //收藏商品
            case R.id.cb_goodsDetailGoodsCollect:
                collect(binding.cbGoodsDetailGoodsCollect.isChecked(),spuId);
                break;
                //店铺信息
            case R.id.cb_goodsDetailShop:
                if(shopEntity!=null){
                    Intent intent = new Intent(context,ShopGoodsListActivity.class);
                    intent.putExtra("shop",shopEntity);
                    context.startActivity(intent);
                }else{
                    if(goodsEntity!=null){
                        HttpUtils.getInstance().getShopInfo(goodsEntity.getSupplierId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                    @Override
                                    public void onSuccess(String t) {
                                        shopEntity = new Gson().fromJson(t,ShopEntity.class);
                                        Intent intent = new Intent(context,ShopGoodsListActivity.class);
                                        intent.putExtra("shop",shopEntity);
                                        context.startActivity(intent);
                                    }
                                    @Override
                                    public void onFail(ExceptionReason msg) {

                                    }
                                });
                    }
                }
                break;
                //购物车
            case R.id.cb_goodsDetailShopCar:
                Intent intent1 = new Intent(context,ShoppingCarActivity.class);
                context.startActivity(intent1);
                break;
                //添加到购物车
            case R.id.tv_goodsDetailAddToCar:
                if(indexFragment!=null){
                    Map<String, Object> goodsMap = indexFragment.onAddShopCar();
                    if(goodsMap!=null){
                        //这就是添加购物车的方法
                       HttpUtils.getInstance().addToShoppingCar(goodsMap.get("spuId").toString(),goodsMap.get("skuId").toString(),goodsMap.get("number").toString())
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                   @Override
                                   public void onSuccess(String t) {
                                        ToastUtils.showShortToast(context,"已添加到购物车");

                                        //刷新购物车
                                        EventBus.getDefault().post(ShoppingCarFragment.MSG_REFRESH_SHOP_CAR);
                                   }

                                   @Override
                                   public void onFail(ExceptionReason msg) {

                                   }
                               });
                    }
                }
                break;
        }
    }

    //获取商品详情
    private void loadGoodsInfo(final int spuId){
        HttpUtils.getInstance().getGoodsDetail(spuId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        goodsEntity = new Gson().fromJson(t, GoodsEntity.class);
                        if(goodsEntity!=null){
                            indexFragment = GoodsIndexFragment.getInstance(goodsEntity);
                            detailFragment = new GoodsDetailFragment();

                            titles.add("商品信息");
                            titles.add("详情");

                            fragments.add(indexFragment);
                            fragments.add(detailFragment);

                            pagerAdapter.notifyDataSetChanged();

                            isCollect.set(goodsEntity.getIsCollect()==1);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    private void collect(final boolean collect, final int spuId){
        if(collect){
            HttpUtils.getInstance().addCollection(2,spuId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            ToastUtils.showShortToast(context,"已收藏");
                            isCollect.set(collect);
                            //在这里发送消息
                            Map<String,Object> data = new HashMap<>();
                            data.put("msg_collect", GoodsCollectActivity.MSG_REFRESH_COLLECT);
                            EventBus.getDefault().post(data);
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {
                            isCollect.set(!collect);
                        }
                    });
        }else{
            HttpUtils.getInstance().editCollectionForOther(spuId,2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,true) {
                        @Override
                        public void onSuccess(String t) {
                            ToastUtils.showShortToast(context,"已取消收藏");
                            isCollect.set(collect);
                            //在这里发送消息
                            Map<String,Object> data = new HashMap<>();
                            data.put("msg_collect", GoodsCollectActivity.MSG_REMOVE_COLLECT_ITEM);
                            data.put("id",spuId);
                            EventBus.getDefault().post(data);
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {
                            isCollect.set(!collect);
                        }
                    });
        }
    }

    //获取商家联系方式并且联系
    private void contactShop(){
        if(goodsEntity==null)
            return;
        HttpUtils.getInstance().getShopInfo(goodsEntity.getSupplierId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onSuccess(String t) {
                        shopEntity = new Gson().fromJson(t,ShopEntity.class);
                        int result = context.checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE);
                        if(result== PackageManager.PERMISSION_DENIED){
                            context.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
                        }else{
                            call();
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    public void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel://".concat(shopEntity.getContactMobile())));
        context.startActivity(intent);
    }
}
