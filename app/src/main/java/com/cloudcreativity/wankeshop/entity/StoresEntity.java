package com.cloudcreativity.wankeshop.entity;

import java.io.Serializable;

/**
 * 这是店铺实体
 */
public class StoresEntity implements Serializable{
    private String bgImg;

    private String createTime;

    private int id;

    private int rank;

    private int shopId;

    private String storesLog;

    private String storesName;

    private String updateTime;

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getStoresLog() {
        return storesLog;
    }

    public void setStoresLog(String storesLog) {
        this.storesLog = storesLog;
    }

    public String getStoresName() {
        return storesName;
    }

    public void setStoresName(String storesName) {
        this.storesName = storesName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
