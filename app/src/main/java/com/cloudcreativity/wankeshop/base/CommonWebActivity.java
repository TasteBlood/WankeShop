package com.cloudcreativity.wankeshop.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.utils.LogUtils;

/**
 * 这是最基本的加载web网页界面
 */
public class CommonWebActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApp.app.addActivity(this);
        setContentView(R.layout.activity_common_web);
        findViewById(R.id.ib_close).setOnClickListener(this);
        findViewById(R.id.ib_previous).setOnClickListener(this);
        webView = findViewById(R.id.wv_content);
        TextView tv_title = findViewById(R.id.tv_title);
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        tv_title.setText(title);
        initWebView();
        webView.loadUrl(url);
    }

    private void initWebView() {

        final WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持插件
        //webSettings.setPluginsEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能

        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                LogUtils.e("xuxiwu",request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                LogUtils.e("xuxiwu",url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                switch (error.getErrorCode()){
                    case 404:
                        break;
                    case 500:
                        break;
                    case 400:
                        break;
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {

            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_BACK){
                    if(webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });


    }

    public static void startActivity(Context context ,String title, String url) {
        Intent intent = new Intent(context,CommonWebActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    public static void startActivity(Context context,String url) {
        Intent intent = new Intent(context,CommonWebActivity.class);
        intent.putExtra("title","");
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_close:
                finish();
                break;
            case R.id.ib_previous:
                if (webView.canGoBack())
                    webView.goBack();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.pauseTimers();
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();

        ((ViewGroup) webView.getParent()).removeView(webView);
        webView.destroy();
        webView = null;

    }
}
