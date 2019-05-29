package mq.kafka;

import mq.kafka.consumer.AutoCommitConsumer;
import mq.kafka.producer.Producer;
import org.junit.Test;

import java.util.Collections;

/**
 * @author cheney
 * @date 2019/5/29
 */
public class Main {

    public static void main(String[] args) {
        Producer<String> stringProducer = new Producer<>();
        stringProducer.send("hello,java api", "test");
    }

    @Test
    public void consumer() {
        AutoCommitConsumer<String> consumer = new AutoCommitConsumer<>("group_0", Collections.singletonList("test"));
        for (;;) {
            consumer.consume(System.out::println);
        }
    }

}
