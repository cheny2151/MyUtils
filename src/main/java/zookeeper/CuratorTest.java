package zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

/**
 * @author cheney
 * @date 2020-05-13
 */
public class CuratorTest {

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
        for (; ; ) {
            Stat stat = curator.checkExists().forPath("/testNode");
            System.out.println(stat);
            Thread.sleep(3000);
        }
    }

}
