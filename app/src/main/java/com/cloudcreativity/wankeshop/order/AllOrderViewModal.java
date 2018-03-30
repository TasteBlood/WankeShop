package com.cloudcreativity.wankeshop.order;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.cloudcreativity.wankeshop.money.RechargeFragment;
import com.cloudcreativity.wankeshop.order.fragment.CancelFragment;
import com.cloudcreativity.wankeshop.order.fragment.FinishFragment;
import com.cloudcreativity.wankeshop.order.fragment.ReturnOrderFragment;
import com.cloudcreativity.wankeshop.order.fragment.WaitPayFragment;
import com.cloudcreativity.wankeshop.order.fragment.WaitReceiveFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部订单ViewModal
 */
public class AllOrderViewModal {

    private AllOrderActivity context;

    private String[] titles = {"待付款","待收货","已完成","已取消","退货"};

    private List<Fragment> fragments = new ArrayList<>();

    public FragmentPagerAdapter pagerAdapter;

    AllOrderViewModal(AllOrderActivity context) {
        this.context = context;
        fragments.add(new WaitPayFragment());
        fragments.add(new WaitReceiveFragment());
        fragments.add(new FinishFragment());
        fragments.add(new CancelFragment());
        fragments.add(new ReturnOrderFragment());

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
                return titles[position];
            }
        };

    }

    public void onBack(View view){
        context.finish();
    }
}
