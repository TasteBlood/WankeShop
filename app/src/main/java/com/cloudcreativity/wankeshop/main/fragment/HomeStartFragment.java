package com.cloudcreativity.wankeshop.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentHomeStartBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutHomeStartTopBinding;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是首页的默认Fragment，其他的都是动态加载的Fragment
 */
public class HomeStartFragment extends LazyFragment {
    private HomeStartViewModal viewModal;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentHomeStartBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_start,container,false);
        viewModal = new HomeStartViewModal(this,context);
        binding.setHomeStartModal(viewModal);
        LayoutHomeStartTopBinding startTopBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_home_start_top,container,false);
        startTopBinding.setHomeViewModal(viewModal);
        binding.gvHomeStart.addHeaderView(startTopBinding.getRoot());
        //初始化Banner
        final List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.page1);
        list.add(R.mipmap.page2);
        list.add(R.mipmap.page3);
        startTopBinding.homeTopBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerImageHolder();
            }
        },list).setPointViewVisible(true).startTurning(3000).
                setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicator(new int[]{R.drawable.banner_dot_5dp_normal,R.drawable.banner_dot_5dp});


        binding.refreshHomeStart.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                },2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                    }
                },2000);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
