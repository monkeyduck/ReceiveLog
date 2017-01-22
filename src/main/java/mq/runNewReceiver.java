package mq;

import llc.service.ServiceHelper;

/**
 * Created by llc on 17/1/22.
 */
public class runNewReceiver {
    public static void main(String[] args) {
        // init the userService
        ServiceHelper.getUserService();
        KafkaProcessor kafka = new KafkaProcessor();
        kafka.process();
    }

}
