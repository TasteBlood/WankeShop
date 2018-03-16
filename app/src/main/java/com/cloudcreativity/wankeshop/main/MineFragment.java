package com.cloudcreativity.wankeshop.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentMineBinding;
import com.cloudcreativity.wankeshop.utils.SPUtils;

/**
 * 这是我的Fragment
 */
public class MineFragment extends LazyFragment {

    private MineFragmentModal mineFragmentModal;
    private FragmentMineBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mine,container,false);
        binding.setMineModal(mineFragmentModal = new MineFragmentModal(context,binding,this));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mineFragmentModal.user = SPUtils.get().getUser();
        binding.invalidateAll();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MineFragmentModal.REQUEST_SCAN){
            if(resultCode==MineFragmentModal.RESULT_OK){
                //这里就是扫描后的结果
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("qr_scan_result");
                mineFragmentModal.dealScanCode(scanResult);
            }
        }
    }
}
