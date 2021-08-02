package algorithm.workday;


import algorithm.workday.storeage.SkipListWorkdayStorage;
import algorithm.workday.storeage.WorkdayStorage;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 文件数据导入工作日以进行计算
 *
 * @author by chenyi
 * @date 2021/8/2
 */
public class FileWorkdayCounter implements WorkdayCounter {

    private final static String DATE_FORMAT = "yyyyMMdd";

    private final static ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_FORMAT));

    private WorkdayStorage workdayStorage;

    public FileWorkdayCounter(InputStream inputStream, String split) throws IOException {
        workdayStorage = new SkipListWorkdayStorage(true);
        loadData(inputStream, split);
    }

    public FileWorkdayCounter(File file, String split) throws IOException {
        this(new FileInputStream(file), split);
    }

    @Override
    public Date count(Date date, int offset) {
        int dateIntVal = workdayStorage.offset(date, offset);
        try {
            return SIMPLE_DATE_FORMAT_THREAD_LOCAL.get().parse(String.valueOf(dateIntVal));
        } catch (ParseException e) {
            throw new RuntimeException("日期解析异常", e);
        }
    }

    @Override
    public LocalDate count(LocalDate date, int offset) {
        int dateIntVal = workdayStorage.offset(date, offset);
        return LocalDate.parse(String.valueOf(dateIntVal), DateTimeFormatter.BASIC_ISO_DATE);
    }

    private void loadData(InputStream inputStream, String split) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] dates = line.split(split);
            for (String date : dates) {
                this.workdayStorage.store(Integer.parseInt(date));
            }
        }
    }

    public WorkdayStorage getWorkdayStorage() {
        return workdayStorage;
    }

    public void setWorkdayStorage(WorkdayStorage workdayStorage) {
        this.workdayStorage = workdayStorage;
    }
}
