package com.haizhi;

import com.haizhi.base.GsxtRunnable;
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

/**
 * Created by youfeng on 2017/9/4.
 * 工商抓取启动入库
 */
public class GsxtServer {
    private static final Logger logger = LoggerFactory.getLogger(GsxtServer.class);

    static {
        //先装在配置信息
        PropertyUtil.loadProperties();
        Config.init();
        logger.info("加载配置完成...");
    }

    public static void main(String... args) {
        logger.info("启动工商抓取程序...");
        int taskPoolNum = PropertyUtil.getInt("task.pool.num");
        String mongoHost = PropertyUtil.getProperty("mongo.host");
        String username = PropertyUtil.getProperty("mongo.username");
        String password = PropertyUtil.getProperty("mongo.password");
        String authdb = PropertyUtil.getProperty("mongo.auth.db");
        Mongo mongo = new Mongo(mongoHost, username, password, authdb);
        MongoDatabase database = mongo.getDb(PropertyUtil.getProperty("mongo.database"));
        MongoCursor<Document> cursor = database.getCollection(PropertyUtil.getProperty("mongo.collection")).
                find(eq("province", "gansu")).batchSize(100).iterator();
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            logger.info("启动任务调度...");

            IgniteCompute compute = ignite.compute();

            List<IgniteRunnable> task_list = new ArrayList<>();

            //开始遍历数据库..
            while (cursor.hasNext()) {
                Document document = cursor.next();

                //获得省份信息
                String province = document.getString("province");

                //获得企业信息
                String company = document.getString("company_name");

                //获得host信息
                String host = PropertyUtil.getProperty("host." + province);

                if (province == null || company == null || host == null) {
                    logger.error("抓取参数错误: " + province + " " + company + " " + host);
                    continue;
                }

                IgniteRunnable task = new GsxtRunnable(province, company, host);
                task_list.add(task);

                // 如果已经添加10个任务了 则先执行完成 再继续
                if (task_list.size() >= taskPoolNum) {
                    compute.runAsync(task_list).get();
                    task_list.clear();
                }
            }
            if (task_list.size() > 0) {
                compute.runAsync(task_list).get();
            }
        } catch (Exception e) {
            logger.error("任务调度异常: ", e);
        }

        cursor.close();
        mongo.close();
        logger.info("退出工商抓取程序...");
    }
}
