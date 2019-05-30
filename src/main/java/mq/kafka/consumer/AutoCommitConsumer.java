package mq.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import mq.kafka.utils.KafkaUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * @author cheney
 * @date 2019/5/29
 */
@Slf4j
public class AutoCommitConsumer<T> implements Consumer<T> {

    private KafkaConsumer<String, T> kafkaConsumer;

    private Properties properties;

    private String groupId;

    private Collection<String> subscribeTopics;

    public AutoCommitConsumer(String groupId, List<String> topics) {
        this.subscribeTopics = topics;
        this.groupId = groupId;
        Properties properties = KafkaUtils.cloneDefaultProperties();
        properties.put("enable.auto.commit", true);
        properties.put("auto.commit.interval.ms", 1000);
        properties.put("group.id", groupId);
        System.out.println(properties.get("key.deserializer"));
        this.properties = properties;
        this.kafkaConsumer = new KafkaConsumer<>(properties);
        this.kafkaConsumer.subscribe(topics);
    }

    @Override
    public void consume(java.util.function.Consumer<T> consumer) {
        ConsumerRecords<String, T> records = kafkaConsumer.poll(Duration.ofSeconds(30));
        for (ConsumerRecord<String, T> record : records) {
            consumer.accept(record.value());
        }
    }

    public KafkaConsumer<String, T> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public String getGroupId() {
        return groupId;
    }


    public Collection<String> getSubscribeTopics() {
        return subscribeTopics;
    }

}
