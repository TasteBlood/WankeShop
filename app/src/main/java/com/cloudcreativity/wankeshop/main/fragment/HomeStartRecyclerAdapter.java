package com.cloudcreativity.wankeshop.main.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.databinding.ItemHomeGoodsListItemBinding;
import com.cloudcreativity.wankeshop.databinding.LayoutHomeStartTopBinding;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;
import com.cloudcreativity.wankeshop.goods.GoodsDetailActivity;

import java.util.List;

/**
 * 这是自定义的adapter，添加头布局
 */
public class HomeStartRecyclerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_HEADER =0x10086;

    private List<GoodsEntity> entities;
    private HomeStartViewModal modal;
    private LayoutHomeStartTopBinding startTopBinding;
    private LinearLayout.LayoutParams params;
    public LayoutHomeStartTopBinding getStartTopBinding() {
        return startTopBinding;
    }

    HomeStartRecyclerAdapter(List<GoodsEntity> entityList, HomeStartViewModal modal) {
        this.entities = entityList;
        this.modal = modal;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER){
            //返回头布局
            this.startTopBinding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_start_top,parent,false);
            this.startTopBinding.rcvHomeStartNav.setLayoutManager(new GridLayoutManager(parent.getContext(),4, LinearLayoutManager.VERTICAL,false));
            this.startTopBinding.setHomeViewModal(this.modal);
            return new MyHeaderHolder(startTopBinding);
        }else{
            //返回普通数据布局
            ItemHomeGoodsListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_home_goods_list_item,parent,false);
            return new MyGoodsHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(position==0){
            //直接绑定头布局数据

        }else{
            MyGoodsHolder goodsHolder = (MyGoodsHolder) holder;
            goodsHolder.binding.setGoodsItem(entities.get(position));
            goodsHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),GoodsDetailActivity.class);
                    intent.putExtra("spuId",entities.get(position).getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return TYPE_HEADER;
        return super.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    int viewType = getItemViewType(position);
                    if (viewType==TYPE_HEADER)
                    {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    class MyGoodsHolder extends RecyclerView.ViewHolder{

        ItemHomeGoodsListItemBinding binding;
        MyGoodsHolder(ItemHomeGoodsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class MyHeaderHolder extends RecyclerView.ViewHolder{

        LayoutHomeStartTopBinding binding;
        MyHeaderHolder(LayoutHomeStartTopBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
