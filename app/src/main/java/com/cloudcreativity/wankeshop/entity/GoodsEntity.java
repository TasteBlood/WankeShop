package com.cloudcreativity.wankeshop.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseApp;

import java.io.Serializable;

public class GoodsEntity implements Parcelable {
//    private List<Attrs> attrs ;

    private String auditCause;

    private int auditId;

    private int auditStatus;

    private String auditTime;

    private int available;

    private String brand;

    private int brandId;

    private int caregoryId;

    private String caregoryName;

    private String createTime;

    private String disableleCause;

    private int goodsUnit;

    private float salePrice;

    private String icon;

    private int id;

//    private List<Pics> pics ;

    private int saleNum;

//    private List<Skus> skus ;

//    private List<Specs> specs ;

    private String spuCode;

    private String spuName;

    private String spuPic;

    private int spuStatus;

    private int supplierId;

    private String unitQuality;

    protected GoodsEntity(Parcel in) {
        auditCause = in.readString();
        auditId = in.readInt();
        auditStatus = in.readInt();
        auditTime = in.readString();
        available = in.readInt();
        brand = in.readString();
        brandId = in.readInt();
        caregoryId = in.readInt();
        caregoryName = in.readString();
        createTime = in.readString();
        disableleCause = in.readString();
        goodsUnit = in.readInt();
        salePrice = in.readFloat();
        icon = in.readString();
        id = in.readInt();
        saleNum = in.readInt();
        spuCode = in.readString();
        spuName = in.readString();
        spuPic = in.readString();
        spuStatus = in.readInt();
        supplierId = in.readInt();
        unitQuality = in.readString();
    }

    public static final Creator<GoodsEntity> CREATOR = new Creator<GoodsEntity>() {
        @Override
        public GoodsEntity createFromParcel(Parcel in) {
            return new GoodsEntity(in);
        }

        @Override
        public GoodsEntity[] newArray(int size) {
            return new GoodsEntity[size];
        }
    };

    public GoodsEntity() {
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public void setAuditCause(String auditCause){
        this.auditCause = auditCause;
    }
    public String getAuditCause(){
        return this.auditCause;
    }
    public void setAuditId(int auditId){
        this.auditId = auditId;
    }
    public int getAuditId(){
        return this.auditId;
    }
    public void setAuditStatus(int auditStatus){
        this.auditStatus = auditStatus;
    }
    public int getAuditStatus(){
        return this.auditStatus;
    }
    public void setAuditTime(String auditTime){
        this.auditTime = auditTime;
    }
    public String getAuditTime(){
        return this.auditTime;
    }
    public void setAvailable(int available){
        this.available = available;
    }
    public int getAvailable(){
        return this.available;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setBrandId(int brandId){
        this.brandId = brandId;
    }
    public int getBrandId(){
        return this.brandId;
    }
    public void setCaregoryId(int caregoryId){
        this.caregoryId = caregoryId;
    }
    public int getCaregoryId(){
        return this.caregoryId;
    }
    public void setCaregoryName(String caregoryName){
        this.caregoryName = caregoryName;
    }
    public String getCaregoryName(){
        return this.caregoryName;
    }
    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setDisableleCause(String disableleCause){
        this.disableleCause = disableleCause;
    }
    public String getDisableleCause(){
        return this.disableleCause;
    }
    public void setGoodsUnit(int goodsUnit){
        this.goodsUnit = goodsUnit;
    }
    public int getGoodsUnit(){
        return this.goodsUnit;
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

    public void setSaleNum(int saleNum){
        this.saleNum = saleNum;
    }
    public int getSaleNum(){
        return this.saleNum;
    }
    public void setSpuCode(String spuCode){
        this.spuCode = spuCode;
    }
    public String getSpuCode(){
        return this.spuCode;
    }
    public void setSpuName(String spuName){
        this.spuName = spuName;
    }
    public String getSpuName(){
        return this.spuName;
    }
    public void setSpuPic(String spuPic){
        this.spuPic = spuPic;
    }
    public String getSpuPic(){
        return this.spuPic;
    }
    public void setSpuStatus(int spuStatus){
        this.spuStatus = spuStatus;
    }
    public int getSpuStatus(){
        return this.spuStatus;
    }
    public void setSupplierId(int supplierId){
        this.supplierId = supplierId;
    }
    public int getSupplierId(){
        return this.supplierId;
    }
    public void setUnitQuality(String unitQuality){
        this.unitQuality = unitQuality;
    }
    public String getUnitQuality(){
        return this.unitQuality;
    }

    public String formatPrice(){
        return String.format(BaseApp.app.getResources().getString(R.string.str_rmb_character),this.salePrice);
    }

    public String formatSaleNum(){
        return String.format(BaseApp.app.getResources().getString(R.string.str_sale_count),this.saleNum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(auditCause);
        dest.writeInt(auditId);
        dest.writeInt(auditStatus);
        dest.writeString(auditTime);
        dest.writeInt(available);
        dest.writeString(brand);
        dest.writeInt(brandId);
        dest.writeInt(caregoryId);
        dest.writeString(caregoryName);
        dest.writeString(createTime);
        dest.writeString(disableleCause);
        dest.writeInt(goodsUnit);
        dest.writeFloat(salePrice);
        dest.writeString(icon);
        dest.writeInt(id);
        dest.writeInt(saleNum);
        dest.writeString(spuCode);
        dest.writeString(spuName);
        dest.writeString(spuPic);
        dest.writeInt(spuStatus);
        dest.writeInt(supplierId);
        dest.writeString(unitQuality);
    }
}
