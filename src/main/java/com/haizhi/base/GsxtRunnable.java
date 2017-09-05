package com.haizhi.base;

import com.haizhi.tools.ChromeDowner;
import com.haizhi.util.Config;
import com.haizhi.util.HttpUtils;
import com.haizhi.util.ProxyUtils;
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

    public void work() {
        //获取代理信息
        String proxyStr = ProxyUtils.getProxy(host);
        if (proxyStr == null) {
            logger.warn("获取代理失败，不进行抓取任务...");
            return;
        }

        //封装代理信息
        HttpProxy httpProxy = HttpProxy.build(proxyStr);
        logger.info("获取到的代理信息为: {}", httpProxy.toStringUserAndpwd());

        //初始化浏览器
        try {
            ChromeDowner chrome = new ChromeDowner(60000, company, httpProxy);
            chrome.insideDown("http://www.gsxt.gov.cn/index.html");
            chrome.insideColose();
        } catch (Exception e) {
            logger.error("浏览器异常: ", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error("休眠被中断: ", e);
        }
    }

    @Override
    public void run() {
        logger.info("开始抓取: {} {}", province, company);

        work();

        logger.info("抓取完成: {} {}", province, company);
    }
}
