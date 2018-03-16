package com.cloudcreativity.wankeshop.entity;

import java.util.List;

/**
 * 这是商品数据的包装，获取列表
 */
public class GoodsWrapper {
    private List<GoodsEntity> data;
    private int totalPage;

    public List<GoodsEntity> getData() {
        return data;
    }

    public void setData(List<GoodsEntity> data) {
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
