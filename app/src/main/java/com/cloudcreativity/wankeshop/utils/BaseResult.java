package com.cloudcreativity.wankeshop.utils;

/**
 *
 *          这是全部请求返回数据的基类
 */
public class BaseResult{
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
