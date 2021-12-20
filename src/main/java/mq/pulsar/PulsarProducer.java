package mq.pulsar;

import com.alibaba.fastjson.JSON;
import org.apache.pulsar.client.api.*;

import java.util.HashMap;
import java.util.Map;

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
        HashMap<String, Object> val = new HashMap<>();
        val.put("hello", "word");
        producer.newMessage().property("test", "111").value(JSON.toJSONString(val)).send();
        producer.close();
        client.close();
    }

}
