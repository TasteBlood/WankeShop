package com.cloudcreativity.wankeshop.userCenter.address;

/**
 * 这是区的Fragment ViewModal
 */

import android.app.Activity;
import android.view.View;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.databinding.FragmentAreaBinding;
import com.cloudcreativity.wankeshop.databinding.ItemAreaFragmentLayoutBinding;
import com.cloudcreativity.wankeshop.entity.address.AreaEntity;
import com.cloudcreativity.wankeshop.entity.address.StreetEntity;
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

public class AreaFragmentModal {


    public BaseBindingRecyclerViewAdapter<AreaEntity,ItemAreaFragmentLayoutBinding> areaAdapter;

    private Activity context;
    private AreaFragment fragment;
    private FragmentAreaBinding binding;

    private AreaEntity lastSelectEntity;
    private int lastPosition;

    AreaFragmentModal(Activity context, AreaFragment fragment, FragmentAreaBinding binding) {
        this.context = context;
        this.fragment = fragment;
        this.binding = binding;
        areaAdapter = new BaseBindingRecyclerViewAdapter<AreaEntity, ItemAreaFragmentLayoutBinding>(this.context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_area_fragment_layout;
            }

            @Override
            protected void onBindItem(ItemAreaFragmentLayoutBinding binding, final AreaEntity item, final int position) {
                binding.setArea(item);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick(item,position);
                    }
                });
            }
        };

        if(TempAddress.areaEntities!=null&&!TempAddress.areaEntities.isEmpty())
            areaAdapter.getItems().addAll(TempAddress.areaEntities);
    }

    public void refreshData(){
        areaAdapter.getItems().clear();
        if(TempAddress.areaEntities!=null&&!TempAddress.areaEntities.isEmpty())
            areaAdapter.getItems().addAll(TempAddress.areaEntities);
    }

    //条目点击事件
    private void onItemClick(AreaEntity entity,int position){
        //相同的点击没反应
        if(entity==lastSelectEntity)
            return;
        if(lastSelectEntity!=null){
            lastSelectEntity.setCheck(false);
            areaAdapter.notifyItemChanged(lastPosition);
        }
        entity.setCheck(true);
        lastSelectEntity = entity;
        lastPosition = position;
        areaAdapter.notifyItemChanged(position);
        //LogUtils.e("xuxiwu","position="+position+"--lastPos="+lastPosition);

        //将当前的区保存
        TempAddress.areaEntity = entity;

        //清空区一下的数据
        TempAddress.streetEntity = null;
        TempAddress.streetEntities = null;

        //加载街道的列表
        HttpUtils.getInstance().getStreet(entity.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(fragment,true) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<List<StreetEntity>>() {
                        }.getType();
                        List<StreetEntity> streetEntities = new Gson().fromJson(t,type);
                        if(streetEntities==null||streetEntities.isEmpty()){
                            //在这里暂无数据，还有可能是出现了没有乡镇街道的情况，直接退出
                            context.finish();
                        }else{
                            TempAddress.streetEntities = streetEntities;
                            //显示区的页面
                            EventBus.getDefault().post(AddressChooseActivity.MSG_DISPLAY_STREET);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
}
