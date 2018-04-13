package com.cloudcreativity.wankeshop.entity;

import java.util.List;

/**
 * 这是首页的分类数据
 */
public class HomeCategoryEntity {

    private LevelEntity level1;
    private List<LevelEntity> level2;

    public LevelEntity getLevel1() {
        return level1;
    }

    public void setLevel1(LevelEntity level1) {
        this.level1 = level1;
    }

    public List<LevelEntity> getLevel2() {
        return level2;
    }

    public void setLevel2(List<LevelEntity> level2) {
        this.level2 = level2;
    }
}
