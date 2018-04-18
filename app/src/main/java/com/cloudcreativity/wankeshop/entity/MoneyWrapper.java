package com.cloudcreativity.wankeshop.entity;

import java.util.List;

/**
 * 这是资金记录的包装类
 */
public class MoneyWrapper {
    private List<MoneyEntity> data;

    public List<MoneyEntity> getData() {
        return data;
    }

    public void setData(List<MoneyEntity> data) {
        this.data = data;
    }
}
