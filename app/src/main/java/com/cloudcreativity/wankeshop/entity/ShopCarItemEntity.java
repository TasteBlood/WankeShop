package com.cloudcreativity.wankeshop.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 这是购物车数据
 */
public class ShopCarItemEntity implements Parcelable,Serializable{
    private String createTime;

    private int id;//这是id

    private int num;//这是数量

    private GoodsEntity.SKU sku;//这是sku信息

    private int skuId;//这是skuId

    private GoodsEntity spu;//这是spu信息

    private int spuId;//这是spuId

    private String updateTime;

    private int userId;//这是用户id

    //这是自己定义的字段，用来处理选中和被选中
    public boolean isCheck = false;

    protected ShopCarItemEntity(Parcel in) {
        createTime = in.readString();
        id = in.readInt();
        num = in.readInt();
        sku = in.readParcelable(GoodsEntity.SKU.class.getClassLoader());
        skuId = in.readInt();
        spu = in.readParcelable(GoodsEntity.class.getClassLoader());
        spuId = in.readInt();
        updateTime = in.readString();
        userId = in.readInt();
        isCheck = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createTime);
        dest.writeInt(id);
        dest.writeInt(num);
        dest.writeParcelable(sku, flags);
        dest.writeInt(skuId);
        dest.writeParcelable(spu, flags);
        dest.writeInt(spuId);
        dest.writeString(updateTime);
        dest.writeInt(userId);
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShopCarItemEntity> CREATOR = new Creator<ShopCarItemEntity>() {
        @Override
        public ShopCarItemEntity createFromParcel(Parcel in) {
            return new ShopCarItemEntity(in);
        }

        @Override
        public ShopCarItemEntity[] newArray(int size) {
            return new ShopCarItemEntity[size];
        }
    };

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
    public void setNum(int num){
        this.num = num;
    }
    public int getNum(){
        return this.num;
    }
    public void setSku(GoodsEntity.SKU sku){
        this.sku = sku;
    }
    public GoodsEntity.SKU getSku(){
        return this.sku;
    }
    public void setSkuId(int skuId){
        this.skuId = skuId;
    }
    public int getSkuId(){
        return this.skuId;
    }
    public void setSpu(GoodsEntity spu){
        this.spu = spu;
    }
    public GoodsEntity getSpu(){
        return this.spu;
    }
    public void setSpuId(int spuId){
        this.spuId = spuId;
    }
    public int getSpuId(){
        return this.spuId;
    }
    public void setUpdateTime(String updateTime){
        this.updateTime = updateTime;
    }
    public String getUpdateTime(){
        return this.updateTime;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId(){
        return this.userId;
    }
}
