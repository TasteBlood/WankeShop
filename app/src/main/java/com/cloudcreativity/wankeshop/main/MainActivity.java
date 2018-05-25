package com.cloudcreativity.wankeshop.main;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.receiver.MyBusinessReceiver;
import com.cloudcreativity.wankeshop.utils.UpdateManager;

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

    //自定义的业务广播接收者
    private MyBusinessReceiver businessReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在这里注册自定义的广播
        businessReceiver = new MyBusinessReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyBusinessReceiver.ACTION_EXIT_APP);
        filter.addAction(MyBusinessReceiver.ACTION_RE_LOGIN);
        filter.addAction(MyBusinessReceiver.ACTION_LOGOUT);
        registerReceiver(businessReceiver,filter);


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁广播
        unregisterReceiver(businessReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //这里是扫码的结果
        if(requestCode==MineFragmentModal.REQUEST_SCAN)
            fragments.get(3).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(currentId!=0){
                RadioButton rbMainHome = findViewById(R.id.rbMainHome);
                rbMainHome.setChecked(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(Manifest.permission.CALL_PHONE==permissions[0]){
            fragments.get(3).onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }
}
