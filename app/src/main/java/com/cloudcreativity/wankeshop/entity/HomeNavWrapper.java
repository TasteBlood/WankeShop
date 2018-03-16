package com.cloudcreativity.wankeshop.entity;

import java.util.List;

/**
 * 这是首页导航菜单item数据包装
 */
public class HomeNavWrapper {
    private List<HomeNavEntity> resultlist ;

    private int totalrecord;

    public void setResultlist(List<HomeNavEntity> resultlist){
        this.resultlist = resultlist;
    }
    public List<HomeNavEntity> getResultlist(){
        return this.resultlist;
    }
    public void setTotalrecord(int totalrecord){
        this.totalrecord = totalrecord;
    }
    public int getTotalrecord(){
        return this.totalrecord;
    }
}
