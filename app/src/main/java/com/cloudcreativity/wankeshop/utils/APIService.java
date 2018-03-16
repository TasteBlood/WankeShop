package com.cloudcreativity.wankeshop.utils;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 整个程序的网络接口配置
 */
public interface APIService {
    /**
     * 网络请求的配置
     */
    long timeOut = 10 * 1000;//网络超时
    /**
     * 整体的接口配置
     */
    String TEST_HOST = "http://192.168.31.19/vmall/";
    String ONLINE_HOST = "http://www.sina.com/";
    String HOST = AppConfig.DEBUG ? TEST_HOST : ONLINE_HOST;

    //获取七牛的上传图片的token
    @GET("getQiNiuToken.do")
    Observable<String> getQiNiuToken();

    //登录接口  0  密码在服务器加密适用于普通登录  1 密码不许加密适用于微信登录
    @FormUrlEncoded
    @POST("login.do")
    Observable<String> login(@Field("mobile") String mobile,
                             @Field("password") String password,
                             @Field("type") int type);

    //注册第一步验证手机号接口
    @FormUrlEncoded
    @POST("checkMobile.do")
    Observable<String> registerFirst(@Field("mobile") String mobile);

    //注册第二步获取验证码
    @FormUrlEncoded
    @POST("sendSms.do")
    Observable<String> registerGetVerRify(@Field("mobile") String mobile);

    //注册第三步，提交验证码、手机号、密码进行注册
    @FormUrlEncoded
    @POST("saveUser.do")
    Observable<String> registerFinalStep(@Field("mobile") String mobile,
                                         @Field("password") String password,
                                         @Field("sms") String sms,
                                         @Field("provinceId") String provinceId,
                                         @Field("cityId") String cityId,
                                         @Field("areaId") String areaId);

    //忘记密码，修改密码
    @FormUrlEncoded
    @POST("editPassword.do")
    Observable<String> editPassword(@Field("mobile") String mobile,
                                    @Field("newPwd") String password,
                                    @Field("sms") String sms);

    //退出登录
    @FormUrlEncoded
    @POST("outLogin.do")
    Observable<String> logout();

    //编辑和完善资料
    @FormUrlEncoded
    @POST("updateUser.do")
    Observable<String> editInformation(@Field("userName") String userName,
                                       @Field("realName") String realName,
                                       @Field("password") String password,
                                       @Field("headPic") String headPic,
                                       @Field("email") String email,
                                       @Field("sex") int sex,
                                       @Field("idCard") String iDcard,
                                       @Field("birthDay") String birthDay,
                                       @Field("type") int type,
                                       @Field("provinceId") String provinceId,
                                       @Field("cityId") String cityId,
                                       @Field("areaId") String areaId);

    //修改手机号第一步
    @FormUrlEncoded
    @POST("editUser1.do")
    Observable<String> modifyMobileOne(@Field("forget") boolean forget);

    //修改手机号第二步
    @FormUrlEncoded
    @POST("editUser2.do")
    Observable<String> modifyMobileTwo(@Field("sms") String sms);

    //修改手机号第三步
    @FormUrlEncoded
    @POST("editMobile3.do")
    Observable<String> modifyMobileThree(@Field("mobile") String mobile);

    //修改手机号第四
    @FormUrlEncoded
    @POST("editMobile4.do")
    Observable<String> modifyMobileFour(@Field("mobile") String mobile,
                                        @Field("sms") String sms);

    //微信登录第一步验证
    @FormUrlEncoded
    @POST("checkWxUser.do")
    Observable<String> checkWxUser(@Field("openId") String openId,
                                     @Field("nickName") String nickName,
                                     @Field("headImgUrl") String headImgUrl);

    //微信登录第二步检测手机号是否已经注册
    @FormUrlEncoded
    @POST("checkWxMobile.do")
    Observable<String>  checkWxUserMobile(@Field("mobile") String mobile,
                                          @Field("sms") String sms);

    //微信登录第三步，保存用户信息
    @FormUrlEncoded
    @POST("saveUserForWx.do")
    Observable<String> saveUserForWx(@Field("mobile") String mobile,
                                     @Field("password") String password,
                                     @Field("provinceId") String provinceId,
                                     @Field("cityId") String cityId,
                                     @Field("areaId") String areaId);

    //微信注册第四步，绑定userId和openId
    @FormUrlEncoded
    @POST("bindWxUser.do")
    Observable<String> bindWxUser(@Field("openId") String openId,
                                  @Field("mobile") String mobile);


    //获取我的收货地址
    @GET("getMyMallAddress.do")
    Observable<String> getMyAddress();

    //添加收货地址
    //  isDefault  0 不是 1是 默认地址
    @FormUrlEncoded
    @POST("newMallAddress.do")
    Observable<String> addAddress(@Field("provinceId") int provinceId,
                                  @Field("cityId") int cityId,
                                  @Field("areaId") int areaId,
                                  @Field("streetId") int streetId,
                                  @Field("addressInfo") String addressInfo,
                                  @Field("zipCode") String zipCode,
                                  @Field("receiptName") String receiptName,
                                  @Field("receiptMobile") String receiptMobile,
                                  @Field("isDefault") int isDefault);


    //编辑收货地址
    // id  就是当前编辑地址的 id
    @FormUrlEncoded
    @POST("editMallAddress.do")
    Observable<String> editAddress(@Field("id") int id,
                                   @Field("provinceId") int provinceId,
                                   @Field("cityId") int cityId,
                                   @Field("areaId") int areaId,
                                   @Field("streetId") int streetId,
                                   @Field("addressInfo") String addressInfo,
                                   @Field("zipCode") String zipCode,
                                   @Field("receiptName") String receiptName,
                                   @Field("receiptMobile") String receiptMobile,
                                   @Field("isDefault") int isDefault);

    //删除收货地址
    @GET("deleteMyMallAddress.do")
    Observable<String> deleteAddress(@Query("addressId") int addressId);


    //获取省列表
    @GET("getProvinceList.do")
    Observable<String> getProvinces();

    //获取市列表
    @GET("getCityList.do")
    Observable<String> getCities(@Query("provinceId") int provinceId);

    //获取区县列表
    @GET("getAreaList.do")
    Observable<String> getAreas(@Query("cityId") int cityId);

    //获取乡镇、街道
    @GET("getStreetList.do")
    Observable<String> getStreet(@Query("areaId") int areaId);

    //获取资金账户记录
    /**
     *
     * @param type 类型 1 充值  2 取现
     * @param pageNum 页码
     * @param pageSize 页容量
     */
    @GET("getCashList.do")
    Observable<String> getMoneyList(@Query("int type") int type,
                                    @Query("pageNum") int pageNum,
                                    @Query("pageSize") int pageSize);

    //申请成为物流小二
    /**
     * @param name 姓名
     * @param mobile 手机号
     * @param provinceId 省id
     * @param cityId 市id
     * @param areaId 区/县id
     * @param emerConName 紧急联系人姓名
     * @param emerConMobile 紧急联系人手机号
     * @param linceColor 车牌颜色
     * @param carType 车类型
     * @param idCard 身份证号
     * @param idCardFront 身份证正面照路径
     * @param driverLicenseFont 驾驶证正面照路径
     * @param situationLicenseFront 行驶证正面照路径
     * @param operateLicenseFont 营运证正面照路径
     * @param carPhoto1 车辆45度照片路径
     */
    @POST("saveDispatcher.do")
    @FormUrlEncoded
    Observable<String> applyToLogistics(    @Field("name") String name,
                                            @Field("mobile") String mobile,
                                            @Field("provinceId") String provinceId,
                                            @Field("cityId") String cityId,
                                            @Field("areaId") String areaId,
                                            @Field("emerConName") String emerConName,
                                            @Field("emerConMobile") String emerConMobile,
                                            @Field("linceColor") String linceColor,
                                            @Field("carType") String carType,
                                            @Field("carNum") String carNum,
                                            @Field("idCard") String idCard,
                                            @Field("idCardFront") String idCardFront,
                                            @Field("driverLicenseFont") String driverLicenseFont,
                                            @Field("situationLicenseFront") String situationLicenseFront,
                                            @Field("operateLicenseFont") String operateLicenseFont,
                                            @Field("carPhoto1") String carPhoto1);


    //获取物流小二的已经提交的信息
    @GET("getDispatcherFrontInfo.do")
    Observable<String> getApplyToLogisticsInfo();

    //获取首页轮播图
    @GET("getCarouselList.do")
    Observable<String> getCarouseList();

    //获取到首页功能导航列表
    @GET("getChannelTypeList4Front.do")
    Observable<String> getHomeNavList();

    //获取首页的商品列表
    @GET("getSpuListByUser.do")
    Observable<String> getHomeGoodsList(@Query("pageNum") int pageNum);

    //进入频道详情信息  style =0 h5页面  style=2 商户内容 style=3 商品内容
    @GET("getPageSpuListByChannel.do")
    Observable<String> getChanelContent(@Query("pageNum") int pageNum,
                                            @Query("style") String style,
                                            @Query("typeId") String typeId);

    //banner的信息
    Observable<String> getBannerContent();
}
