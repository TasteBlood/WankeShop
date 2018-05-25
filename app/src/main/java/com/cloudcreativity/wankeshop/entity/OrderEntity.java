package com.cloudcreativity.wankeshop.entity;

import java.io.Serializable;

/**
 * 这是每一个商品订单数据
 */
public class OrderEntity implements Serializable{
    private int addressId;

    private String createTime;

    private int id;

    private String onlyId;

    private String orderNum;

    private String payMoney;

    private String payNum;

    private int payState;

    private int payWay;

    private int shipState;

    private int shopId;

    private ShopCarItemEntity shoppingCart;

    private int shoppingCartId;

    private int state;

    private String updateTime;

    private int userId;

    private int isDelay;//是否延长收货

    private String refundDesc;//退款原因

    private int refundState;//退款状态

    private int isNoReason;//是否支持7天无忧退货

    private int returnState;//退货状态

    private String completeTime;//订单完成时间

    private OrderGift skuGiftList;//这是订单赠品信息

    private GIFT orderGift;//这是订单数量信息

    public GIFT getOrderGift() {
        return orderGift;
    }

    public void setOrderGift(GIFT orderGift) {
        this.orderGift = orderGift;
    }

    public OrderGift getSkuGiftList() {
        return skuGiftList;
    }

    public void setSkuGiftList(OrderGift skuGiftList) {
        this.skuGiftList = skuGiftList;
    }

    public int getReturnState() {
        return returnState;
    }

    public void setReturnState(int returnState) {
        this.returnState = returnState;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public int getIsNoReason() {
        return isNoReason;
    }

    public void setIsNoReason(int isNoReason) {
        this.isNoReason = isNoReason;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public int getIsDelay() {
        return isDelay;
    }

    public void setIsDelay(int isDelay) {
        this.isDelay = isDelay;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOnlyId() {
        return onlyId;
    }

    public void setOnlyId(String onlyId) {
        this.onlyId = onlyId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public int getShipState() {
        return shipState;
    }

    public void setShipState(int shipState) {
        this.shipState = shipState;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public ShopCarItemEntity getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShopCarItemEntity shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public int getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(int shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    //这是赠品商品信息
    public static class OrderGift implements Serializable{
        private String createTime;
        private int id;
        private int num;
        private int shopId;
        private String skuIcon;
        private int skuId;
        private String skuName;
        private String spuIcon;
        private int spuId;
        private String spuName;


        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getSkuIcon() {
            return skuIcon;
        }

        public void setSkuIcon(String skuIcon) {
            this.skuIcon = skuIcon;
        }

        public int getSkuId() {
            return skuId;
        }

        public void setSkuId(int skuId) {
            this.skuId = skuId;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSpuIcon() {
            return spuIcon;
        }

        public void setSpuIcon(String spuIcon) {
            this.spuIcon = spuIcon;
        }

        public int getSpuId() {
            return spuId;
        }

        public void setSpuId(int spuId) {
            this.spuId = spuId;
        }

        public String getSpuName() {
            return spuName;
        }

        public void setSpuName(String spuName) {
            this.spuName = spuName;
        }
    }

    //这是赠品数量信息
    public static class GIFT implements Serializable{
        private int giftId;
        private int giftNum;

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
    }
}
