package expression.cheney.func;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * 表达式执行器内置函数
 *
 * @author cheney
 * @date 2019-12-13
 */
public class InternalFunction {

    public static void println(Object obj) {
        System.out.println(obj);
    }

    public static void print(Object obj) {
        System.out.print(obj);
    }

    public static String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static Map<String, Object> jsonToMap(String json) {
        return JSON.parseObject(json);
    }

    public static List<Object> jsonToList(String json) {
        return JSON.parseArray(json);
    }

}
