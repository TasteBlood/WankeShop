package com.cloudcreativity.wankeshop.entity;

import java.util.List;

/**
 * 这是购物车的包装
 */
public class ShopCarItemWrapper {
    private List<ShopCarItemEntity> data;

    public List<ShopCarItemEntity> getData() {
        return data;
    }

    public void setData(List<ShopCarItemEntity> data) {
        this.data = data;
    }
}
