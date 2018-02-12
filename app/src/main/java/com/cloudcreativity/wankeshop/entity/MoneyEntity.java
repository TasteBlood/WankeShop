package com.cloudcreativity.wankeshop.entity;

/**
 * 这是账户记录 entity
 */
public class MoneyEntity {
    private int audit;//交易状态 0 等待中  1 成功  2 失败
    private int balance;//账户余额
    private String createTime;
    private float drawMoney;
    private String headPic;
    private int id;
    private String mobile;
    private float rechargeMoney;
    private int state;
    private int type;
    private String updateTime;
    private int userId;
    private String userName;

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public float getDrawMoney() {
        return drawMoney;
    }

    public void setDrawMoney(float drawMoney) {
        this.drawMoney = drawMoney;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public float getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(float rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 这是方便处理金额
     * @return 处理后的字符串
     */
    public String getMoney(){
        return "+"+(this.rechargeMoney==0?String.valueOf(this.drawMoney):String.valueOf(this.rechargeMoney));
    }
}
