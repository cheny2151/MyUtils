package algorithm.workday;

import java.time.LocalDate;
import java.util.Date;

/**
 * 工作日计算接口
 *
 * @author by chenyi
 * @date 2021/8/2
 */
public interface WorkdayCounter {

    /**
     * 计算工作日偏移日期
     *
     * @param date   开始日期
     * @param offset 偏移天数
     * @return 计算结果日期
     */
    Date count(Date date, int offset);

    /**
     * 计算工作日偏移日期
     *
     * @param date   开始日期
     * @param offset 偏移天数
     * @return 计算结果日期
     */
    LocalDate count(LocalDate date, int offset);

}
