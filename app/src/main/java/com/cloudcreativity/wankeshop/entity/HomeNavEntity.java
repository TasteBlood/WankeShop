package com.cloudcreativity.wankeshop.entity;

/**
 * 这是首页导航菜单item数据
 */
public class HomeNavEntity {
    private String createTime;

    private String icon;

    private int id;

    private String name;

    private String sort;

    private String style;

    private String updateTime;

    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setIcon(String icon){
        this.icon = icon;
    }
    public String getIcon(){
        return this.icon;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setSort(String sort){
        this.sort = sort;
    }
    public String getSort(){
        return this.sort;
    }
    public void setStyle(String style){
        this.style = style;
    }
    public String getStyle(){
        return this.style;
    }
    public void setUpdateTime(String updateTime){
        this.updateTime = updateTime;
    }
    public String getUpdateTime(){
        return this.updateTime;
    }

}
