package com.cloudcreativity.wankeshop.goods;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.LazyFragment;
import com.cloudcreativity.wankeshop.databinding.FragmentGoodsDetailBinding;

/**
 * 这是商品详情fragment
 */
public class GoodsDetailFragment extends LazyFragment {

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentGoodsDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods_detail,container,false);

        assert getArguments() != null;
        String desc = getArguments().getString("desc");
        WebSettings settings = binding.wvGoodsDetail.getSettings();

        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        binding.wvGoodsDetail.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
               String data = "javascript:(function(){" +
                       "var all = document.getElementsByTagName(\"img\");" +
                       "if(!all)" +
                       "return;" +
                       "for(var i=0;i<all.length;i++){" +
                       "    all[i].style.width=\"100%\";" +
                       "    all[i].style.height=\"auto\"" +
                       "}" +
                       "})()";
               binding.wvGoodsDetail.loadUrl(data);
            }
        });
        binding.wvGoodsDetail.loadData(TextUtils.isEmpty(desc)?"暂无商品详情":desc,"text/html;charset=UTF-8","utf-8");
        return binding.getRoot();
    }

    public static GoodsDetailFragment getInstance(String goodsDesc){
        Bundle bundle = new Bundle();
        bundle.putString("desc",goodsDesc);
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initialLoadData() {

    }
}
