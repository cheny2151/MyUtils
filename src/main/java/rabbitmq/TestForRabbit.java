package rabbitmq;

import DesignPattern.observer.single.Student;
import com.rabbitmq.client.*;
import jsonUtils.JsonUtils;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.TimeoutException;

public class TestForRabbit {

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
            channel.queueDeclare("test", false, false, false, null);
            Student student = new Student("1", "1", "1");
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//            objectOutputStream.writeObject(student);
            System.out.println(SerializationUtils.serialize(student).length);
            String s = JsonUtils.toJson(student);
            System.out.println(s);
            channel.basicPublish("", "test", null, s.getBytes("utf-8"));
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
    public void test2() throws IOException, TimeoutException {
        Connection connection = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("test", false, false, false, null);
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
//                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    String s = new String(body, "utf-8");
                    System.out.println(s);
                    Student student = JsonUtils.toJavaBean(s, Student.class);
                    System.out.println(student);
//                    System.out.println(SerializationUtils.deserialize(body));
                }
            };
            channel.basicConsume("test", true, consumer);
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
    public void test3() throws IOException, ClassNotFoundException {
        Student student = JsonUtils.toJavaBean("{\"id\":\"1\",\"name\":\"1\",\"sex\":\"1\",\"homework\":[]}", Student.class);
        System.out.println(student);
    }

}
