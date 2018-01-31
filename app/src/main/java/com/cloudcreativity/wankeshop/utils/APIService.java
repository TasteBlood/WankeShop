package com.cloudcreativity.wankeshop.utils;

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
    String TEST_HOST = "http://v5.pc.duomi.com/";
    String ONLINE_HOST = "http://www.sina.com/";
    String HOST = AppConfig.DEBUG ? TEST_HOST : ONLINE_HOST;

}
