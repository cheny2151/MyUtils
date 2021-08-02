package algorithm.workday.storeage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author by chenyi
 * @date 2021/8/2
 */
public abstract class BaseWorkdayStorage implements WorkdayStorage {

    public static final String PATTERN = "yyyyMMdd";

    protected Date cover(int date) {
        try {
            return new SimpleDateFormat(PATTERN).parse(String.valueOf(date));
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期解析异常,date:" + date, e);
        }
    }

    protected int cover(Date date) {
        return Integer.parseInt(new SimpleDateFormat(PATTERN).format(date));
    }

}
