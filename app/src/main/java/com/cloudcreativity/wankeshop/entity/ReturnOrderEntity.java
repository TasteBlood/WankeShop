package com.cloudcreativity.wankeshop.entity;

import java.io.Serializable;

public class ReturnOrderEntity implements Serializable{
    private int addressId;

    private String createTime;

    private int id;

    private OrderEntity order;

    private int orderId;

    private String returnDes;

    private String returnMoney;

    private String returnNum;

    private int returnState;

    private int shopId;

    private int skuId;

    private int skuNum;

    private int state;

    private String updateTime;

    private int userId;

    public void setAddressId(int addressId){
        this.addressId = addressId;
    }
    public int getAddressId(){
        return this.addressId;
    }
    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setOrder(OrderEntity order){
        this.order = order;
    }
    public OrderEntity getOrder(){
        return this.order;
    }
    public void setOrderId(int orderId){
        this.orderId = orderId;
    }
    public int getOrderId(){
        return this.orderId;
    }
    public void setReturnDes(String returnDes){
        this.returnDes = returnDes;
    }
    public String getReturnDes(){
        return this.returnDes;
    }
    public void setReturnMoney(String returnMoney){
        this.returnMoney = returnMoney;
    }
    public String getReturnMoney(){
        return this.returnMoney;
    }
    public void setReturnNum(String returnNum){
        this.returnNum = returnNum;
    }
    public String getReturnNum(){
        return this.returnNum;
    }
    public void setReturnState(int returnState){
        this.returnState = returnState;
    }
    public int getReturnState(){
        return this.returnState;
    }
    public void setShopId(int shopId){
        this.shopId = shopId;
    }
    public int getShopId(){
        return this.shopId;
    }
    public void setSkuId(int skuId){
        this.skuId = skuId;
    }
    public int getSkuId(){
        return this.skuId;
    }
    public void setSkuNum(int skuNum){
        this.skuNum = skuNum;
    }
    public int getSkuNum(){
        return this.skuNum;
    }
    public void setState(int state){
        this.state = state;
    }
    public int getState(){
        return this.state;
    }
    public void setUpdateTime(String updateTime){
        this.updateTime = updateTime;
    }
    public String getUpdateTime(){
        return this.updateTime;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId(){
        return this.userId;
    }
}
