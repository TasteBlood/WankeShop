package com.cloudcreativity.wankeshop.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudcreativity.wankeshop.R;

/**
 * Glide加载图片的自定义
 */
public class GlideUtils {
    /**
     *
     * @param context 上下文
     * @param url 网络图片或者本地文件
     * @param imageView 目标控件
     */
    public static void loadCircle(Context context, String url, final ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .transform(new GlideCircleTrasform()))
                .into(imageView);
    }

    /**
     *
     * @param context 上下文
     * @param resource 本地的资源id
     * @param imageView 目标控件
     */
    public static void loadCircle(Context context,  int resource, final ImageView imageView){
        Glide.with(context)
                .load(resource)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .transform(new GlideCircleTrasform()))
                .into(imageView);
    }

    /**
     *
     * @param context 上下文
     * @param url 路径
     * @param imageView 目标控件
     */
    public static void load(Context context, String url, final ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher))
                .into(imageView);
    }
}
