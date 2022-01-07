package mq.pulsar;

import org.apache.pulsar.client.api.*;

/**
 * @author by chenyi
 * @date 2021/12/17
 */
public class PulsarProducer {

    public static final String SERVICE_URL = "pulsar://localhost:6650";
    public static final String TOPIC = "persistent://test/test-namespace/test-topic";
    public static final String subscription = "sub-1";

    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .build();


        Producer<String> producer = client.newProducer(Schema.STRING)
                .accessMode(ProducerAccessMode.Shared)
                .topic(TOPIC).create();
        for (int i = 0; i < 1000; i++) {
            producer.newMessage().property("test", "111").value("{\n" +
                    "  \"requestId\": \"" + i + "\"\n" +
                    "}").send();
        }

        producer.close();
        client.close();
    }

}
