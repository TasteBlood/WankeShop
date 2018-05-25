package com.cloudcreativity.wankeshop.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseApp;
import com.cloudcreativity.wankeshop.utils.StrUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 商品信息
 */
public class GoodsEntity implements Parcelable,Serializable{
//    private List<Attrs> attrs ;

    private String auditCause;

    private int auditId;

    private int auditStatus;

    private String auditTime;

    private int available;

    private Brand brand;

    private int brandId;

    private int caregoryId;

    private String caregoryName;

    private String createTime;

    private String disableleCause;

    private int goodsUnit;

    private String salePrice;

    private String icon;

    private int id;

    private List<Pics> pics ;

    private int saleNum;

    private List<SKU> skus ;

    private List<Specs> specs ;

    private String spuCode;

    private String spuName;

    private String spuPic;

    private int spuStatus;

    private String spuDesc;//商品详情

    private int supplierId;

    private Unit unit;

    private String unitQuality;

    private int isCollect;//是否收藏

    private int isGift;//是否是促销

    public GoodsEntity() {
    }


    protected GoodsEntity(Parcel in) {
        auditCause = in.readString();
        auditId = in.readInt();
        auditStatus = in.readInt();
        auditTime = in.readString();
        available = in.readInt();
        brandId = in.readInt();
        caregoryId = in.readInt();
        caregoryName = in.readString();
        createTime = in.readString();
        disableleCause = in.readString();
        goodsUnit = in.readInt();
        salePrice = in.readString();
        icon = in.readString();
        id = in.readInt();
        saleNum = in.readInt();
        skus = in.createTypedArrayList(SKU.CREATOR);
        spuCode = in.readString();
        spuName = in.readString();
        spuPic = in.readString();
        spuStatus = in.readInt();
        spuDesc = in.readString();
        supplierId = in.readInt();
        unit = in.readParcelable(Unit.class.getClassLoader());
        unitQuality = in.readString();
        isCollect = in.readInt();
        isGift = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(auditCause);
        dest.writeInt(auditId);
        dest.writeInt(auditStatus);
        dest.writeString(auditTime);
        dest.writeInt(available);
        dest.writeInt(brandId);
        dest.writeInt(caregoryId);
        dest.writeString(caregoryName);
        dest.writeString(createTime);
        dest.writeString(disableleCause);
        dest.writeInt(goodsUnit);
        dest.writeString(salePrice);
        dest.writeString(icon);
        dest.writeInt(id);
        dest.writeInt(saleNum);
        dest.writeTypedList(skus);
        dest.writeString(spuCode);
        dest.writeString(spuName);
        dest.writeString(spuPic);
        dest.writeInt(spuStatus);
        dest.writeString(spuDesc);
        dest.writeInt(supplierId);
        dest.writeParcelable(unit, flags);
        dest.writeString(unitQuality);
        dest.writeInt(isCollect);
        dest.writeInt(isGift);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getSpuDesc() {
        return spuDesc;
    }

    public void setSpuDesc(String spuDesc) {
        this.spuDesc = spuDesc;
    }

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public List<Pics> getPics() {
        return pics;
    }

    public void setPics(List<Pics> pics) {
        this.pics = pics;
    }

    public List<SKU> getSkus() {
        return skus;
    }

    public void setSkus(List<SKU> skus) {
        this.skus = skus;
    }

    public List<Specs> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Specs> specs) {
        this.specs = specs;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
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
    public void setBrand(Brand brand){
        this.brand = brand;
    }
    public Brand getBrand(){
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
        return String.format(BaseApp.app.getResources().getString(R.string.str_rmb_character), StrUtils.get2BitDecimal(TextUtils.isEmpty(this.salePrice)?0.00f:Float.parseFloat(this.salePrice)));
    }

    public String formatSaleNum(){
        return String.format(BaseApp.app.getResources().getString(R.string.str_sale_count),StrUtils.formatNumberByThousands(this.saleNum));
    }

    /**
     * 这是SKU实体数据
     */
    public static class SKU implements Parcelable,Serializable{
        private int alertNum;

        private int clickCount;

        private String createTime;

        private int depositNum;

        private int hotSell;

        private String icon;

        private int id;

        private int maxQuality;

        private int minQuality;

        private String regionOperator;

        private int saleNum;

        private String salePrice;

        private String scanCode;

        private String skuCode;

        private String skuName;

        private String skuTagPrice;

        private List<Specs> specs ;

        private int spuId;

        private int status;

        private int isGift;//是否是促销

        private List<SKUGift> skuGiftList;

        protected SKU(Parcel in) {
            alertNum = in.readInt();
            clickCount = in.readInt();
            createTime = in.readString();
            depositNum = in.readInt();
            hotSell = in.readInt();
            icon = in.readString();
            id = in.readInt();
            maxQuality = in.readInt();
            minQuality = in.readInt();
            regionOperator = in.readString();
            saleNum = in.readInt();
            salePrice = in.readString();
            scanCode = in.readString();
            skuCode = in.readString();
            skuName = in.readString();
            skuTagPrice = in.readString();
            spuId = in.readInt();
            status = in.readInt();
            isGift = in.readInt();
            skuGiftList = in.createTypedArrayList(SKUGift.CREATOR);
        }

        public static final Creator<SKU> CREATOR = new Creator<SKU>() {
            @Override
            public SKU createFromParcel(Parcel in) {
                return new SKU(in);
            }

            @Override
            public SKU[] newArray(int size) {
                return new SKU[size];
            }
        };

        public List<SKUGift> getSkuGiftList() {
            return skuGiftList;
        }

        public void setSkuGiftList(List<SKUGift> skuGiftList) {
            this.skuGiftList = skuGiftList;
        }

        public void setAlertNum(int alertNum){
            this.alertNum = alertNum;
        }
        public int getAlertNum(){
            return this.alertNum;
        }
        public void setClickCount(int clickCount){
            this.clickCount = clickCount;
        }
        public int getClickCount(){
            return this.clickCount;
        }
        public void setCreateTime(String createTime){
            this.createTime = createTime;
        }
        public String getCreateTime(){
            return this.createTime;
        }
        public void setDepositNum(int depositNum){
            this.depositNum = depositNum;
        }
        public int getDepositNum(){
            return this.depositNum;
        }
        public void setHotSell(int hotSell){
            this.hotSell = hotSell;
        }
        public int getHotSell(){
            return this.hotSell;
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
        public void setMaxQuality(int maxQuality){
            this.maxQuality = maxQuality;
        }
        public int getMaxQuality(){
            return this.maxQuality;
        }
        public void setMinQuality(int minQuality){
            this.minQuality = minQuality;
        }
        public int getMinQuality(){
            return this.minQuality;
        }
        public void setRegionOperator(String regionOperator){
            this.regionOperator = regionOperator;
        }
        public String getRegionOperator(){
            return this.regionOperator;
        }
        public void setSaleNum(int saleNum){
            this.saleNum = saleNum;
        }
        public int getSaleNum(){
            return this.saleNum;
        }
        public void setSalePrice(String salePrice){
            this.salePrice = salePrice;
        }
        public String getSalePrice(){
            return this.salePrice;
        }
        public void setScanCode(String scanCode){
            this.scanCode = scanCode;
        }
        public String getScanCode(){
            return this.scanCode;
        }
        public void setSkuCode(String skuCode){
            this.skuCode = skuCode;
        }
        public String getSkuCode(){
            return this.skuCode;
        }
        public void setSkuName(String skuName){
            this.skuName = skuName;
        }
        public String getSkuName(){
            return this.skuName;
        }
        public void setSkuTagPrice(String skuTagPrice){
            this.skuTagPrice = skuTagPrice;
        }
        public String getSkuTagPrice(){
            return this.skuTagPrice;
        }
        public void setSpecs(List<Specs> specs){
            this.specs = specs;
        }
        public List<Specs> getSpecs(){
            return this.specs;
        }
        public void setSpuId(int spuId){
            this.spuId = spuId;
        }
        public int getSpuId(){
            return this.spuId;
        }
        public void setStatus(int status){
            this.status = status;
        }
        public int getStatus(){
            return this.status;
        }

        public int getIsGift() {
            return isGift;
        }

        public void setIsGift(int isGift) {
            this.isGift = isGift;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(alertNum);
            dest.writeInt(clickCount);
            dest.writeString(createTime);
            dest.writeInt(depositNum);
            dest.writeInt(hotSell);
            dest.writeString(icon);
            dest.writeInt(id);
            dest.writeInt(maxQuality);
            dest.writeInt(minQuality);
            dest.writeString(regionOperator);
            dest.writeInt(saleNum);
            dest.writeString(salePrice);
            dest.writeString(scanCode);
            dest.writeString(skuCode);
            dest.writeString(skuName);
            dest.writeString(skuTagPrice);
            dest.writeInt(spuId);
            dest.writeInt(status);
            dest.writeInt(isGift);
            dest.writeTypedList(skuGiftList);
        }
    }

    /**
     * 这是详细的sku信息
     */
    public static class Specs{
        private String goodsSpeciValue;

        private int id;

        private int skuId;

        private String specifactionName;

        private int spuID;

        public void setGoodsSpeciValue(String goodsSpeciValue){
            this.goodsSpeciValue = goodsSpeciValue;
        }
        public String getGoodsSpeciValue(){
            return this.goodsSpeciValue;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setSkuId(int skuId){
            this.skuId = skuId;
        }
        public int getSkuId(){
            return this.skuId;
        }
        public void setSpecifactionName(String specifactionName){
            this.specifactionName = specifactionName;
        }
        public String getSpecifactionName(){
            return this.specifactionName;
        }
        public void setSpuID(int spuID){
            this.spuID = spuID;
        }
        public int getSpuID(){
            return this.spuID;
        }

    }

    /**
     * 这是照片信息实体
     */
    public static class Pics{
        private String createTime;

        private int id;

        private String picExt;

        private String picPath;

        private int picShort;

        private int picType;

        private int spuId;

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
        public void setPicExt(String picExt){
            this.picExt = picExt;
        }
        public String getPicExt(){
            return this.picExt;
        }
        public void setPicPath(String picPath){
            this.picPath = picPath;
        }
        public String getPicPath(){
            return this.picPath;
        }
        public void setPicShort(int picShort){
            this.picShort = picShort;
        }
        public int getPicShort(){
            return this.picShort;
        }
        public void setPicType(int picType){
            this.picType = picType;
        }
        public int getPicType(){
            return this.picType;
        }
        public void setSpuId(int spuId){
            this.spuId = spuId;
        }
        public int getSpuId(){
            return this.spuId;
        }
    }

    /**
     * 这是单位实体
     */
    public static class Unit implements Parcelable,Serializable{
        private String createTime;

        private int creatorId;

        private int id;

        private String remark;

        private int sort;

        private String unityName;

        protected Unit(Parcel in) {
            createTime = in.readString();
            creatorId = in.readInt();
            id = in.readInt();
            remark = in.readString();
            sort = in.readInt();
            unityName = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(createTime);
            dest.writeInt(creatorId);
            dest.writeInt(id);
            dest.writeString(remark);
            dest.writeInt(sort);
            dest.writeString(unityName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Unit> CREATOR = new Creator<Unit>() {
            @Override
            public Unit createFromParcel(Parcel in) {
                return new Unit(in);
            }

            @Override
            public Unit[] newArray(int size) {
                return new Unit[size];
            }
        };

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
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setRemark(String remark){
            this.remark = remark;
        }
        public String getRemark(){
            return this.remark;
        }
        public void setSort(int sort){
            this.sort = sort;
        }
        public int getSort(){
            return this.sort;
        }
        public void setUnityName(String unityName){
            this.unityName = unityName;
        }
        public String getUnityName(){
            return this.unityName;
        }

    }

    /**
     * 这是Brand信息
     */
    public static class Brand{
        private String brandDesc;

        private String brandName;

        private int brandSort;

        private String createTime;

        private int creatorId;

        private int id;

        private String logo;

        private int status;

        public void setBrandDesc(String brandDesc){
            this.brandDesc = brandDesc;
        }
        public String getBrandDesc(){
            return this.brandDesc;
        }
        public void setBrandName(String brandName){
            this.brandName = brandName;
        }
        public String getBrandName(){
            return this.brandName;
        }
        public void setBrandSort(int brandSort){
            this.brandSort = brandSort;
        }
        public int getBrandSort(){
            return this.brandSort;
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
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setLogo(String logo){
            this.logo = logo;
        }
        public String getLogo(){
            return this.logo;
        }
        public void setStatus(int status){
            this.status = status;
        }
        public int getStatus(){
            return this.status;
        }
    }

    public static class SKUGift implements Parcelable,Serializable{
        private String createTime;
        private String desc;
        private int giftId;
        private int giftNum;
        private int id;
        private int skuId;
        private int skuNum;
        private String updateTime;

        SKUGift(Parcel in) {
            createTime = in.readString();
            desc = in.readString();
            giftId = in.readInt();
            giftNum = in.readInt();
            id = in.readInt();
            skuId = in.readInt();
            skuNum = in.readInt();
            updateTime = in.readString();
        }

        public static final Creator<SKUGift> CREATOR = new Creator<SKUGift>() {
            @Override
            public SKUGift createFromParcel(Parcel in) {
                return new SKUGift(in);
            }

            @Override
            public SKUGift[] newArray(int size) {
                return new SKUGift[size];
            }
        };

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getGiftId() {
            return giftId;
        }

        public void setGiftId(int giftId) {
            this.giftId = giftId;
        }

        public int getGiftNum() {
            return giftNum;
        }

        public void setGiftNum(int giftNum) {
            this.giftNum = giftNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSkuId() {
            return skuId;
        }

        public void setSkuId(int skuId) {
            this.skuId = skuId;
        }

        public int getSkuNum() {
            return skuNum;
        }

        public void setSkuNum(int skuNum) {
            this.skuNum = skuNum;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(createTime);
            dest.writeString(desc);
            dest.writeInt(giftId);
            dest.writeInt(giftNum);
            dest.writeInt(id);
            dest.writeInt(skuId);
            dest.writeInt(skuNum);
            dest.writeString(updateTime);
        }
    }
}
