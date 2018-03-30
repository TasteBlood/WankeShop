package com.cloudcreativity.wankeshop.entity;

/**
 * 这是收藏的数据实体
 */
public class CollectEntity {
    private int collectId;
    private String createTime;
    private int id;
    //private ShopEntity shop;
    private GoodsEntity spu;
    private int type;
    private String updateTime;
    private int userId;

    public int getCollectId() {
        return collectId;
    }

    public void setCollectId(int collectId) {
        this.collectId = collectId;
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

    public GoodsEntity getSpu() {
        return spu;
    }

    public void setSpu(GoodsEntity spu) {
        this.spu = spu;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
