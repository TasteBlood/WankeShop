package com.cloudcreativity.wankeshop.main;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentHomeBinding;
import com.cloudcreativity.wankeshop.entity.HomeCategoryEntity;
import com.cloudcreativity.wankeshop.main.fragment.HomeSimpleFragment;
import com.cloudcreativity.wankeshop.main.fragment.HomeStartFragment;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是HomeFragment的数据模型
 */
public class HomeFragmentModal {

    private Context context;
    private BaseDialogImpl baseDialog;

    private HomeFragment fragment;
    private FragmentHomeBinding binding;

    public HomePagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> pageTitles = new ArrayList<>();

    HomeFragmentModal(Context context, BaseDialogImpl baseDialog, HomeFragment fragment,FragmentHomeBinding binding) {
        this.context = context;
        this.baseDialog = baseDialog;
        this.fragment = fragment;
        this.binding = binding;

        pageTitles.add("首页");
        fragments.add(new HomeStartFragment());
        pagerAdapter = new HomePagerAdapter(fragment.getChildFragmentManager(),fragments,pageTitles);

        loadData();
    }

    /**
     * 加载首页的类别数据，并且动态生成页面，并且检查热更新
     */
    public void loadData(){

        HttpUtils.getInstance().getHomeCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<List<HomeCategoryEntity>>() {
                        }.getType();
                        List<HomeCategoryEntity> entities = new Gson().fromJson(t,type);
                        pageTitles.clear();
                        fragments.clear();
                        pageTitles.add("首页");
                        fragments.add(new HomeStartFragment());
                        if(entities!=null&&!entities.isEmpty()){
                            for(HomeCategoryEntity entity : entities){
                                pageTitles.add(entity.getLevel1().getCategoryName());
                                fragments.add(HomeSimpleFragment.getInstance(entity.getLevel1(),entity.getLevel2()));
                            }
                        }
                        pagerAdapter.notifyDataSetChanged();
                        binding.homeViewPager.setOffscreenPageLimit(fragments.size());
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    /**
     *
     * @return 返回当前的首页类目标题
     */
    public List<String> getPageTitles() {
        return pageTitles;
    }
}
