package com.cloudcreativity.wankeshop.utils;

/**
 * 这个app的属性配置
 */
public class AppConfig {
    /**
     * 是否是开发调试阶段
     */
    public static boolean DEBUG = false;
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
     * 这是APP热更新的下载缓存目录
     */
    public static final String APP_HOT_UPDATE_FILE = "wanke_app_hot_update.apk";
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

    /**
     * 这是微信的app_id
     */
    public static String WX_APP_ID = "wx93fee6f47dbda966";
    /**
     * 这是微信的app_secret
     */
    public static String WX_APP_SECRET = "2f97bbc403d48852c783ef484776b8eb";

    /**
     * 这是扫描二维码的图片路径
     */
    public static String PATH_QR_CODE = "wk_qr_code_path";
}
