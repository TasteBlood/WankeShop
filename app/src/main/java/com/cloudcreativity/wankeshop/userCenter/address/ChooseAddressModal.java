package com.cloudcreativity.wankeshop.userCenter.address;

import android.support.v4.app.Fragment;
import android.view.View;

import java.util.List;

/**
 * 选择地址的Modal
 */
public class ChooseAddressModal {
    public AddressFragmentAdapter fragmentAdapter;
    private List<Fragment> fragments;
    private List<String> titles;
    private AddressChooseActivity context;

    ChooseAddressModal(List<Fragment> fragments, List<String> titles, AddressChooseActivity context) {
        this.fragments = fragments;
        this.titles = titles;
        this.context = context;
        fragmentAdapter = new AddressFragmentAdapter(context.getSupportFragmentManager(),fragments,titles);
    }

    public void onBack(View view){
        context.finish();
    }
}
