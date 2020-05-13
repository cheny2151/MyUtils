package zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeperAPI封装
 */
@Slf4j
public class ZkClient {

    private Logger logger = Logger.getLogger(this.getClass());

    private final static class ZookeeperHolder {

        private static ZooKeeper ZOO_KEEPER;

        private static CountDownLatch latch;

        static {
            latch = new CountDownLatch(1);
            try {
                ZOO_KEEPER = new ZooKeeper(ZookeeperConfig.ZK_CONNECTSTRING, ZookeeperConfig.SESSION_TIMEOUT, (event) -> {
                    if (Watcher.Event.KeeperState.SyncConnected.equals(event.getState())) {
                        latch.countDown();
                    }
                    log.info("zk start event:{}", event.toString());
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void waitForConnection() throws InterruptedException {
            latch.await();
        }

    }

    public ZooKeeper getZK() {
        try {
            ZookeeperHolder.waitForConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ZookeeperHolder.ZOO_KEEPER;
    }

    /**
     * 创建节点，父节点不存在则自动创建
     * 注意：临时节点不允许创建节点
     */
    public String createNodeAndParentIfNeed(String path, byte[] data, List<ACL> acls, CreateMode createMode) {
        //第一个/忽略
        int start = 1;
        //记录/的index
        int i;
        boolean exist = true;
        ZooKeeper zk = getZK();

        try {
            while ((i = path.indexOf("/", start)) != -1) {
                String parent = path.substring(0, i);
                //上一个节点存在才需判断下个节点是否存在（上个节点不存在，下一个节点一定不存在）
                if (!exist || !(exist = (zk.exists(parent, false) != null))) {
                    String p = getZK().create(parent, "".getBytes(), acls, CreateMode.PERSISTENT);
                    logger.info("parent node be create:" + p);
                }
                start = i + 1;
            }
            return getZK().create(path, data, acls, createMode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void printNodes(String nodePath) throws KeeperException, InterruptedException {
        List<String> children = getZK().getChildren(nodePath, true);
        if (children.size() > 0) {
            StringBuilder stringBuffer = new StringBuilder();
            for (String c : children) {
                stringBuffer.append(c).append(",");
            }
            String print = stringBuffer.subSequence(0, stringBuffer.length() - 1).toString();
            System.out.println(print);
            return;
        }
        System.out.println("null");
    }

    @Test
    public void test() throws InterruptedException {
        ZkClient zkClient = new ZkClient();
        String s = zkClient.createNodeAndParentIfNeed("/test/child", "".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(s);
        Thread.sleep(100000);
    }

    @Test
    public void test2() throws KeeperException, InterruptedException {
        for (; ; ) {
            printNodes("/test");
            Thread.sleep(3000);
        }
    }

}
