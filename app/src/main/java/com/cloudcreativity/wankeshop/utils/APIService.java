package com.cloudcreativity.wankeshop.utils;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 整个程序的网络接口配置
 */
public interface APIService {
    /**
     * 网络请求的配置
     */
    long timeOut = 10 * 1000;//网络超时
    /**
     * 整体的网关接口配置
     */
    String TEST_HOST = "http://192.168.31.19/vmall/";
    String ONLINE_HOST = "http://www.sina.com/";
    String HOST = AppConfig.DEBUG ? TEST_HOST : ONLINE_HOST;

    //登录接口
    @FormUrlEncoded
    @POST("login.do")
    Observable<String> login(@Field("mobile") String mobile,
                                 @Field("password") String password);
    //注册第一步验证手机号接口
    @FormUrlEncoded
    @POST("checkMobile.do")
    Observable<String> registerFirst(@Field("mobile") String mobile);

    //注册第二步获取验证码
    @FormUrlEncoded
    @POST("sendSms.do")
    Observable<String> registerGetVerfiry(@Field("mobile") String mobile);

    //注册第三步，提交验证码、手机号、密码进行注册
    @FormUrlEncoded
    @POST("saveUser.do")
    Observable<String> registerFinalStep(@Field("mobile") String mobile,
                                         @Field("password") String password,
                                         @Field("sms") String sms);
}
