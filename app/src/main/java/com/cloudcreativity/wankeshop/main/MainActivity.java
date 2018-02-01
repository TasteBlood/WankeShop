package com.cloudcreativity.wankeshop.main;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.base.LazyFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {

    private static final String SAVED_CURRENT_ID = "currentId";

    public static final List<String> PAGE_TAGS = new ArrayList<>();
    //自定义的Fragment，主要目的是在初始化时能够通过循环初始化，与重建时的恢复统一
    private List<Class<? extends LazyFragment>> fragmentClasses = Arrays.asList(HomeFragment.class,
            GroupFragment.class, ShoppingCarFragment.class, MineFragment.class);
    private List<Fragment> fragments = new ArrayList<>();

    private FragmentManager mFragmentManager;

    private int currentId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PAGE_TAGS.add("home");
        PAGE_TAGS.add("group");
        PAGE_TAGS.add("shop");
        PAGE_TAGS.add("mine");
        fragments.add(new HomeFragment());
        fragments.add(new GroupFragment());
        fragments.add(new ShoppingCarFragment());
        fragments.add(new MineFragment());
        initFragments(savedInstanceState);
        RadioGroup radioGroup = findViewById(R.id.rgMainActivity);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbMainHome:
                        switchFragment(0);
                        break;
                    case R.id.rbMainGroup:
                        switchFragment(1);
                        break;
                    case R.id.rbMainShop:
                        switchFragment(2);
                        break;
                    case R.id.rbMainMine:
                        switchFragment(3);
                        break;
                }
            }
        });
        //radioGroup.getChildAt(0).performClick();
    }

    private void initFragments(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragments.size(); i++) {
            fragments.set(i, mFragmentManager.findFragmentByTag(PAGE_TAGS.get(i)));
            if(fragments.get(i) == null) {
                try {
                    fragments.set(i,fragmentClasses.get(i).newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //先重置所有fragment的状态为隐藏，彻底解决重叠问题
            if(fragments.get(i).isAdded()) {
                mFragmentManager.beginTransaction()
                        .hide(fragments.get(i))
                        .commitAllowingStateLoss();
            }
        }
        if(savedInstanceState != null) {
            int cachedId = savedInstanceState.getInt(SAVED_CURRENT_ID, 0);
            if(cachedId >= 0 && cachedId <= 4) {
                currentId = cachedId;
            }
        }
        switchFragment(currentId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_CURRENT_ID, currentId);
    }

    private void switchFragment(int index) {
        /* Fragment 切换 */
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if(fragments.get(index).isAdded()) {
            transaction.hide(fragments.get(currentId));
            transaction.show(fragments.get(index));
        }
        else {
            transaction.hide(fragments.get(currentId));
            transaction.add(R.id.mainContent, fragments.get(index), PAGE_TAGS.get(index));
            transaction.show(fragments.get(index));
        }
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commitAllowingStateLoss();
        currentId = index;
    }
}