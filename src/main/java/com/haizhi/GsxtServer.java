package com.haizhi;

import com.haizhi.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by youfeng on 2017/9/4.
 * 工商抓取启动入库
 */
public class GsxtServer {
    private static final Logger logger = LoggerFactory.getLogger(GsxtServer.class);

    static {
        //先装在配置信息
        PropertyUtil.loadProperties();
        logger.info("加载配置完成...");
    }

    public static void main(String... args) {
        logger.info("启动工商抓取程序...");
    }
}
