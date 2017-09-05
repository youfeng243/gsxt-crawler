package com.haizhi.util;

/**
 * Created by youfeng on 2017/9/5.
 * 存储配置信息
 */
public class Config {
    public static String PROXY_URL;

    public static void init() {
        PROXY_URL = PropertyUtil.getProperty("proxy.url");
    }
}
