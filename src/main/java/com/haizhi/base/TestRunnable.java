package com.haizhi.base;

import org.apache.ignite.lang.IgniteRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by youfeng on 2017/9/4.
 * 工商运行基本库
 */
public class TestRunnable implements IgniteRunnable {

    private static final Logger logger = LoggerFactory.getLogger(TestRunnable.class);

    private int sleepTime = 0;

    public TestRunnable(int sleepTime) throws Exception {
        this.sleepTime = sleepTime;
    }


    @Override
    public void run() {
        logger.info("开始休眠: {}", sleepTime);

        try {
            Thread.sleep(this.sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("休眠完成: {}", sleepTime);
    }
}
