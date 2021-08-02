package algorithm.workday;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author by chenyi
 * @date 2021/8/2
 */
public class WorkdayUtils {

    private static WorkdayCounter workdayCounter;

    static {
        URL resource = WorkdayUtils.class.getClassLoader().getResource("workday_data.txt");
        if (resource == null) {
            throw new RuntimeException("workday_data.txt不存在，请检查文件");
        }
        try {
            WorkdayUtils.workdayCounter = new FileWorkdayCounter(resource.openStream(), ",");
        } catch (IOException ioException) {
            throw new RuntimeException("加载文件workday_data.txt异常，请检查文件");
        }
    }

    /**
     * 计算工作日偏移日期
     *
     * @param date   开始日期
     * @param offset 偏移天数
     * @return 计算结果日期
     */
    public static Date count(Date date, int offset) {
        return workdayCounter.count(date, offset);
    }

    /**
     * 计算工作日偏移日期
     *
     * @param date   开始日期
     * @param offset 偏移天数
     * @return 计算结果日期
     */
    public static LocalDate count(LocalDate date, int offset) {
        return workdayCounter.count(date, offset);
    }


    // get workday
    public static void main(String[] args) {
       /* // Holidays
        List<Integer> holidays = Arrays.asList(20210101, 20210102, 20210103, 20210211, 20210212, 20210213, 20210214, 20210215, 20210216,
                20210217, 20210403, 20210404, 20210405, 20210501, 20210502, 20210503, 20210504, 20210505, 20210612, 20210613, 20210614
                , 20210919, 20210920, 20210921, 20211001, 20211002, 20211003, 20211004, 20211005, 20211006, 20211007);
        // Working day
        List<Integer> workingDay = Arrays.asList(20210207, 20210220, 20210425, 20210508, 20210918, 20210926, 20211009);

        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate end = LocalDate.of(2022, 1, 1);
        StringBuilder result = new StringBuilder();
        for (; start.compareTo(end) < 0; start = start.plusDays(1)) {
            int date = Integer.parseInt(start.format(DateTimeFormatter.BASIC_ISO_DATE));
            if (holidays.contains(date)) {
                continue;
            }
            int dayOfWeek = start.getDayOfWeek().getValue();
            if (workingDay.contains(date) || !(dayOfWeek == 6 || dayOfWeek == 7)) {
                result.append(date).append(",");
            }
        }*/
        LocalDate count = WorkdayUtils.count(LocalDate.of(2023,1,20), -3311);
//        System.out.println(new SimpleDateFormat("yyyyMMdd").format(count));
        System.out.println(count.format(DateTimeFormatter.BASIC_ISO_DATE));
    }

}
