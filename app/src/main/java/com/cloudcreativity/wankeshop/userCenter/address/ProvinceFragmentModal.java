package com.cloudcreativity.wankeshop.userCenter.address;

/**
 * 这是省的Fragment ViewModal
 */

import android.app.Activity;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.databinding.FragmentProvinceBinding;
import com.cloudcreativity.wankeshop.databinding.ItemProvinceFragmentLayoutBinding;
import com.cloudcreativity.wankeshop.entity.address.CityEntity;
import com.cloudcreativity.wankeshop.entity.address.ProvinceEntity;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProvinceFragmentModal {


    public BaseBindingRecyclerViewAdapter<ProvinceEntity,ItemProvinceFragmentLayoutBinding> provinceAdapter;

    private Activity context;
    private ProvinceFragment fragment;
    private FragmentProvinceBinding binding;

    private ProvinceEntity lastSelectEntity;
    private int lastPosition;

    ProvinceFragmentModal(Activity context, ProvinceFragment fragment, FragmentProvinceBinding binding) {
        this.context = context;
        this.fragment = fragment;
        this.binding = binding;

        provinceAdapter = new BaseBindingRecyclerViewAdapter<ProvinceEntity, ItemProvinceFragmentLayoutBinding>(this.context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_province_fragment_layout;
            }

            @Override
            protected void onBindItem(ItemProvinceFragmentLayoutBinding binding, final ProvinceEntity item, final int position) {
                binding.setProvince(item);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick(item,position);
                    }
                });
            }
        };

        if(TempAddress.provinceEntities!=null&&!TempAddress.provinceEntities.isEmpty())
            provinceAdapter.getItems().addAll(TempAddress.provinceEntities);
    }

    //刷新数据
    public void refreshData(){
        provinceAdapter.getItems().clear();
        if(TempAddress.provinceEntities!=null&&!TempAddress.provinceEntities.isEmpty())
            provinceAdapter.getItems().addAll(TempAddress.provinceEntities);
    }

    //条目点击事件
    private void onItemClick(ProvinceEntity entity,int position){

        //相同的点击没反应
        if(entity==lastSelectEntity)
            return;

        //清空省以下的数据
        TempAddress.cityEntities.clear();
        TempAddress.cityEntity = null;
        TempAddress.areaEntities.clear();
        TempAddress.areaEntity = null;
        TempAddress.streetEntities.clear();
        TempAddress.streetEntity = null;


        if(lastSelectEntity!=null){
            lastSelectEntity.setCheck(false);
            provinceAdapter.notifyItemChanged(lastPosition);
        }
        entity.setCheck(true);
        lastSelectEntity = entity;
        lastPosition = position;
        provinceAdapter.notifyItemChanged(position);

        //将当前的省保存
        TempAddress.provinceEntity = entity;

        //加载市的列表
        HttpUtils.getInstance().getCities(TempAddress.provinceEntity.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(fragment,true) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<List<CityEntity>>() {
                        }.getType();
                        List<CityEntity> cityEntities = new Gson().fromJson(t,type);
                        if(cityEntities==null||cityEntities.isEmpty()){
                            ToastUtils.showShortToast(context,R.string.str_no_data);
                        }else{
                            TempAddress.cityEntities = cityEntities;
                            //显示市的页面
                            EventBus.getDefault().post(AddressChooseActivity.MSG_DISPLAY_CITY);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
}
