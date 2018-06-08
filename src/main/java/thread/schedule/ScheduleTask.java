package thread.schedule;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledThreadPoolExecutor 定时器
 */
public class ScheduleTask {

    private Date date = new Date();

    @Test
    public void test() throws InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
        executor.scheduleAtFixedRate(() -> {
            Date now = new Date();
            long l = (now.getTime() - date.getTime()) / 1000;
            date = now;
            System.out.println("running..." + l);
        }, 1, 2, TimeUnit.SECONDS);
        Thread.sleep(100000);
    }

}
