package mq.pulsar;

import org.apache.pulsar.client.api.*;

/**
 * @author by chenyi
 * @date 2021/12/17
 */
public class PulsarProducer {

    public static final String SERVICE_URL = "pulsar://localhost:6650";
    public static final String TOPIC = "persistent://test/test-namespace/req";
    public static final String TOPIC_RESP = "persistent://test/test-namespace/resp";
    public static final String subscription = "sub-1";

    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .build();


        Producer<String> producer = client.newProducer(Schema.STRING)
                .accessMode(ProducerAccessMode.Shared)
                .topic(TOPIC).create();
        for (int i = 0; i < 1; i++) {
            producer.newMessage().value("{\n" +
                    "        \"appId\": \"11111\",\n" +
                    "        \"requestId\": \"00001111\",\n" +
                    "        \"content\": \"Return  goods\",\n" +
                    "        \"merchantId\": \"algorithm_test\",\n" +
                    "        \"customerId\": \"123\",\n" +
                    "        \"psid\": \"test\",\n" +
                    "        \"type\": [\n" +
                    "            \"label\"\n" +
                    "        ],\n" +
                    "        \"payload\": {\n" +
                    "            \"test\": \"123\"\n" +
                    "        },\n" +
                    "        \"region\": \"VN\"\n" +
                    "    }").send();
        }
        producer.close();
        client.close();
    }

}
