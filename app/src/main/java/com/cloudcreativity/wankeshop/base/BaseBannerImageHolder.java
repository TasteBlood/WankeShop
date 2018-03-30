package com.cloudcreativity.wankeshop.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.entity.BannerEntity;
import com.cloudcreativity.wankeshop.utils.GlideUtils;

/**
 * 这是没有标题的banner
 */
public class BaseBannerImageHolder implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        GlideUtils.load(context,data,imageView);
    }
}
