package algorithm.workday.storeage;


import algorithm.skiplist.SkipList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author by chenyi
 * @date 2021/8/2
 */
public class SkipListWorkdayStorage extends BaseWorkdayStorage {

    private final SkipList<Integer> skipList;

    private static EstimateWorkdayStorage estimateWorkdayStorage;

    public SkipListWorkdayStorage(boolean useEstimate) {
        this.skipList = new SkipList<>();
        if (useEstimate) {
            estimateWorkdayStorage = new EstimateWorkdayStorage();
        }
    }

    @Override
    public void store(Date date) {
        int intVal = cover(date);
        store(intVal);
    }

    @Override
    public void store(int date) {
        skipList.add(date);
    }

    @Override
    public int offset(Date date, int offset) {
        int intVal = cover(date);
        return offset(intVal, offset);
    }

    @Override
    public int offset(LocalDate date, int offset) {
        int intVal = Integer.parseInt(date.format(DateTimeFormatter.BASIC_ISO_DATE));
        return offset(intVal, offset);
    }

    @Override
    public int offset(int date, int offset) {
        Integer targetOrPre = skipList.offset(date, offset);
        if (targetOrPre == null) {
            return useEstimate(date, offset);
        }
        return targetOrPre;
    }

    /**
     * 使用估算结果
     *
     * @param date   日期
     * @param offset 偏移量
     * @return 估算结果
     */
    private int useEstimate(int date, int offset) {
        estimateWorkdayStorage.initYearEstimateDate(date / 10000);
        return estimateWorkdayStorage.offset(date, offset);
    }

    public SkipList<Integer> getSkipList() {
        return skipList;
    }
}
