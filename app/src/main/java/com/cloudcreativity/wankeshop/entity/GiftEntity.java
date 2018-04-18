package com.cloudcreativity.wankeshop.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 这是赠品的数据实体
 */
public class GiftEntity implements Parcelable{
    private String createTime;
    private String desc;
    private int giftId;
    private int giftNum;
    private int id;
    private GoodsEntity.SKU sku;
    private int skuId;
    private int skuNum;
    private GoodsEntity spu;
    private String updateTime;

    protected GiftEntity(Parcel in) {
        createTime = in.readString();
        desc = in.readString();
        giftId = in.readInt();
        giftNum = in.readInt();
        id = in.readInt();
        sku = in.readParcelable(GoodsEntity.SKU.class.getClassLoader());
        skuId = in.readInt();
        skuNum = in.readInt();
        spu = in.readParcelable(GoodsEntity.class.getClassLoader());
        updateTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createTime);
        dest.writeString(desc);
        dest.writeInt(giftId);
        dest.writeInt(giftNum);
        dest.writeInt(id);
        dest.writeParcelable(sku, flags);
        dest.writeInt(skuId);
        dest.writeInt(skuNum);
        dest.writeParcelable(spu, flags);
        dest.writeString(updateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GiftEntity> CREATOR = new Creator<GiftEntity>() {
        @Override
        public GiftEntity createFromParcel(Parcel in) {
            return new GiftEntity(in);
        }

        @Override
        public GiftEntity[] newArray(int size) {
            return new GiftEntity[size];
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

    public GoodsEntity.SKU getSku() {
        return sku;
    }

    public void setSku(GoodsEntity.SKU sku) {
        this.sku = sku;
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

    public GoodsEntity getSpu() {
        return spu;
    }

    public void setSpu(GoodsEntity spu) {
        this.spu = spu;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
