package redis.subscribe;

import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPubSub;

/**
 * 订阅者 -------- 继承JedisPubSub
 */
public class JedisSubscribe extends JedisPubSub {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void onMessage(String channel, String message) {
        logger.info("onMessage: channel:" + channel + "-------msg:" + message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        logger.info("onPMessage: channel:" + channel + "---------pattern:" + pattern + "-------msg:" + message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        logger.info("onSubscribe: onMessage:channel:" + channel + "-------subscribedChannels:" + subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        logger.info("onUnsubscribe: onMessage:channel:" + channel + "-------subscribedChannels:" + subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        logger.info("onPUnsubscribe: onMessage:channel:" + pattern + "-------subscribedChannels:" + subscribedChannels);
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        logger.info("onPSubscribe: onMessage:channel:" + pattern + "-------subscribedChannels:" + subscribedChannels);
    }


}
