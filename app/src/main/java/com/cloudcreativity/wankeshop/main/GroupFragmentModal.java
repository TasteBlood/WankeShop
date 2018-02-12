package com.cloudcreativity.wankeshop.main;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.FragmentGroupBinding;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是拼团的ViewModal
 */
public class GroupFragmentModal {

    private GroupFragment groupFragment;
    private FragmentGroupBinding binding;

    GroupFragmentModal(GroupFragment fragment, FragmentGroupBinding binding) {
        this.groupFragment = fragment;
        this.binding = binding;
        this.binding.refreshGroup.setOnRefreshListener(refreshListenerAdapter);
    }

    //刷新加载事件
    private RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.finishRefreshing();
                }
            },3000);
        }

        @Override
        public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.finishLoadmore();
                }
            },3000);
        }
    };

    //这是筛选按钮的点击事件
    public void onDropTabClick(View view){
        switch (view.getId()){
                //类别
            case R.id.ll_groupCategoryDropDown:
                break;
                //品牌
            case R.id.ll_groupLabelDropDown:
                break;
                //排序
            case R.id.ll_groupSortDropDown:
                break;
        }
    }

    //显示dropDialog
    private void showDropWindow(){

    }

    //隐藏dropDialog
    private void dismissWindow(){

    }
}
