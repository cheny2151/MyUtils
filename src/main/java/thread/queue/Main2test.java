package thread.queue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
//import java.util.concurrent.LinkedBlockingQueue;

public class Main2test {

    @Test
    public void test() throws InterruptedException {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(1000);
        for (int i = 0; i < 1000; i++) {
            queue.put(i + "");
        }
        final HashMap<String, Integer> count = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            count.put(i + "", 0);
        }
        new Thread(() -> {
            while (queue.size() != 0) {
                try {
                    String take = queue.take();
                    count.put(take, count.get(take) + 1);
                    System.out.println(Thread.currentThread().getName() + ":" + take);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (queue.size() != 0) {
                try {
                    String take = queue.take();
                    count.put(take, count.get(take) + 1);
                    System.out.println(Thread.currentThread().getName() + ":" + take);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    String take = queue.take();
//                    count.put(take, count.get(take) + 1);
                    System.out.println(Thread.currentThread().getName() + ":" + take);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey());
            }
        }
        Thread.sleep(1000);
        queue.put("test");
        Thread.sleep(1000);
    }

}
