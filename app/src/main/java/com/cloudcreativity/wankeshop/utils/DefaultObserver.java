package com.cloudcreativity.wankeshop.utils;

import android.text.TextUtils;

import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class DefaultObserver<T extends BaseResult> implements Observer<T> {
    private BaseDialogImpl impl;
    protected DefaultObserver(BaseDialogImpl impl){
        this.impl = impl;
    }
    protected DefaultObserver(BaseDialogImpl impl ,boolean isShowProgress){
        this.impl = impl;
        if(isShowProgress)
            impl.showProgress("加载中...");
    }

    @Override
    public void onSubscribe(Disposable d) {
        impl.addRxDestroy(d);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e(this.getClass().getName(), TextUtils.isEmpty(e.getMessage())?"出错啦":e.getMessage());
        impl.dismissProgress();
        if(e instanceof HttpException){
            onFail(ExceptionReason.BAD_NETWORK);
        }else if(e instanceof ConnectException||e instanceof UnknownHostException){
            onFail(ExceptionReason.CONNECT_ERROR);
        }else if(e instanceof InterruptedException){
            onFail(ExceptionReason.CONNECT_TIMEOUT);
        }else if(e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException){
            onFail(ExceptionReason.PARSE_ERROR);
        }else{
            onFail(ExceptionReason.UNKNOWN_ERROR);
        }
    }
    @Override
    public void onNext(T t) {
        impl.dismissProgress();
        //这里是模仿该账号是否在其他地方登录了
        if(t.getCode()==10086){
            impl.showUserAuthOutDialog();
        }else{
            onSuccess(t);
        }
    }
    public abstract void onSuccess(T t);
    public abstract void onFail(ExceptionReason msg);

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
