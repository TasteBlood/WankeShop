package com.cloudcreativity.wankeshop.entity.address;

/**
 * 省份
 */
public class ProvinceEntity {
    private int id;
    private String name;

    private boolean isCheck;//这是自定义的变量，是为了实现选中的效果
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
