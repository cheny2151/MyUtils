package thread.countDownLatch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch是一种同步手段，允许一个或者更多的线程等待，直到在其他线程正在执行的一组操作完成。
 * 给定count数目后CountDownLatch被初始化。await()方法阻塞，直到由于调用countDown()方法，当前count值达到0，
 * 之后所有等待线程被释放，而任何后续await()方法的调用会立即返回。这个是只有一次的现场，即count值无法被重设。
 * 如果你需要一个能够重设count值的版本，不妨考虑使用CyclicBarrier。
 * <p>
 * https://blog.csdn.net/lipeng_bigdata/article/details/53556739
 */
public class CountDownLatchTest {

    @Test
    public void main() throws InterruptedException {

        CountDownLatch start = new CountDownLatch(1);

        CountDownLatch complete = new CountDownLatch(6);

        ExecutorService threadPool = Executors.newCachedThreadPool();
        ArrayList<String> strings = new ArrayList<>();
        worker worker = new worker(start, complete, strings);
        threadPool.execute(worker);
        //模拟准备工作，worker阻塞等等countDown()
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        strings.add("6");
        Thread.sleep(1000);
        start.countDown();
        //主线程等待worker的complete计数器减为0
        complete.await();
    }

    private static class worker implements Runnable {

        private CountDownLatch start;

        private volatile List<String> works;

        private CountDownLatch complete;

        public worker(CountDownLatch start, CountDownLatch complete, List<String> works) {
            this.start = start;
            this.works = works;
            this.complete = complete;
        }

        @Override
        public void run() {
            try {
                System.out.println("wait for start");
                start.await();
                System.out.println("start");
                doWork(works);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void doWork(List<String> works) throws InterruptedException {
            for (String s : works) {
                Thread.sleep(300);
                System.out.println(s);
                complete.countDown();
            }
        }

    }

}
