package mq.kafka.consumer;

/**
 * kafka消费者
 * 新建立消费组consumer_group会把offset设置到订阅的topic的最头部（最新），消费后续消息
 * 旧的consumer_group会记录最后一次消费时的offset，其他的consumer_group消费消息不会影响此consumer_group的offset，
 * 当有该分组的consumer线程启动时从offset开始消费（1.所以不会丢失消息，2.消息是否已经被消费要看具体的consumer_group）
 *
 * @author cheney
 * @date 2019/5/29
 */
public interface Consumer<T> {

    void consume(java.util.function.Consumer<T> consumer);

}
