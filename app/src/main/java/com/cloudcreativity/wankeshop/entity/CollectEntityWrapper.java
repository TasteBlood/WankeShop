package com.cloudcreativity.wankeshop.entity;

import java.util.List;

/**
 * 这是收藏的数据包装
 */
public class CollectEntityWrapper {
    private List<CollectEntity> data;

    public List<CollectEntity> getData() {
        return data;
    }

    public void setData(List<CollectEntity> data) {
        this.data = data;
    }
}
