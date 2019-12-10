package expression.cheney.test;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * @author cheney
 * @date 2019-12-10
 */
public class TestFunction {

    public static String getString() {
        return "yyyy-MM-dd";
    }

    public static String date_format(Date date, String format) {
        return DateFormatUtils.format(date, format);
    }

}
