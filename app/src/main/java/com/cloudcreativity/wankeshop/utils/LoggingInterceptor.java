package com.cloudcreativity.wankeshop.utils;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 网络请求的日志打印工具
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        LogUtils.i(this.getClass().getName(),String.format("发送请求 %s on %s%n%s",request.url(),chain.connection(),request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        ResponseBody peekBody = response.peekBody(1024 * 1024);
        LogUtils.i(this.getClass().getName(),
                String.format("接受响应:[%s] %n返回json:[%s] %.1fms%n%s",
                        response.request().url(),
                        peekBody.string(),
                        (t2-t1)/1e6d,
                        response.headers()));
        return chain.proceed(chain.request());
    }
}
