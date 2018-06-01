package zookeeper;

import jsonUtils.JsonUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZkApi {

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
                    System.out.println(JsonUtils.toJson(event));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void waitForConnection() throws InterruptedException {
            latch.await();
        }

    }

    public static ZooKeeper getZK() {
        try {
            ZookeeperHolder.waitForConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ZookeeperHolder.ZOO_KEEPER;
    }

    public String createAsloNoNode(String path, byte[] data, List<ACL> acls, CreateMode createMode) {
        try {
            getZK().create(path, data, acls, createMode);
        } catch (KeeperException.NoNodeException e) {
            //try to create no exists node
            createAsloNoNode(path.substring(0, path.lastIndexOf("/")), data, acls, createMode);
            createAsloNoNode(path, data, acls, createMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void test() throws KeeperException, InterruptedException {
//        for (; ; ) {
        String s = getZK().create("/path1/test", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(s);
//            Thread.sleep(3000);
//        }
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
    public void test2() throws KeeperException, InterruptedException {
        for (; ; ) {
            printNodes("/testNode");
            Thread.sleep(3000);
        }
    }

    @Test
    public void curator() throws Exception {
        //1 重试策略：初试时间为1s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(ZookeeperConfig.ZK_CONNECTSTRING)
                .connectionTimeoutMs(ZookeeperConfig.SESSION_TIMEOUT)
                .retryPolicy(retryPolicy)
                .build();

        curator.start();

        byte[] bytes = curator.getData().forPath("/testNode");
    }

}
