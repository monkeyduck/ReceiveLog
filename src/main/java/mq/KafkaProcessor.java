package mq;

/**
 * Created by llc on 17/1/22.
 */

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KafkaProcessor {
    private final ConsumerConnector consumer;
    // 连接的Zookeeper接口, 不同业务模块使用的数据是相同的
    private final static String zookeeper = "10.172.217.86:2181,10.44.143.42:2181,10.252.0.171:2181";
    // Kafka消费者订阅的主题,不同模块使用是相同的
    private final static String topic = "aibasisStats";
    // Kafka
    private final static String groupId = "ReceiveConsumer";
    // logger
    static final Logger logger = LoggerFactory.getLogger(KafkaProcessor.class);

    public KafkaProcessor(String env) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", env + groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
    }
    public void process(String env) {
        Map<String, Integer> topicCount = new HashMap<>();
        topicCount.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()) {
                String message = new String(it.next().message());
                logger.info("Message from Kafka: " + message);
                Receiver.handleMessage(message, env);
            }
        }
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    public static void main(String[] args) {
        KafkaProcessor simpleHLConsumer = new KafkaProcessor("alpha");
        simpleHLConsumer.process("alpha");
    }

}
