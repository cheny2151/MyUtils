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

    @Test
    public void produce() {
        Producer<String> stringProducer = new Producer<>();
        stringProducer.send("hello,java api", "test");
    }

    @Test
    public void consume() {
        AutoCommitConsumer<String> consumer = new AutoCommitConsumer<>("group_0", Collections.singletonList("test"));
        for (;;) {
            consumer.consume(System.out::println);
        }
    }

    @Test
    public void consume2() {
        AutoCommitConsumer<String> consumer = new AutoCommitConsumer<>("group_1", Collections.singletonList("test"));
        for (;;) {
            consumer.consume(System.out::println);
        }
    }

}
