package mq;

/**
 * Created by llc on 17/1/22.
 */

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import llc.model.ModuleUse;
import llc.service.ServiceHelper;
import mq.Enums.EnvironmentType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Cons;

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
    private final static String groupId = "firstStatConsumer";
    // logger
    static final Logger logger = LoggerFactory.getLogger(KafkaProcessor.class);

    public KafkaProcessor() {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
    }
    public void process() {
        Map<String, Integer> topicCount = new HashMap<>();
        topicCount.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()) {
                String message = new String(it.next().message());
                logger.info("Message from Single Topic: " + message);
                handleMessage(message);
            }
        }
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    public void handleMessage(String slog) {
        NormalLog log;
        try {
            log = new NormalLog(slog);
        }catch (Exception e){
            logger.error("Parse log error: " + e.getMessage());
            return;
        }
        EnvironmentType envType = log.getEnvType();
        String modTrans = log.getModtrans();
        if (envType.name().toLowerCase().equals("alpha")){
//            if (modTrans.contains("->")){   //给宇骁上下文服务器
//                try{
//                    IInteractContextSender icsender = getIcFetcher();
//                    boolean res = icsender.recordToContext(slog);
//                    logger.info("上下文服务器发送结果 "+res);
//                }catch (Exception e){
//                    logger.error("上下文发送失败, 原因:" + e.getMessage());
//                }
//
//            }
        }
        else if (envType.equals(EnvironmentType.Online) || envType.equals(EnvironmentType.Release)) {
            if (!modTrans.equals("FrontEnd")) {
                try {
                    ServiceHelper.getUserService().insertHourCache(log);
                } catch (Exception e) {
                    logger.error("Error occurs! throws: " + e.getMessage());
                }
            }
        }
        if (log.containMethodName() && !modTrans.equals("FrontEnd")) {
            String methodName = log.getMethodName();
            if (!methodName.equals("") &&
                    !Cons.uselessMethod.contains(methodName)){
                String usedTime = log.getUsedTime();
                String env = envType.name().toLowerCase();
                String tableName;
                if (env.equals("online") || env.equals("release")){
                    tableName = "stat_module";
                }else{
                    tableName = env + "_stat_module";
                }
                String memberId = log.getMember_id();
                String logTime = log.getLog_time();
                DateTime dateTime = new DateTime(Long.parseLong(logTime));
                String sDate = dateTime.toString("yyyy-MM-dd HH:mm:ss");
                String module = log.getModtrans();
                String content = log.getContent();
                ModuleUse moduleUse = new ModuleUse(memberId, sDate, module, usedTime, content);
                try{
                    if (env.equals("alpha")) {
                        ServiceHelper.getUserService().insertUsedTime_alpha(moduleUse);
                    } else if(env.equals("beta")){
                        ServiceHelper.getUserService().insertUsedTime_beta(moduleUse);
                    }else{
                        ServiceHelper.getUserService().insertUsedTime(moduleUse);
                    }
                    logger.info("Insert into " + tableName + " successfully");
                }catch (Exception e){
                    logger.error("Insert error: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        KafkaProcessor simpleHLConsumer = new KafkaProcessor();
        simpleHLConsumer.process();
    }

}
