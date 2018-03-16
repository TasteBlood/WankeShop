package com.cloudcreativity.wankeshop.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.GridDecoration;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentHomeStartBinding;

/**
 * 这是首页的默认Fragment，其他的都是动态加载的Fragment
 */
public class HomeStartFragment extends LazyFragment {
    private HomeStartViewModal viewModal;
    private FragmentHomeStartBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_start,container,false);
        viewModal = new HomeStartViewModal(this,context,binding,this);
        binding.refreshHomeStart.setOnRefreshListener(viewModal.refreshListenerAdapter);
        binding.rcvHomeStartMain.setLayoutManager(new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false));
        binding.rcvHomeStartMain.addItemDecoration(new GridDecoration(context,
                5,
                context.getResources().getColor(R.color.transparent)) {
            @Override
            public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
                boolean[] booleans = {false, false, false, false};
                if (itemPosition != 0) {
                    //因为给 RecyclerView 添加了 header，所以原本的 position 发生了变化
                    //position 为 0 的地方实际上是 header，真正的列表 position 从 1 开始
                    switch (itemPosition % 2) {
                        case 0:
                            //每一行第二个只显示左边距和下边距
                            booleans[0] = true;
                            booleans[3] = true;
                            break;
                        case 1:
                            //每一行第一个显示右边距和下边距
                            booleans[2] = true;
                            booleans[3] = true;
                            break;
                    }
                }
                return booleans;
            }
        });
        binding.setHomeStartModal(viewModal);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
        viewModal.refreshListenerAdapter.onRefresh(binding.refreshHomeStart);
    }
}
