package com.haizhi;

import com.haizhi.base.GsxtRunnable;
import com.haizhi.base.TestRunnable;
import com.haizhi.mongo.Mongo;
import com.haizhi.util.Config;
import com.haizhi.util.PropertyUtil;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteRunnable;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;

/**
 * Created by youfeng on 2017/9/4.
 * 工商抓取启动入库
 */
public class TestServer {
    private static final Logger logger = LoggerFactory.getLogger(TestServer.class);

    static {
        //先装在配置信息
        PropertyUtil.loadProperties();
        Config.init();
        logger.info("加载配置完成...");
    }

    public static void main(String... args) {
        logger.info("启动工商抓取程序...");
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            logger.info("启动任务调度...");

            IgniteCompute compute = ignite.compute();

            List<IgniteRunnable> task_list = new ArrayList<>();

            task_list.add(new TestRunnable(25000));
            task_list.add(new TestRunnable(20000));
            task_list.add(new TestRunnable(10000));
            task_list.add(new TestRunnable(5000));
            task_list.add(new TestRunnable(1000));

            compute.runAsync(task_list).get();

        } catch (Exception e) {
            logger.error("任务调度异常: ", e);
        }

        logger.info("退出工商抓取程序...");
    }
}
