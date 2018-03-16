package com.cloudcreativity.wankeshop.main.fragment;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;
import com.cloudcreativity.wankeshop.utils.GlideUtils;
import com.cloudcreativity.wankeshop.databinding.ItemHomeGoodsListItemBinding;
import java.util.List;

/**
 * 首页第一个页面的Adapter
 */
public class HomeStartAdapter extends BaseAdapter {
    private Context context;
    private List<GoodsEntity> entities;
    private LinearLayout.LayoutParams params;
    HomeStartAdapter(Context context,List<GoodsEntity> entities) {
        this.context = context;
        this.entities = entities;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int gridWidth = (int) ((metrics.widthPixels-30*metrics.density)/2);
        params = new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHomeGoodsListItemBinding binding;
        if(convertView==null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_home_goods_list_item,parent,false);
        }else{
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setGoodsItem(entities.get(position));
        binding.getRoot().setLayoutParams(params);
        return binding.getRoot();
    }


    @BindingAdapter("homeGoodsPic")
    public static void display(ImageView imageView,String url){
        GlideUtils.load(imageView.getContext(),url,imageView);
    }
}
