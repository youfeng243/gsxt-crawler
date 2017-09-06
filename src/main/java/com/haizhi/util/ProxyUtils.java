package com.haizhi.util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by youfeng on 2017/9/5.
 * 代理
 */
public class ProxyUtils {

    private static final Logger logger = LoggerFactory.getLogger(ProxyUtils.class);

    //获取代理信息
    public static String getProxy(String host) {
        String url = "http://172.18.180.225:9300/proxy/" + host;
        logger.info("当前访问url = {}", url);
        CloseableHttpClient closeableHttpClient = HttpUtils.createHttpClient();
        String result = HttpUtils.get(closeableHttpClient, url, 5, null);
        try {
            closeableHttpClient.close();
        } catch (IOException e) {
            logger.error("关闭httpclient失败: ", e);
        }
        return result;
    }
}
