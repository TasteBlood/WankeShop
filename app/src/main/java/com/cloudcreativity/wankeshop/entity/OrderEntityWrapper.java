package com.cloudcreativity.wankeshop.entity;

import java.util.List;

/**
 * 这是单个小订单的包装
 */
public class OrderEntityWrapper {
    private List<OrderEntity> data;

    public List<OrderEntity> getData() {
        return data;
    }

    public void setData(List<OrderEntity> data) {
        this.data = data;
    }
}
