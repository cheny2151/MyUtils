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
