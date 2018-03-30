package com.cloudcreativity.wankeshop.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cloudcreativity.wankeshop.base.BaseApp;
import com.cloudcreativity.wankeshop.entity.UserEntity;
import com.google.gson.Gson;

/**
 * 这是SharePreferences工具
 */
public class SPUtils {

    public interface Config{
        String IS_LOGIN = "app_is_login";
        String UID = "app_login_user_id";
        String TOKEN = "app_request_token";
        String USER = "app_login_user";
        String DEFAULT_ADDRESS = "app_default_address";
    }

    private static SharedPreferences preferences;
    private static SPUtils utils;
    private SPUtils(){
        preferences = BaseApp.app.getSharedPreferences(AppConfig.SP_NAME,Context.MODE_PRIVATE);
    }

    public synchronized static SPUtils get(){
        return utils==null?utils=new SPUtils():utils;
    }

    public  void putString(String name,String value){
        preferences.edit().putString(name,value).apply();
    }
    public  void putInt(String name,int value){
        preferences.edit().putInt(name,value).apply();
    }

    public void putBoolean(String name,boolean value){
        preferences.edit().putBoolean(name,value).apply();
    }

    public boolean getBoolean(String name,boolean defaultValue){
        return preferences.getBoolean(name,defaultValue);
    }

    public  String getString(String name,String defaultValue){
        return preferences.getString(name,defaultValue);
    }

    public  int getInt(String name,int defaultValue){
        return preferences.getInt(name,defaultValue);
    }

    //移除保存的数据
    public  void remove(String name){
        preferences.edit().remove(name).apply();
    }

    public  void setUser(String user){
        preferences.edit().putString(Config.USER,user).apply();
    }
    public  UserEntity getUser(){
        String login_user = preferences.getString(Config.USER,"");
        if(TextUtils.isEmpty(login_user)){
            return null;
        }else{
            return new Gson().fromJson(login_user, UserEntity.class);
        }
    }

    public boolean isLogin(){
        return preferences.getBoolean(Config.IS_LOGIN,false);
    }

    public int getUid(){
        return preferences.getInt(Config.UID,0);
    }
    public String getToken(){
        return preferences.getString(Config.TOKEN,null);
    }
}
