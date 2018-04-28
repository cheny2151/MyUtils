package thread.pool;

import org.junit.Test;

import java.util.LinkedList;

/**
 * 模拟从连接池中获取，释放连接
 */
public class ConnectionPool {

    private final static Object key = new Object();

    private static LinkedList<String> pool = new LinkedList<>();

    static {
        pool.push("connection0");
        pool.push("connection1");
        pool.push("connection2");
        pool.push("connection3");
        pool.push("connection4");
    }

    public static String getConnection(Long outtime) {
        synchronized (key) {
            String connection = pool.poll();
            if (connection == null) {
                try {
                    System.out.println("连接池为空，等待归还");
                    key.wait(outtime);
                    connection = pool.poll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return connection;
        }
    }

    public static void releaseConnection(String connection) {
        if (connection != null) {
            synchronized (key) {
                pool.push(connection);
                key.notify();
            }
        }
    }

    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                String connection = getConnection(1000L);
                System.out.println("获取连接:" + connection);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                releaseConnection(connection);
            }).start();
        }
        Thread.sleep(3000);
    }


}
