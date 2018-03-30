package com.cloudcreativity.wankeshop.entity;

import java.util.List;

/**
 * 这是商户数据的包装
 */
public class ShopWrapper {
    private List<ShopEntity> data;

    public List<ShopEntity> getData() {
        return data;
    }

    public void setData(List<ShopEntity> data) {
        this.data = data;
    }
}
