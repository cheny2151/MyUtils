package zookeeper;

import org.apache.log4j.Logger;
import org.junit.Test;

public class ZkTest {

    private Logger logger = Logger.getLogger(this.getClass());

    ZKReentrantLock lock = new ZKReentrantLock("/lock", "test");

    @Test
    public void test() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            lock.lock();
            logger.info("A get lock");
            try {
                for (int i = 0; i < 8; i++) {
                    logger.info("A running...");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                logger.info("A unlock");
            }
        });
        thread1.setName("A");
        thread1.start();
        Thread thread2 = new Thread(() -> {
            lock.lock();
            logger.info("B get lock");
            try {
                lock.lock();
                for (int i = 0; i < 8; i++) {
                    logger.info("B running...");
                    Thread.sleep(500);
                }
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                logger.info("B unlock");
            }
        });
        thread2.setName("B");
        thread2.start();
        Thread thread3 = new Thread(() -> {
            lock.lock();
            logger.info("C get lock");
            try {
                for (int i = 0; i < 8; i++) {
                    logger.info("C running...");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                logger.info("C unlock");
            }
        });
        thread3.setName("C");
        thread3.start();
        Thread.sleep(10000000);
    }

}
