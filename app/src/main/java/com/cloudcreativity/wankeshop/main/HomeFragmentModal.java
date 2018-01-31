package com.cloudcreativity.wankeshop.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.wankeshop.main.fragment.HomeStartFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是HomeFragment的数据模型
 */
public class HomeFragmentModal {
    public HomePagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> pageTitles = new ArrayList<>();
    HomeFragmentModal(FragmentManager manager){
        pageTitles.add("首页");
        fragments.add(new HomeStartFragment());
        pagerAdapter = new HomePagerAdapter(manager,fragments,pageTitles);

        //剩余的继续自己添加
        pageTitles.add("服饰");
        pageTitles.add("鞋类");
        pageTitles.add("运动旅行");
        pageTitles.add("3C数码");
        pageTitles.add("家电小家电");
        pageTitles.add("儿童母婴");
        pageTitles.add("文教办公");
        pageTitles.add("粮油副食");

        fragments.add(new HomeStartFragment());
        fragments.add(new HomeStartFragment());
        fragments.add(new HomeStartFragment());
        fragments.add(new HomeStartFragment());
        fragments.add(new HomeStartFragment());
        fragments.add(new HomeStartFragment());
        fragments.add(new HomeStartFragment());
        fragments.add(new HomeStartFragment());

        pagerAdapter.notifyDataSetChanged();
    }
}
