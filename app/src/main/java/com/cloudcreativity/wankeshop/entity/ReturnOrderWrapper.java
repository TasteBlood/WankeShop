package com.cloudcreativity.wankeshop.entity;

import java.util.List;

/**
 * 这是退货的包装类
 */
public class ReturnOrderWrapper {

    private List<ReturnOrderEntity> data;

    public List<ReturnOrderEntity> getData() {
        return data;
    }

    public void setData(List<ReturnOrderEntity> data) {
        this.data = data;
    }
}
