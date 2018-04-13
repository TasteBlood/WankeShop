package com.cloudcreativity.wankeshop.entity;

import java.io.Serializable;

/**
 * 这是每一个商品订单数据
 */
public class OrderEntity implements Serializable{
    private int addressId;

    private String createTime;

    private int id;

    private String onlyId;

    private String orderNum;

    private String payMoney;

    private String payNum;

    private int payState;

    private int payWay;

    private int shipState;

    private int shopId;

    private ShopCarItemEntity shoppingCart;

    private int shoppingCartId;

    private int state;

    private String updateTime;

    private int userId;

    private int isDelay;//是否延长收货

    public int getIsDelay() {
        return isDelay;
    }

    public void setIsDelay(int isDelay) {
        this.isDelay = isDelay;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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

    public String getOnlyId() {
        return onlyId;
    }

    public void setOnlyId(String onlyId) {
        this.onlyId = onlyId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public int getShipState() {
        return shipState;
    }

    public void setShipState(int shipState) {
        this.shipState = shipState;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public ShopCarItemEntity getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShopCarItemEntity shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public int getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(int shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
