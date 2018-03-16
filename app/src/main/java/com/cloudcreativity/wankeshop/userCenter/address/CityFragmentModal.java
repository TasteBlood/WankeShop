package com.cloudcreativity.wankeshop.userCenter.address;

/**
 * 这是市的Fragment ViewModal
 */

import android.app.Activity;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.databinding.FragmentCityBinding;
import com.cloudcreativity.wankeshop.databinding.ItemCityFragmentLayoutBinding;
import com.cloudcreativity.wankeshop.entity.address.AreaEntity;
import com.cloudcreativity.wankeshop.entity.address.CityEntity;
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

public class CityFragmentModal {


    public BaseBindingRecyclerViewAdapter<CityEntity,ItemCityFragmentLayoutBinding> cityAdapter;

    private Activity context;
    private CityFragment fragment;
    private FragmentCityBinding binding;

    private CityEntity lastSelectEntity;
    private int lastPosition;

    CityFragmentModal(Activity context, CityFragment fragment, FragmentCityBinding binding) {
        this.context = context;
        this.fragment = fragment;
        this.binding = binding;
        cityAdapter = new BaseBindingRecyclerViewAdapter<CityEntity, ItemCityFragmentLayoutBinding>(this.context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_city_fragment_layout;
            }

            @Override
            protected void onBindItem(ItemCityFragmentLayoutBinding binding, final CityEntity item, final int position) {
                binding.setCity(item);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick(item,position);
                    }
                });
            }
        };
        if(TempAddress.cityEntities!=null&&!TempAddress.cityEntities.isEmpty())
            cityAdapter.getItems().addAll(TempAddress.cityEntities);
    }

    public void refreshData(){
        cityAdapter.getItems().clear();
        if(TempAddress.cityEntities!=null&&!TempAddress.cityEntities.isEmpty())
            cityAdapter.getItems().addAll(TempAddress.cityEntities);
    }

    //条目点击事件
    private void onItemClick(CityEntity entity,int position){
        //相同的点击没反应
        if(entity==lastSelectEntity)
            return;
        if(lastSelectEntity!=null){
            lastSelectEntity.setCheck(false);
            cityAdapter.notifyItemChanged(lastPosition);
        }
        entity.setCheck(true);
        lastSelectEntity = entity;
        lastPosition = position;
        cityAdapter.notifyItemChanged(position);
        //LogUtils.e("xuxiwu","position="+position+"--lastPos="+lastPosition);

        //将当前的市保存
        TempAddress.cityEntity = entity;

        //清空市一下的数据
        TempAddress.areaEntities = null;
        TempAddress.areaEntity = null;
        TempAddress.streetEntities = null;
        TempAddress.streetEntity = null;

        //加载区的列表
        HttpUtils.getInstance().getAreas(entity.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(fragment,true) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<List<AreaEntity>>() {
                        }.getType();
                        List<AreaEntity> areaEntities = new Gson().fromJson(t,type);
                        if(areaEntities==null||areaEntities.isEmpty()){
                            ToastUtils.showShortToast(context,R.string.str_no_data);
                        }else{
                            TempAddress.areaEntities = areaEntities;
                            //显示区的页面
                            EventBus.getDefault().post(AddressChooseActivity.MSG_DISPLAY_AREA);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
}
