package com.easydata.core.constants;

/**
 * 常用全局变量。<br />
 *
 * @author Mr.Pro
 */
public class Constants {
    /**
     * 缓存时效 1天
     */
    public static final int CACHE_EXP_DAY = 3600 * 24;

    /**
     * 缓存时效 1周
     */
    public static final int CACHE_EXP_WEEK = 3600 * 24 * 7;

    /**
     * 缓存时效 1月
     */
    public static final int CACHE_EXP_MONTH = 3600 * 24 * 30 * 7;

    /**
     * 缓存时效 永久
     */
    public static final int CACHE_EXP_FOREVER = 0;



    public static final int STATUS_ENABLED = 1;// 启用
    public static final int STATUS_DISABLE = 0;// 禁用
}
