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
     * 整体的网关接口配置
     */
    String TEST_HOST = "http://192.168.31.19/vmall/";
    String ONLINE_HOST = "http://www.sina.com/";
    String HOST = AppConfig.DEBUG ? TEST_HOST : ONLINE_HOST;

    //获取七牛的上传图片的token
    @GET("getQiNiuToken.do")
    Observable<String> getQiNiuToken();

    //登录接口
    @FormUrlEncoded
    @POST("login.do")
    Observable<String> login(@Field("mobile") String mobile,
                                 @Field("password") String password);

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
                                         @Field("sms") String sms);

    //忘记密码，修改密码
    @FormUrlEncoded
    @POST("editPassword.do")
    Observable<String> editPassword(@Field("mobile") String mobile,
                                    @Field("newPwd") String password,
                                    @Field("sms") String sms);

    //退出登录
    @FormUrlEncoded
    @POST("outLogin.do")
    Observable<String> logout(@Field("userId") int userId,
                              @Field("token") String token);

    //编辑和完善资料
    @FormUrlEncoded
    @POST("updateUser.do")
    Observable<String> editInformation(@Field("userId") int userId,
                                       @Field("token") String token,
                                       @Field("userName") String userName,
                                       @Field("realName") String realName,
                                       @Field("password") String password,
                                       @Field("headPic") String headPic,
                                       @Field("email") String email,
                                       @Field("sex") int sex,
                                       @Field("idCard") String iDcard,
                                       @Field("birthDay") String birthDay,
                                       @Field("type") int type);

    //修改手机号第一步
    @FormUrlEncoded
    @POST("editUser1.do")
    Observable<String> modifyMobileOne(@Field("userId") int userId,
                                       @Field("token") String token,
                                       @Field("forget") boolean forget);

    //修改手机号第二步
    @FormUrlEncoded
    @POST("editUser2.do")
    Observable<String> modifyMobileTwo(@Field("userId") int userId,
                                       @Field("token") String token,
                                       @Field("sms") String sms);

    //修改手机号第三步
    @FormUrlEncoded
    @POST("editMobile3.do")
    Observable<String> modifyMobileThree(@Field("userId") int userId,
                                         @Field("token") String token,
                                         @Field("mobile") String mobile);

    //修改手机号第四
    @FormUrlEncoded
    @POST("editMobile4.do")
    Observable<String> modifyMobileFour(@Field("userId") int userId,
                                        @Field("token") String token,
                                        @Field("mobile") String mobile,
                                        @Field("sms") String sms);

    //获取我的收货地址
    @FormUrlEncoded
    @POST("getMyMallAddress.do")
    Observable<String> getMyAddress(@Field("userId") int userId,
                                    @Field("token") String token);

    //添加收货地址
    //  isDefault  0 不是 1是 默认地址
    @FormUrlEncoded
    @POST("newMallAddress.do")
    Observable<String> addAddress(@Field("userId") int userId,
                                  @Field("token") String token,
                                  @Field("provinceId") int provinceId,
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
    Observable<String> editAddress(@Field("userId") int userId,
                                   @Field("token") String token,
                                   @Field("id") int id,
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
    Observable<String> deleteAddress(@Query("userId") int userId,
                                     @Query("token") String token,
                                     @Query("addressId") int addressId);


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
}
