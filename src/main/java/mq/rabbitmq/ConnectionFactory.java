package mq.rabbitmq;

import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

/**
 * mq.rabbitmq 工厂
 */
public class ConnectionFactory {

    private static String host = "localhost";
    private static String username = "guest";
    private static String password = "guest";

    private static LinkedList<Connection> pool = new LinkedList<>();

    public static Connection createConnection() throws IOException, TimeoutException {
        Connection connection = pool.poll();
        if (connection == null) {
            connection = createConnectionFactory().newConnection();
        }
        return connection;
    }

    private static com.rabbitmq.client.ConnectionFactory createConnectionFactory() {
        com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

}
