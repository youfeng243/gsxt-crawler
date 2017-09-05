package com.haizhi.base;

import com.haizhi.util.Config;
import com.haizhi.util.HttpUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.ignite.lang.IgniteRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by youfeng on 2017/9/4.
 * 工商运行基本库
 */
public class GsxtRunnable implements IgniteRunnable {

    private static final Logger logger = LoggerFactory.getLogger(GsxtRunnable.class);


    private String province;
    private String company;
    private String host;

    public GsxtRunnable(String province, String company, String host) throws Exception {
        this.province = province;
        this.company = company;
        this.host = host;
    }

    //获取代理信息
    private String getProxy() {
        String url = Config.PROXY_URL + host;
        CloseableHttpClient closeableHttpClient = HttpUtils.createHttpClient();
        String result = HttpUtils.get(closeableHttpClient, url, 5, null);
        try {
            closeableHttpClient.close();
        } catch (IOException e) {
            logger.error("关闭httpclient失败: ", e);
        }
        return result;
    }


    @Override
    public void run() {
        logger.info("开始抓取: {} {}", province, company);

        //获取代理信息
        String proxyStr = getProxy();
        if (proxyStr == null) {
            logger.warn("获取代理失败，不进行抓取任务...");
            return;
        }

        //封装代理信息
        HttpProxy httpProxy = HttpProxy.build(proxyStr);
        logger.info("获取到的代理信息为: {}", httpProxy.toStringUserAndpwd());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error("休眠被中断: ", e);
        }

        logger.info("抓取完成: {} {}", province, company);
    }
}
