package rabbitmq.demo;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

/**
 * 发布订阅模式
 * 交换器fanout：不管消息的routing key和参数表的头信息/值是什么，消息都会路由到所有队列上
 */
public class Demo2 {

    private String queueName;

    private String exChangeName = "ex_fanout";

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
            //定义一个交换器 参数1 名称  参数2 交换器类型 参数3表示将交换器信息永久保存在服务器磁盘上
            channel.exchangeDeclare(exChangeName, BuiltinExchangeType.FANOUT, true);
            for (int i = 0; i < 5; i++) {
                //通过交换机发送消息，routingKey为空 默认会转发给所有的订阅者队列
                channel.basicPublish(exChangeName, "", MessageProperties.PERSISTENT_TEXT_PLAIN, "test".getBytes("utf-8"));
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

    /**
     * 消费者
     */
    @Test
    public void test2() {
        Connection connection = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //定义一个交换机（防止服务器还未创建此交换机）
            channel.exchangeDeclare(exChangeName, BuiltinExchangeType.FANOUT, false);
            channel.basicQos(1);
            //产生一个随机的队列名称 该队列用于从交换器获取消息
            queueName = channel.queueDeclare().getQueue();
            //将队列和交换机绑定 就可以正式获取消息了 routingKey和交换器的一样都设置成空（获取所有消息）
            channel.queueBind(queueName, exChangeName, "");
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    callBack(body);
                    //手动确认收到信息
                    channel.basicAck(envelope.getDeliveryTag(), false);
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
            Thread.sleep(1000);
            System.out.println("---" + new String(body, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void basicConsume(String name, Channel channel, Consumer consumer) throws IOException, InterruptedException {
        //autoAck:false 手动确认
        channel.basicConsume(name, false, consumer);
        Thread.sleep(100);
        basicConsume(name, channel, consumer);
    }

}
