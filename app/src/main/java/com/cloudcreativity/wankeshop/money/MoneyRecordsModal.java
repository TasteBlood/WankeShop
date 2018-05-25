package com.cloudcreativity.wankeshop.money;

import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是资金记录的ViewModal
 */
public class MoneyRecordsModal {

    private MoneyRecordsActivity context;
    public MoneyPagerAdapter pagerAdapter;

    MoneyRecordsModal(MoneyRecordsActivity context) {
        this.context = context;
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        titles.add("消费记录");
        //titles.add("充值记录");
        titles.add("提现记录");

        fragments.add(new PayFragment());
        //fragments.add(new RechargeFragment());
        fragments.add(new WithDrawFragment());

        pagerAdapter = new MoneyPagerAdapter(context.getSupportFragmentManager(),fragments,titles);
    }

    public void onBack(View view){
        context.finish();
    }

}
