package thread.queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedBlockingQueue {

    /**
     * 生产者锁
     */
    private ReentrantLock putLock;

    private Condition fullSignal;

    /**
     * 消费者锁
     */
    private ReentrantLock takeLock;

    private Condition emptySignal;

}
