package rabbitmq.demo;

import com.rabbitmq.client.*;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * 简单模式
 * 概念1 消息确认
 * 假设 发布者推送了一个消息  消息接受者 接受到了消息  处理过程中出现中断或者异常 就说明消息处理失败
 * channel.basicConsume(QUEUE_NAME, true, consumer);  
 * 第二个参数是true 表示自动确定  服务器推送消息给接受者 一旦推送成功就删除消息 不会管接受者的死活了
 * rabbitmq 提供了 手动确认的功能 等接受者 业务逻辑处理完成后 手动调用确认方法确认后 才会删除消息 否则消息就不会从服务器删除
 * 状态1:  新建消息 未被消费
 * 状态2:  已消费未确认 接受者设置为手动确定 但是最终未确认(未调用channel.basicAck(envelope.getDeliveryTag(),false))
 * 状态3:   已消费确认
 */
public class Demo1 {

    private Logger logger = Logger.getLogger(Demo1.class);

    private String queueName = "test";

    /**
     * 生产者
     */
    @Test
    public void test() {
        Connection connection = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //定义一个队列
            channel.queueDeclare(queueName, true, false, false, null);
            HashMap<String, String> map = new HashMap<>();
            map.put("test", "success");
            for (int i = 0; i < 5; i++) {
                //通过交换机发送消息，routingKey为空 默认会转发给所有的订阅者队列

                //发送json数据的配置
                AMQP.BasicProperties jsonProperties = new AMQP.BasicProperties("application/json",
                        null,
                        null,
                        1,
                        0, null, null, null,
                        null, null, null, null,
                        null, null);
                //发送java序列化数据配置
                AMQP.BasicProperties objectProperties = new AMQP.BasicProperties("application/x-java-serialized-object",
                        null,
                        null,
                        1,
                        0, null, null, null,
                        null, null, null, null,
                        null, null);

                channel.basicPublish("", queueName, objectProperties, SerializationUtils.serialize(map));
            }
            System.out.println("-------send--------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private final Object key = new Object();

    /**
     * 消费者
     */
    @Test
    public void test2() throws InterruptedException {

        Connection connection = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
//            channel.basicQos(1);
            //定义一个队列 (防止服务器不存在此队列)
            channel.queueDeclare(queueName, true, false, false, null);
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    callBack(body);
                    //手动确认收到信息
                    synchronized (key) {
                        channel.basicAck(envelope.getDeliveryTag(), false);
//                        key.notifyAll();
                    }
                }
            };
            //执行客户端回调
            basicConsume(queueName, channel, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void callBack(byte[] body) {
        try {
            logger.info(Thread.currentThread().getId() + ":" + SerializationUtils.deserialize(body));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void basicConsume(String name, Channel channel, Consumer consumer) throws IOException, InterruptedException {
        //autoAck:false 手动确认
        logger.info("start receive...");
        channel.basicConsume(name, false, consumer);
        while (true) {
            synchronized (key) {
                key.wait();
            }
        }
    }
}
