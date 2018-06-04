package zookeeper;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

public class ZkTest {

    ZKReentrantLock lock = new ZKReentrantLock("/lock", "test");

    @Test
    public void test() throws InterruptedException {
        new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(3000);
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(10000000);
    }

}
