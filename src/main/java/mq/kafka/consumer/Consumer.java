package mq.kafka.consumer;

/**
 * kafka消费者
 *
 * @author cheney
 * @date 2019/5/29
 */
public interface Consumer<T> {

    void consume(java.util.function.Consumer<T> consumer);

}
