package thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子性操作工具包：java.util.concurrent.atomic
 */
public class CAS {

    private int x;
    private AtomicInteger y = new AtomicInteger(0);

    @Test
    public void test() throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int y = 0; y < 100; y++) {
                    count();
                    safeCount();
                }
            });
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.start();
        }
        Thread.sleep(2000);
        System.out.println("x" + x);
        System.out.println("y" + y.get());
    }

    private void safeCount() {
        int current = y.get();
        for (; ; ) {
            //CAS
            if (y.compareAndSet(current, ++current)) {
                break;
            }
        }

    }

    private void count() {
        int empty = x;
        empty = empty + 1;
        x = empty;
    }

}
