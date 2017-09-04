package com.haizhi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertyUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    private static Properties props;

    private PropertyUtil() {

    }

    public static void loadProperties() {

        props = new Properties();

        // 初始化配置信息
        initProperties(props);
    }

    private static void initProperties(Properties properties) {
        if (properties == null) {
            logger.error("参数有误...");
            return;
        }

        InputStreamReader reader = null;

        try {
            reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"), "utf-8");
            properties.load(reader);
            logger.info("load {} ok", "application.properties");
        } catch (Exception e) {
            logger.error("装载配置文件报错！", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("关闭输入流出错:", e);
                }
            }
        }

    }

    public static String getProperty(String key) {
        logger.info("[getProperty] key->{}, value->{}", key, props.getProperty(key));
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        logger.info("[getProperty] key->{}, defaultValue->{}, value->{}", key, defaultValue, props.getProperty(key));
        return props.getProperty(key, defaultValue);
    }

    public static Integer getInt(String key) {
        logger.info("[getInt] key->{}, value->{}", key, Integer.parseInt(props.getProperty(key)));
        return Integer.parseInt(props.getProperty(key));
    }

    public static Long getLong(String key) {
        logger.info("[getInt] key->{}, value->{}", key, Integer.parseInt(props.getProperty(key)));
        return Long.parseLong(props.getProperty(key));
    }

    public static Long getLong(String key, long defaultValue) {
        logger.info("[getLong] key->{}, defaultValue->{}, value->{}", key, defaultValue, Long.parseLong(props.getProperty(key, String.valueOf(defaultValue))));
        return Long.parseLong(props.getProperty(key, String.valueOf(defaultValue)));
    }

    public static Integer getInt(String key, String defaultValue) {
        logger.info("[getInt] key->{}, defaultValue->{}, value->{}", key, defaultValue, Integer.parseInt(props.getProperty(key, defaultValue)));
        return Integer.parseInt(props.getProperty(key, defaultValue));
    }

    public static Boolean getBoolean(String key) {
        logger.info("[getBoolean] key->{}, value->{}", key, Boolean.valueOf(props.getProperty(key)));
        return Boolean.valueOf(props.getProperty(key));
    }

    public static Boolean getBoolean(String key, String defaultValue) {
        logger.info("[getBoolean] key->{}, defaultValue->{}, value->{}", key, defaultValue, Boolean.valueOf(props.getProperty(key, defaultValue)));
        return Boolean.valueOf(props.getProperty(key, defaultValue));
    }
}
