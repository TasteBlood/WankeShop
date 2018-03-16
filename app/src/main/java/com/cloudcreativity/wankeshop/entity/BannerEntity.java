package com.cloudcreativity.wankeshop.entity;

/**
 * 这是轮播图的数据
 */
public class BannerEntity {
    private int cityId;

    private String cityName;

    private String createTime;

    private int id;

    private String keyWords;

    private String picUrl;

    private int sort;

    private int thirdClassId;

    private String thirdClassName;

    private String title;

    private String updateTime;

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
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setKeyWords(String keyWords){
        this.keyWords = keyWords;
    }
    public String getKeyWords(){
        return this.keyWords;
    }
    public void setPicUrl(String picUrl){
        this.picUrl = picUrl;
    }
    public String getPicUrl(){
        return this.picUrl;
    }
    public void setSort(int sort){
        this.sort = sort;
    }
    public int getSort(){
        return this.sort;
    }
    public void setThirdClassId(int thirdClassId){
        this.thirdClassId = thirdClassId;
    }
    public int getThirdClassId(){
        return this.thirdClassId;
    }
    public void setThirdClassName(String thirdClassName){
        this.thirdClassName = thirdClassName;
    }
    public String getThirdClassName(){
        return this.thirdClassName;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setUpdateTime(String updateTime){
        this.updateTime = updateTime;
    }
    public String getUpdateTime(){
        return this.updateTime;
    }
}
