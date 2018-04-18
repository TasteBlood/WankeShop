package com.cloudcreativity.wankeshop.entity;

/**
 * 这是账户记录 entity
 */
public class MoneyEntity {
    private int audit;//交易状态 0 等待中  1 成功  2 失败
    private int balance;//账户余额
    private String createTime;
    private double drawMoney;//提现金额
    private String headPic;
    private int id;
    private String mobile;
    private double rechargeMoney;//充值金额
    private int state;// 1充值  2提现  3支付
    private double payMoney;//消费支付的钱
    private int type;//支付方式 1 微信 2 支付宝 3 余额
    private String outTradeNo;//订单号
    private String transactionNo;//支付单号
    private String updateTime;
    private int userId;
    private String userName;
    private String cashNo;//自己平台的交易单号

    public double getDrawMoney() {
        return drawMoney;
    }

    public void setDrawMoney(double drawMoney) {
        this.drawMoney = drawMoney;
    }

    public double getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(double rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public void setPayMoney(float payMoney) {
        this.payMoney = payMoney;
    }

    public String getCashNo() {
        return cashNo;
    }

    public void setCashNo(String cashNo) {
        this.cashNo = cashNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

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
        if(this.state==3){
            return "-"+String.valueOf(this.payMoney);
        }else{
            return "+"+(this.state==2?String.valueOf(this.drawMoney):String.valueOf(this.rechargeMoney));
        }
    }
}
