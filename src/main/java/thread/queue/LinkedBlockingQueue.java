package thread.queue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedBlockingQueue<V> {

    /**
     * 生产者锁
     */
    private ReentrantLock putLock = new ReentrantLock();

    private Condition notFull = putLock.newCondition();

    /**
     * 消费者锁
     */
    private ReentrantLock takeLock = new ReentrantLock();

    private Condition notEmpty = takeLock.newCondition();

    private AtomicInteger count = new AtomicInteger();

    private final int capacity;

    private volatile node<V> head;

    private volatile node<V> last;

    public int size() {
        return count.get();
    }

    /**
     * 存储数据的节点
     *
     * @param <V>
     */
    private static class node<V> {

        private V value;

        private node<V> next;

        public node(V value) {
            this.value = value;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public node<V> getNext() {
            return next;
        }

        public void setNext(node<V> next) {
            this.next = next;
        }
    }

    public LinkedBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    private void signalNotEmpty() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    private void signalNotFull() {
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    private void enqueue(V v) {
        node<V> node = new node<>(v);
        node<V> last = this.last;
        while (!compareAndSetLast(last, node)) {
        }
    }

    private synchronized boolean compareAndSetLast(node<V> expect, node<V> node) {
        node nowLast = this.last;
        if (expect != nowLast) {
            return false;
        }
        if (head == null) {
            head = last = node;
        } else {
            last = last.next = node;
        }
        return true;
    }

    /**
     * 思考：为什么不用加synchronized，由于是操作对象引用，而不是值。
     * 明确h与head始终指向一个内存地址,所以无论head是否被其他线程修改，h始终和head保持同个引用，不存在线程安全问题。
     *
     * @return
     */
    private V dequeue() {
        node<V> h = head;
        V v = h.getValue();
        head = h.next;
        h.next = null;
        return v;
    }

    public void put(V v) {

        if (v == null) {
            throw new NullPointerException();
        }

        int c = -1;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;

        putLock.lock();
        try {
            while (count.get() == capacity) {
                notFull.await();
            }
            enqueue(v);
            c = count.getAndIncrement();
            if (c + 1 < capacity) {
                notFull.signal();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            putLock.unlock();
        }
        //put前为空，通知notEmpty
        if (c == 0) {
            signalNotEmpty();
        }
    }

    public boolean offer(V v, long timeout, TimeUnit timeUnit) {

        if (v == null) {
            throw new NullPointerException();
        }

        long time = timeUnit.toNanos(timeout);
        final long start = System.nanoTime();
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        int c = -1;

        putLock.lock();
        try {
            while (count.get() == capacity) {
                time = time - (System.nanoTime() - start);
                if (time <= 0) {
                    return false;
                }
                notFull.awaitNanos(time);
            }
            enqueue(v);
            c = count.getAndIncrement();
            if (c + 1 < capacity) {
                notFull.signal();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            putLock.unlock();
        }

        if (c == 0) {
            signalNotEmpty();
        }
        return true;
    }

    public V take() {

        V result = null;
        final ReentrantLock takeLock = this.takeLock;
        final AtomicInteger count = this.count;
        int c = -1;

        takeLock.lock();
        try {
            while (count.get() == 0) {
                notEmpty.await();
            }
            result = dequeue();
            c = count.getAndDecrement();
            if (c - 1 > 0) {
                notEmpty.signal();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            takeLock.unlock();
        }

        //take前最大，通知notFull
        if (c == capacity) {
            signalNotFull();
        }
        return result;
    }

    public void show() {
        node<V> node = head;
        do {
            System.out.println(node);
        } while ((node = node.next) != null);
    }

}
