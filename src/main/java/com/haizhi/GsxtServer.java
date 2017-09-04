package com.haizhi;

import com.haizhi.util.PropertyUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteRunnable;
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

        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            System.out.println();
            System.out.println("Compute runnable example started.");

            IgniteCompute compute = ignite.compute();

            // Iterate through all words in the sentence and create runnable jobs.
            for (final String word : "Print words using runnable".split(" ")) {
                // Execute runnable on some node.
                compute.run((IgniteRunnable) () -> {
                    System.out.println();
                    System.out.println(">>> Printing '" + word + "' on this node from ignite job.");
                });
            }

            System.out.println();
            System.out.println(">>> Finished printing words using runnable execution.");
            System.out.println(">>> Check all nodes for output (this node is also part of the cluster).");
        }

    }
}
