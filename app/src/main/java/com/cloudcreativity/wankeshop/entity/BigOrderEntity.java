package com.cloudcreativity.wankeshop.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 这是每一个大订单数据，里面可能会包含多个商品订单数据
 */
public class BigOrderEntity implements Serializable{

    private List<OrderEntity> data ;

    private String onlyId;

    public void setData(List<OrderEntity> data){
        this.data = data;
    }
    public List<OrderEntity> getData(){
        return this.data;
    }
    public void setOnlyId(String onlyId){
        this.onlyId = onlyId;
    }
    public String getOnlyId(){
        return this.onlyId;
    }
}
