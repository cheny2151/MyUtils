package rabbitmq.demo;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

/**
 * 路由模式
 * 交换器direct：如果消息的routing key与banding的routing key直接匹配的话，消息将会路由到该队列上
 */
public class Demo3 {

    private String queueName;

    private String exChangeName = "ex_direct";

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
            channel.exchangeDeclare(exChangeName, BuiltinExchangeType.DIRECT, false);
            //通过交换机发送消息，指定不同的routingKey
            channel.basicPublish(exChangeName, "error", MessageProperties.PERSISTENT_TEXT_PLAIN, "error".getBytes("utf-8"));
            channel.basicPublish(exChangeName, "warn", MessageProperties.PERSISTENT_TEXT_PLAIN, "warn".getBytes("utf-8"));
            channel.basicPublish(exChangeName, "info", MessageProperties.PERSISTENT_TEXT_PLAIN, "info".getBytes("utf-8"));
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

    @Test
    public void test2() {
        System.out.println("---warn----");
        consume("warn");
    }

    @Test
    public void test3() {
        System.out.println("---info----");
        consume("info");
    }

    @Test
    public void test4() {
        System.out.println("---error----");
        consume("error");
    }

    private void consume(String routingKey) {
        Connection connection = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //定义一个交换机（防止服务器还未创建此交换机）
            channel.exchangeDeclare(exChangeName, BuiltinExchangeType.DIRECT, false);
            channel.basicQos(1);
            //产生一个随机的队列名称 该队列用于从交换器获取消息
            queueName = channel.queueDeclare().getQueue();
            //将队列和交换机绑定 设置routingKey获取对应的消息(可多次绑定，设置多个routingKey)
            channel.queueBind(queueName, exChangeName, routingKey);
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("routingKey:" + envelope.getRoutingKey());
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
