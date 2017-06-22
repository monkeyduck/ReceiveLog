package mq;

/**
 * Created by llc on 17/1/22.
 */
public class runNewReceiver {
    public static void main(String[] args) {
        // init the userService
        String env = "alpha";
        if (args.length > 0) {
            env = args[0];
        }
        KafkaProcessor kafka = new KafkaProcessor(env);
        kafka.process(env);
    }

}
