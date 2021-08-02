package algorithm.workday.storeage;

import java.time.LocalDate;
import java.util.Date;

/**
 * 工作日存储接口
 *
 * @author by chenyi
 * @date 2021/8/2
 */
public interface WorkdayStorage {

    void store(Date date);

    void store(int date);

    int offset(Date date, int offset);

    int offset(LocalDate date, int offset);

    int offset(int date, int offset);
}
