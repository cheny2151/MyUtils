package redis.subscribe;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Publish {

    private JedisPool jedisPool = new JedisPool();

    @Test
    public void subscribe(){
        Jedis jedis = jedisPool.getResource();
        JedisSubscribe subscribe = new JedisSubscribe();
        new Thread(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subscribe.punsubscribe();
        }).start();
        jedis.psubscribe(subscribe,"test.*");
    }

    @Test
    public void publish(){
        jedisPool.getResource().publish("test.1234","success");
    }

}
