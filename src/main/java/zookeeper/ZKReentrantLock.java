package zookeeper;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * zookeeper实现的分布式可重入锁
 */
public class ZKReentrantLock implements Serializable {

    private static final long serialVersionUID = -1066035320340951704L;

    /**
     * 锁节点
     */
    private String lockPath;

    /**
     * k:path
     * v:thread
     * 存放阻塞的线程
     */
    private ConcurrentHashMap<String, Thread> threadMap = new ConcurrentHashMap<>();

    public ZKReentrantLock(String lockPath) {
        this.lockPath = lockPath;
    }

    public final void lock() {
//        if (CreateAndCheckSmall()) {
//
//        }
    }

    private String CreateAndCheckSmall() {
        ZkApi.getZK();
        return "";
    }

    private boolean compareAndSet() {
        return false;
    }


}
