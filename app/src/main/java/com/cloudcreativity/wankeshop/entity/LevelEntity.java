package com.cloudcreativity.wankeshop.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class LevelEntity implements Serializable,Parcelable{

//    private List<Attrs> attrs ;
//
//    private List<Brands> brands ;

    private String categoryCode;

    private String categoryName;

    private String categorySort;

    private String createTime;

    private int creatorId;

    private int deeper;

    private String des;

    private String icon;

    private int id;

    private int parentId;

    private String parentIds;

    private String parentNames;

    //private List<Specs> specs ;

    private int status;

    public LevelEntity() {
    }

    protected LevelEntity(Parcel in) {
        categoryCode = in.readString();
        categoryName = in.readString();
        categorySort = in.readString();
        createTime = in.readString();
        creatorId = in.readInt();
        deeper = in.readInt();
        des = in.readString();
        icon = in.readString();
        id = in.readInt();
        parentId = in.readInt();
        parentIds = in.readString();
        parentNames = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryCode);
        dest.writeString(categoryName);
        dest.writeString(categorySort);
        dest.writeString(createTime);
        dest.writeInt(creatorId);
        dest.writeInt(deeper);
        dest.writeString(des);
        dest.writeString(icon);
        dest.writeInt(id);
        dest.writeInt(parentId);
        dest.writeString(parentIds);
        dest.writeString(parentNames);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LevelEntity> CREATOR = new Creator<LevelEntity>() {
        @Override
        public LevelEntity createFromParcel(Parcel in) {
            return new LevelEntity(in);
        }

        @Override
        public LevelEntity[] newArray(int size) {
            return new LevelEntity[size];
        }
    };

    //    public void setAttrs(List<Attrs> attrs){
//        this.attrs = attrs;
//    }
//    public List<Attrs> getAttrs(){
//        return this.attrs;
//    }
//    public void setBrands(List<Brands> brands){
//        this.brands = brands;
//    }
//    public List<Brands> getBrands(){
//        return this.brands;
//    }
    public void setCategoryCode(String categoryCode){
        this.categoryCode = categoryCode;
    }
    public String getCategoryCode(){
        return this.categoryCode;
    }
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
    public String getCategoryName(){
        return this.categoryName;
    }
    public void setCategorySort(String categorySort){
        this.categorySort = categorySort;
    }
    public String getCategorySort(){
        return this.categorySort;
    }
    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setCreatorId(int creatorId){
        this.creatorId = creatorId;
    }
    public int getCreatorId(){
        return this.creatorId;
    }
    public void setDeeper(int deeper){
        this.deeper = deeper;
    }
    public int getDeeper(){
        return this.deeper;
    }
    public void setDes(String des){
        this.des = des;
    }
    public String getDes(){
        return this.des;
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
    public void setParentId(int parentId){
        this.parentId = parentId;
    }
    public int getParentId(){
        return this.parentId;
    }
    public void setParentIds(String parentIds){
        this.parentIds = parentIds;
    }
    public String getParentIds(){
        return this.parentIds;
    }
    public void setParentNames(String parentNames){
        this.parentNames = parentNames;
    }
    public String getParentNames(){
        return this.parentNames;
    }
//    public void setSpecs(List<Specs> specs){
//        this.specs = specs;
//    }
//    public List<Specs> getSpecs(){
//        return this.specs;
//    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
}
