package com.haizhi.base;

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
    //private String host;

    public GsxtRunnable(String province, String company) {
        this.province = province;
        this.company = company;
    }

//    // 获取代理
//    private String getProxy() {
//        return "fasdf";
//    }

    @Override
    public void run() {
        logger.info("开始抓取: {} {}", province, company);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("休眠被中断: ", e);
        }

        logger.info("抓取完成: {} {}", province, company);
    }
}
