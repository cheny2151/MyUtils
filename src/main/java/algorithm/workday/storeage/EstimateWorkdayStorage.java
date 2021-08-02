package algorithm.workday.storeage;


import algorithm.skiplist.SkipList;
import algorithm.workday.WorkdayCountException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 工作日收集网站：https://publicholidays.cn/zh/2022-dates/
 *
 * @author by chenyi
 * @date 2021/8/2
 */
public class EstimateWorkdayStorage extends SkipListWorkdayStorage {

    // Holidays
    private static final List<String> holidays =
            Arrays.asList("0101", "0102", "0103", "0405", "0501", "1001", "1002", "1003", "1004", "1005", "1006", "1007");

    private final List<Integer> initedYear = new ArrayList<>();

    private final static int MAX_TRY_TIME = 2;

    public EstimateWorkdayStorage() {
        super(false);
    }

    /**
     * 初始化指定年份的所有估算工作日
     *
     * @param year 年份 e.g:2022
     */
    public synchronized void initYearEstimateDate(int year) {
        if (initedYear.contains(year)) {
            return;
        }
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year + 1, 1, 1);
        for (; start.compareTo(end) < 0; start = start.plusDays(1)) {
            String date = start.format(DateTimeFormatter.BASIC_ISO_DATE);
            boolean matchHoliday = holidays.stream().anyMatch(date::endsWith);
            if (matchHoliday) {
                continue;
            }
            if (!isWeekend(start)) {
                this.store(Integer.parseInt(date));
            }
        }
        initedYear.add(year);
    }

    @Override
    public int offset(int date, int offset) {
        return tryOffset(date, offset, 0);
    }

    public int tryOffset(int date, int offset, int time) {
        if (time >= MAX_TRY_TIME) {
            throw new WorkdayCountException("工作日估算异常,超出估算范围限制");
        }
        SkipList<Integer> skipList = getSkipList();
        Integer estimateDate = skipList.offset(date, offset);
        if (estimateDate == null) {
            return tryInitOtherYear(date, offset, time);
        }
        return estimateDate;
    }

    /**
     * 由于工作日初始化是以年为单位，解决跨年估算问题，若无跨年进入此方法则抛出异常
     */
    private int tryInitOtherYear(int date, int offset, int time) {
        LocalDate localDate = LocalDate.parse(String.valueOf(date), DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate offsetLocalDate = localDate.plusDays(offset);
        int year = offsetLocalDate.getYear();
        if (localDate.getYear() == year) {
            if (offset > 0) {
                this.initYearEstimateDate(year + 1);
            } else {
                this.initYearEstimateDate(year - 1);
            }
        } else {
            this.initYearEstimateDate(year);
        }
        return tryOffset(date, offset, time + 1);
    }

    private boolean isWeekend(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek == 6 || dayOfWeek == 7;
    }
}
