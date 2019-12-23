package expression.cheney.test;

import jsonUtils.JsonUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cheney
 * @date 2019-12-10
 */
public class TestFunction {

    public static String noArg() {
        return "yyyy-MM-dd";
    }

    public static String getString(Object test1, Object... test) {
        System.out.println(test1);
        System.out.println(JsonUtils.toJson(test));
        return "yyyy-MM-dd";
    }

    public static String date_format(Date date, String format) {
        return DateFormatUtils.format(date, format);
    }

    public static Object ifs(Object... objs) {
        System.out.println(JsonUtils.toJson(objs));
        for (int i = 0; i < objs.length; i++) {
            if ((i & 1) == 0 && (boolean) objs[i]) {
                return objs[i + 1];
            }
        }
        return "error";
    }

    public static boolean contains(String text, String content) {
        return text.contains(content);
    }

    public static BigDecimal abs(Object value) {
        return new BigDecimal(value.toString()).abs();
    }

}
