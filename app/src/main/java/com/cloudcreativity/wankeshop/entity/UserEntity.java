package com.cloudcreativity.wankeshop.entity;

import android.databinding.Bindable;

/**
 * 这是用户数据
 */
public class UserEntity{
    private String birthDay;
    private String createTime;
    private String email;
    private String headPic;
    private int id;
    private String idCard;
    private String loginTime;
    private String mobile;
    private String password;
    private String rank;
    private String realName;
    private String regDate;
    private int sex;
    private String token;
    private int type;
    private String updateTime;
    private String userName;
    private String balance;
    private int isBind;//是否绑定微信
    private String areaId;
    private String areaName;
    private String cityId;
    private String cityName;
    private String provinceId;
    private String provinceName;

    private int returnOrderNum;//这是退换货的数量
    private int unOrderNum;//这是待收货的数量


    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public int getReturnOrderNum() {
        return returnOrderNum;
    }

    public void setReturnOrderNum(int returnOrderNum) {
        this.returnOrderNum = returnOrderNum;
    }

    public int getUnOrderNum() {
        return unOrderNum;
    }

    public void setUnOrderNum(int unOrderNum) {
        this.unOrderNum = unOrderNum;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "UserEntity{" +
                "birthDay='" + birthDay + '\'' +
                ", createTime='" + createTime + '\'' +
                ", email='" + email + '\'' +
                ", headPic='" + headPic + '\'' +
                ", id=" + id +
                ", idCard='" + idCard + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", rank='" + rank + '\'' +
                ", realName='" + realName + '\'' +
                ", regDate='" + regDate + '\'' +
                ", sex=" + sex +
                ", token='" + token + '\'' +
                ", type=" + type +
                ", updateTime='" + updateTime + '\'' +
                ", userName='" + userName + '\'' +
                ", balance='" + balance + '\'' +
                ", isBind=" + isBind +
                ", areaId='" + areaId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", returnOrderNum=" + returnOrderNum +
                ", unOrderNum=" + unOrderNum +
                '}';
    }
}
