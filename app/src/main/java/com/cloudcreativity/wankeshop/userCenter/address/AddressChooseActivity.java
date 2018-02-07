package com.cloudcreativity.wankeshop.userCenter.address;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseActivity;
import com.cloudcreativity.wankeshop.databinding.ActivityAddressChooseBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址选择
 */
public class AddressChooseActivity extends BaseActivity {
    //这是刷新页面的消息
    public static final String MSG_DISPLAY_CITY = "msg_display_city";//显示市的页面
    public static final String MSG_DISPLAY_AREA = "msg_display_area";//显示区县页面
    public static final String MSG_DISPLAY_STREET = "msg_display_street";//显示乡镇、街道的页面


    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();


    private ProvinceFragment provinceFragment;
    private CityFragment cityFragment;
    private AreaFragment areaFragment;
    private StreetFragment streetFragment;



    private ChooseAddressModal addressModal;
    private ActivityAddressChooseBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册EventBus
        EventBus.getDefault().register(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_choose);
        addressModal = new ChooseAddressModal(fragments,titles,this);
        binding.tlChooseAddress.setupWithViewPager(binding.vpChooseAddress);
        binding.vpChooseAddress.setOffscreenPageLimit(4);
        binding.setAddressModal(addressModal);
        fragments.add(provinceFragment = new ProvinceFragment());
        titles.add("请选择");
        addressModal.fragmentAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //这是接受消息

    @Subscribe
    public void onEvent(String msg){
        switch (msg){
            case MSG_DISPLAY_CITY:
                //先清空其他的数据
                fragments.clear();
                titles.clear();
                fragments.add(provinceFragment);
                titles.add(TempAddress.provinceEntity.getName());
                titles.add("请选择");

                //刷新市的数据
                if(cityFragment==null)
                    cityFragment = new CityFragment();

                fragments.add(cityFragment);
                cityFragment.refreshData();

                addressModal.fragmentAdapter.notifyDataSetChanged();
                binding.vpChooseAddress.setCurrentItem(1);
                break;
            case MSG_DISPLAY_AREA:
                //先清空别的数据
                fragments.clear();
                titles.clear();
                fragments.add(provinceFragment);
                fragments.add(cityFragment);

                //刷新区的数据
                if(areaFragment==null)
                    areaFragment = new AreaFragment();

                fragments.add(areaFragment);
                areaFragment.refreshData();

                titles.add(TempAddress.provinceEntity.getName());
                titles.add(TempAddress.cityEntity.getName());
                titles.add("请选择");

                addressModal.fragmentAdapter.notifyDataSetChanged();
                binding.vpChooseAddress.setCurrentItem(2);

                break;
            case MSG_DISPLAY_STREET:
                //先清空别的数据
                fragments.clear();
                titles.clear();

                fragments.add(provinceFragment);
                fragments.add(cityFragment);
                fragments.add(areaFragment);

                //刷新区的数据
                if(streetFragment==null)
                    streetFragment = new StreetFragment();

                fragments.add(streetFragment);
                streetFragment.refreshData();

                titles.add(TempAddress.provinceEntity.getName());
                titles.add(TempAddress.cityEntity.getName());
                titles.add(TempAddress.areaEntity.getName());
                titles.add("请选择");

                addressModal.fragmentAdapter.notifyDataSetChanged();
                binding.vpChooseAddress.setCurrentItem(3);
                break;
        }
    }
}
