package com.cloudcreativity.wankeshop.entity;

import java.io.Serializable;

/**
 * 这是申请成为物流小二的数据POJO
 */
public class ApplyLogisticsEntity implements Serializable {

    private int areaId;//区县id

    private String areaName;//区县名称

    private String carNum;//车牌号码

    private String carPhoto1;//车辆照片1

    private String carPhoto2;//车辆照片2

    private String carPhoto3;//车辆照片3

    private int carType;//车辆类型

    private int cityId;//市id

    private String cityName;//市名称

    private String createTime;

    private String driverLicenseBack;//驾驶证背面

    private String driverLicenseFront;//驾驶证正面

    private String emerConMobile;//紧急联系人手机

    private String emerConName;//紧急联系人姓名

    private int id;//id

    private String idCard;//身份证号

    private String idCardBack;//身份证背面照

    private String idCardFront;//身份证正面照

    private int linceColor;//车牌颜色

    private String mobile;//手机号

    private String name;//姓名

    private String operateLicenseBack;//营运证背面照

    private String operateLicenseFront;//营运证正面照

    private int provinceId;//省id

    private String provinceName;//省名称

    private String situationLicenseBack;//行驶证背面照

    private String situationLicenseFront;//行驶证正面照

    private int state;//审核状态

    private String updateTime;//更新时间

    private int userId;//用户Id

    public void setAreaId(int areaId){
        this.areaId = areaId;
    }
    public int getAreaId(){
        return this.areaId;
    }
    public void setAreaName(String areaName){
        this.areaName = areaName;
    }
    public String getAreaName(){
        return this.areaName;
    }
    public void setCarNum(String carNum){
        this.carNum = carNum;
    }
    public String getCarNum(){
        return this.carNum;
    }
    public void setCarPhoto1(String carPhoto1){
        this.carPhoto1 = carPhoto1;
    }
    public String getCarPhoto1(){
        return this.carPhoto1;
    }
    public void setCarPhoto2(String carPhoto2){
        this.carPhoto2 = carPhoto2;
    }
    public String getCarPhoto2(){
        return this.carPhoto2;
    }
    public void setCarPhoto3(String carPhoto3){
        this.carPhoto3 = carPhoto3;
    }
    public String getCarPhoto3(){
        return this.carPhoto3;
    }
    public void setCarType(int carType){
        this.carType = carType;
    }
    public int getCarType(){
        return this.carType;
    }
    public void setCityId(int cityId){
        this.cityId = cityId;
    }
    public int getCityId(){
        return this.cityId;
    }
    public void setCityName(String cityName){
        this.cityName = cityName;
    }
    public String getCityName(){
        return this.cityName;
    }
    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setDriverLicenseBack(String driverLicenseBack){
        this.driverLicenseBack = driverLicenseBack;
    }
    public String getDriverLicenseBack(){
        return this.driverLicenseBack;
    }
    public void setDriverLicenseFront(String driverLicenseFront){
        this.driverLicenseFront = driverLicenseFront;
    }
    public String getDriverLicenseFront(){
        return this.driverLicenseFront;
    }
    public void setEmerConMobile(String emerConMobile){
        this.emerConMobile = emerConMobile;
    }
    public String getEmerConMobile(){
        return this.emerConMobile;
    }
    public void setEmerConName(String emerConName){
        this.emerConName = emerConName;
    }
    public String getEmerConName(){
        return this.emerConName;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setIdCard(String idCard){
        this.idCard = idCard;
    }
    public String getIdCard(){
        return this.idCard;
    }
    public void setIdCardBack(String idCardBack){
        this.idCardBack = idCardBack;
    }
    public String getIdCardBack(){
        return this.idCardBack;
    }
    public void setIdCardFront(String idCardFront){
        this.idCardFront = idCardFront;
    }
    public String getIdCardFront(){
        return this.idCardFront;
    }
    public void setLinceColor(int linceColor){
        this.linceColor = linceColor;
    }
    public int getLinceColor(){
        return this.linceColor;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }
    public String getMobile(){
        return this.mobile;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setOperateLicenseBack(String operateLicenseBack){
        this.operateLicenseBack = operateLicenseBack;
    }
    public String getOperateLicenseBack(){
        return this.operateLicenseBack;
    }
    public void setOperateLicenseFront(String operateLicenseFront){
        this.operateLicenseFront = operateLicenseFront;
    }
    public String getOperateLicenseFront(){
        return this.operateLicenseFront;
    }
    public void setProvinceId(int provinceId){
        this.provinceId = provinceId;
    }
    public int getProvinceId(){
        return this.provinceId;
    }
    public void setProvinceName(String provinceName){
        this.provinceName = provinceName;
    }
    public String getProvinceName(){
        return this.provinceName;
    }
    public void setSituationLicenseBack(String situationLicenseBack){
        this.situationLicenseBack = situationLicenseBack;
    }
    public String getSituationLicenseBack(){
        return this.situationLicenseBack;
    }
    public void setSituationLicenseFront(String situationLicenseFront){
        this.situationLicenseFront = situationLicenseFront;
    }
    public String getSituationLicenseFront(){
        return this.situationLicenseFront;
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
