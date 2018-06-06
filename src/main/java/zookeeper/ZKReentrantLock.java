package zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * zookeeper实现的分布式可重入锁
 */
public class ZKReentrantLock implements Serializable {

    private Logger logger = Logger.getLogger(this.getClass());

    private static final long serialVersionUID = -1066035320340951704L;

    private final ZkClient zkClient;

    /**
     * 锁节点
     * lockPath = rootPath + lockName;
     */
    private String lockPath;

    /**
     * 锁root节点
     */
    private String rootPath;

    /**
     * 锁名称
     */
    private String lockName;

    /**
     * 锁节点长度
     */
    private int length;

    /**
     * 当前获取到锁的节点
     */
    private volatile String currentNode;

    /**
     * 当前获取到锁的线程
     */
    private volatile Thread currentThread;

    /**
     * 重入次数
     * 由于计数的只有当前线程没有并发，所以无需原子操作
     */
    private volatile int count = 0;


    public ZKReentrantLock(String rootPath, String lockName) {
        if (lockName.contains("/")) {
            throw new IllegalArgumentException("lockName can't have '/'");
        }
        int rootLen = rootPath.length();
        if (rootPath.substring(rootLen - 1).equals("/")) {
            rootPath = rootPath.substring(0, rootLen - 1);
        }

        this.lockName = lockName;
        this.rootPath = rootPath;
        this.lockPath = rootPath + "/" + lockName;
        this.length = lockPath.length();
        zkClient = new ZkClient();
    }

    public final void lock() {
        Thread current = Thread.currentThread();
        if (current == currentThread || CreateNextAndCheckSmallest()) {
            count++;
        } else {
            logger.info("park thread: " + current.getName());
            LockSupport.park(this);
        }
    }

    private boolean CreateNextAndCheckSmallest() {
        //创建锁标识（临时节点）
        String zkPath = zkClient.createNodeAndParentIfNeed(lockPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        logger.info("create lock node:" + zkPath);
        return checkSmallest(zkPath);
    }

    private boolean checkSmallest(String zkPath) {
        final Thread current = Thread.currentThread();
        //锁节点编号
        Long newNum = lockNum(zkPath);
        //父节点路径
        String parent = zkPath.substring(0, zkPath.lastIndexOf("/"));

        List<String> children;
        try {
            children = zkClient.getZK().getChildren(parent, false);
            logger.info("children size:" + children.size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //转为runtime异常
            throw new ZKLockException();
        }

        //查找最靠近并比newNum小的节点编号，不存在则newNum最小
        Long pre = null;
        String preNode = null;
        for (String c : children) {
            Long cNum = lockNum(c);
            if (cNum < newNum && (pre == null || cNum > pre)) {
                pre = cNum;
                preNode = c;
            }
        }
        logger.info("smallest:" + preNode);

        //当前线程获取到锁
        if (pre == null) {
            currentNode = zkPath;
            currentThread = Thread.currentThread();
            logger.info("new node is smallest,return true");
            return true;
        }

        try {
            final String finalPreNode = rootPath + "/" + preNode;
            logger.info("exists and listening node:" + finalPreNode);
            zkClient.getZK().exists(finalPreNode, (event) -> {
                logger.info("thread:" + current.getName());
                count++;
                //设置当前执行的线程和对应节点
                currentThread = current;
                currentNode = zkPath;
                LockSupport.unpark(current);
            });

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }

        return false;
    }

    /**
     * 计算锁编号
     */
    private Long lockNum(String zkPath) {
        return Long.valueOf(zkPath.substring(length));
    }

    public void unlock() {
        ZooKeeper zk = zkClient.getZK();
        try {
            count--;
            if (count < 0) {
                throw new ZKLockException();
            } else if (count > 0) {
                return;
            }
            if (zk.exists(currentNode, false) != null) {
                logger.info("delete node:" + currentNode);
                zk.delete(currentNode, -1);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //转为runtime异常
            throw new ZKLockException();
        }
    }

}
