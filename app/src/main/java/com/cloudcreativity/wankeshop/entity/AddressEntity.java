package com.cloudcreativity.wankeshop.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * 地址
 */
public class AddressEntity extends BaseObservable implements Serializable{
    private String addressInfo;
    private String areaId;
    private String areaName;
    private String cityId;
    private String cityName;
    private String createTime;
    private int id;
    private String idNum;
    private int isDefault;
    private String provinceId;
    private String provinceName;
    private String receiptMobile;
    private String receiptName;
    private String streetId;
    private String streetName;
    private int userId;
    private String zipCode;

    @Bindable
    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    @Bindable
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Bindable
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Bindable
    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Bindable
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Bindable
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    @Bindable
    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Bindable
    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    @Bindable
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Bindable
    public String getReceiptMobile() {
        return receiptMobile;
    }

    public void setReceiptMobile(String receiptMobile) {
        this.receiptMobile = receiptMobile;
    }

    @Bindable
    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    @Bindable
    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    @Bindable
    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    @Bindable
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Bindable
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    //进行电话号码的隐藏中间四位
    public String formatPhone(){
        if(TextUtils.isEmpty(this.receiptMobile))
            return "暂无手机号";
        return this.receiptMobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }
    //获取地址信息
    public String formatAddress(){
        return this.provinceName+this.cityName+this.areaName+this.streetName+" "+this.addressInfo;
    }
    //获取省市区地址信息
    public String formatAddress2(){
        return this.provinceName+this.cityName+this.areaName+this.streetName;
    }
}
