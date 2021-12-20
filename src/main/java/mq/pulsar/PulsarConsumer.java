package mq.pulsar;

import org.apache.pulsar.client.api.*;

import java.util.Map;

import static mq.pulsar.PulsarProducer.*;

/**
 * @author by chenyi
 * @date 2021/12/17
 */
public class PulsarConsumer {

    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .build();

        Consumer<String> consumer = client.newConsumer(Schema.STRING)
                .topic(TOPIC)
                .subscriptionName(subscription)
                .subscriptionType(SubscriptionType.Shared)
                // 如果要限制接收器队列大小
                .receiverQueueSize(10)
                .subscribe();

        while (true) {
            Message<String> message = consumer.receive();
            System.out.printf("id:%s; value:%s%n; topicName:%s; key:%s; properties: %s",
                    message.getMessageId(), message.getValue(), message.getTopicName(), message.getKey(), message.getProperties());
            consumer.acknowledge(message.getMessageId());
        }
    }

}
