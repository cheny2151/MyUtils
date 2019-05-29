package mq.kafka.utils;

import lombok.extern.slf4j.Slf4j;
import mq.kafka.producer.Producer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author cheney
 * @date 2019/5/29
 */
@Slf4j
public class KafkaUtils {

    private static Properties defaultProperties;

    static {
        InputStream inputStream = Producer.class.getClassLoader().getResourceAsStream("kafka.properties");
        defaultProperties = new Properties();
        try {
            defaultProperties.load(inputStream);
        } catch (IOException e) {
            log.error("读取默认配置失败", e);
        }
    }

    /**
     * 获取默认配置
     */
    public static Properties getDefaultProperties() {
        return defaultProperties;
    }

    /**
     * 克隆默认配置
     */
    public static Properties cloneDefaultProperties() {
        Properties properties = new Properties();
        properties.putAll(defaultProperties);
        return properties;
    }

}
