package mq.pulsar;

import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

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
                .negativeAckRedeliveryDelay(5,TimeUnit.SECONDS)
//                .enableRetry(true)
                .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(3).build())
                // 如果要限制接收器队列大小
                .receiverQueueSize(2)
                .subscribe();

        while (true) {
            Message<String> message = consumer.receive();
            System.out.printf("id:%s; value:%s%n; topicName:%s; key:%s; properties: %s",
                    message.getMessageId(), message.getValue(), message.getTopicName(), message.getKey(), message.getProperties());
//            consumer.reconsumeLater(message, 10, TimeUnit.SECONDS);
//            consumer.reconsumeLaterCumulative(message, 5, TimeUnit.SECONDS);
//            consumer.negativeAcknowledge(message);
            consumer.acknowledge(message);
        }
    }

}
