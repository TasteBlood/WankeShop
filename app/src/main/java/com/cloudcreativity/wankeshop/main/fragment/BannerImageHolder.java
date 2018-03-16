package com.cloudcreativity.wankeshop.main.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.entity.BannerEntity;
import com.cloudcreativity.wankeshop.utils.GlideUtils;

public class BannerImageHolder implements Holder<BannerEntity> {
    private View layoutView;

    @Override
    public View createView(Context context) {
        layoutView = LayoutInflater.from(context).inflate(R.layout.layout_banner_item,null);
        return layoutView;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerEntity data) {
        ImageView imageview = layoutView.findViewById(R.id.iv_banner);
        TextView textview = layoutView.findViewById(R.id.tv_banner);

        textview.setText(data.getTitle());
        GlideUtils.load(context,data.getPicUrl(),imageview);
    }
}
