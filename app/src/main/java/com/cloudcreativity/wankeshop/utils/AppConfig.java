package com.cloudcreativity.wankeshop.utils;

/**
 * 这个app的属性配置
 */
public class AppConfig {
    /**
     * 是否是开发调试阶段
     */
    public static boolean DEBUG = true;
    /**
     * 网络数据缓存的文件夹名称
     */
    public static final String CACHE_FILE_NAME = "app_cache";
    /**
     * 网络图片缓存的文件夹名称
     */
    public static final String CACHE_IMAGE_NAME = "app_image_cache";
    /**
     * 这是SharePreference的名称
     */
    public static final String SP_NAME = "wanke_shop_app_config";
    /**
     * 这是登录的用户类型
     */
    public static final int USER_TYPE_ONE = 1;//普通
    public static final int USER_TYPE_TWO = 2;//分拣员
    public static final int USER_TYPE_THREE = 3;//物流小二
    public static final int USER_TYPE_FOUR = 4;//站长

    /**
     * 这是统一的文件名
     */
    public static String FILE_NAME = "wk_image_%d.%s";
}
