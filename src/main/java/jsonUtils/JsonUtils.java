package jsonUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JsonUtils {

    private static ObjectMapper objectMapper;

    private static int count = 0;

    //懒加载同步锁方式
    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            initObjectMapper();
        }
        return objectMapper;
    }

    private synchronized static void initObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    public JsonUtils() {
    }

    /**
     * 转化为json格式
     */
    public static String toJson(Object object) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转化为bean
     */
    public static <T> T toJavaBean(String json, Class<T> type) {
        try {
            return getObjectMapper().readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转化为map
     */
    public static Map<?,?> toMap(String json) {
        try {
            return getObjectMapper().readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
