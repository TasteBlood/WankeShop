package com.cloudcreativity.wankeshop.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.GridDividerItemDecoration;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentHomeSimpleBinding;
import com.cloudcreativity.wankeshop.entity.LevelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是首页的其他Fragment
 */

public class HomeSimpleFragment extends LazyFragment {

    private FragmentHomeSimpleBinding simpleBinding;
    private HomeSimpleViewModal simpleViewModal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        simpleBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_simple,container,false);

        simpleBinding.rcvHomeSimple.setLayoutManager(new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false));
        simpleBinding.rcvHomeSimple.addItemDecoration(new GridDividerItemDecoration(15,getResources().getColor(R.color.transparent)));

        LevelEntity firstLevel = getArguments().getParcelable("firstLevel");
        List<LevelEntity> secondLevels = getArguments().getParcelableArrayList("secondLevels");

        simpleBinding.setVariable(BR.simpleModal,simpleViewModal = new HomeSimpleViewModal(this,this,context,simpleBinding,firstLevel,secondLevels));

        return simpleBinding.getRoot();
    }

    public static synchronized HomeSimpleFragment getInstance(LevelEntity firstLevel, List<LevelEntity> secondLevels){
        HomeSimpleFragment fragment = new HomeSimpleFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("firstLevel",firstLevel);
        bundle.putParcelableArrayList("secondLevels", (ArrayList<? extends Parcelable>) secondLevels);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initialLoadData() {
       simpleBinding.refreshHomeSimple.startRefresh();
    }
}
