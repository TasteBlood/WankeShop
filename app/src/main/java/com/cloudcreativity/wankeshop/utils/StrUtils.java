package com.cloudcreativity.wankeshop.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这是自己定义的字符串处理工具
 */
public class StrUtils {
    /**
     *
     * @param number 数字
     * @return 在前面补零返回数据
     */
    public static String addZeroFormat(int number){
        if(number<10){
            return "0"+number;
        }
        return String.valueOf(number);
    }
    public static boolean isPhone(String phone){
        String regExp = "^((13[0-9])|(15[^4])|(18[0-3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
