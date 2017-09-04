package com.haizhi.base;

import com.haizhi.util.HttpUtils;
import com.haizhi.util.PropertyUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.ignite.lang.IgniteRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by youfeng on 2017/9/4.
 * 工商运行基本库
 */
public class GsxtRunnable implements IgniteRunnable {

    private static final Logger logger = LoggerFactory.getLogger(GsxtRunnable.class);


    private String province;
    private String company;
    private String proxyUrl;
    //private String host;

    public GsxtRunnable(String province, String company) {
        this.province = province;
        this.company = company;
        this.proxyUrl = PropertyUtil.getProperty("proxy.url");
    }

    //获取代理信息
    private String getProxy() {

        String url = proxyUrl + "gs.gsxt.gov.cn";
        CloseableHttpClient closeableHttpClient = HttpUtils.createHttpClient();
        String result = HttpUtils.get(closeableHttpClient, url, null, 5);

        if (result != null) {
            logger.info(result);
        }
        return result;
    }

//    // 获取代理
//    private String getProxy() {
//        return "fasdf";
//    }

    @Override
    public void run() {
        logger.info("开始抓取: {} {}", province, company);

        String proxy = getProxy();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("休眠被中断: ", e);
        }

        logger.info("抓取完成: {} {}", province, company);
    }
}
