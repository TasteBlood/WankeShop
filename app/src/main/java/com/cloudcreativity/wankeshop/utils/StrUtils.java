package com.cloudcreativity.wankeshop.utils;

import java.math.BigDecimal;
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

    /**
     *
     * @param phone 电话号码
     * @return 是否
     */
    public static boolean isPhone(String phone){
        String regExp = "^((13[0-9])|(15[^4])|(18[0-3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     *
     * @param email 邮箱
     * @return 是否
     */
    public static boolean isEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     *
     * @param num 身份证号
     * @return 是否
     */
    public static boolean isIDCard(String num) {
        String reg = "^\\d{17}[0-9Xx]$";
        boolean matches = num.matches(reg);
        boolean verify = verify(num.toCharArray());
        return matches&verify;//别问我为什么，这是
    }

    //身份证最后一位的校验算法
    private static boolean verify(char[] id) {
        int sum = 0;
        int w[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        char[] ch = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
        for (int i = 0; i < id.length - 1; i++) {
            sum += (id[i] - '0') * w[i];
        }
        int c = sum % 11;
        char code = ch[c];
        char last = id[id.length-1];
        last = last == 'x' ? 'X' : last;
        return last == code;
    }

    //获取两位小数，四舍五入
    public static float get2BitDecimal(float number){
        BigDecimal b   =   new   BigDecimal(number);
        return b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
