package mq.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import mq.kafka.utils.KafkaUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * partition：分区（队列）
 * Consumer group：各个consumer（consumer 线程）可以组成一个组，partition中的每个message只能被组（Consumer group ）中的一个consumer（consumer 线程）消费
 * 如果想同时对一个topic做消费的话，启动多个consumer group去消费该topic即可。
 *
 * kafka为了保证吞吐量，只允许同一个consumer group下的一个consumer线程去访问一个partition。如果觉得效率不高的时候，可以加partition的数量来横向扩展，那么再加新的consumer thread去消费。
 * 如果想多个不同的业务都需要这个topic的数据，起多个consumer group就好了，大家都是顺序的读取message，offsite的值互不影响。这样没有锁竞争，充分发挥了横向的扩展性，吞吐量极高。这也就形成了分布式消费的概念。
 * @author cheney
 * @date 2019/5/29
 */
@Slf4j
public class Producer<T> implements Closeable {

    private KafkaProducer<String, T> producer;

    private Properties properties;

    public Producer() {
        this.properties = KafkaUtils.getDefaultProperties();
        producer = new KafkaProducer<>(properties);
    }

    public Producer(Properties properties) {
        this.properties = properties;
        producer = new KafkaProducer<>(properties);
    }

    public void send(T msg, String topic) {
        if (msg == null) {
            throw new IllegalArgumentException("msg can not be null");
        }
        producer.send(new ProducerRecord<>(topic, msg));
        producer.flush();
    }

    public void sendAll(List<T> msgs, String topic) {
        if (CollectionUtils.isEmpty(msgs)) {
            throw new IllegalArgumentException("msg can not be null");
        }
        for (T msg : msgs) {
            producer.send(new ProducerRecord<>(topic, msg));
        }
        producer.flush();
    }

    @Override
    public void close() throws IOException {
        this.producer.close();
    }
}
