package mq;

import llc.service.ServiceHelper;

/**
 * Created by llc on 16/8/18.
 */
public class runReceiver {
    public static void main(String args[]){
        MQServer server = new MQServer();
        try{
            /**
             * init the userService
             */
            ServiceHelper.getUserService();
            server.start();
        }catch (InterruptedException e){
            MQServer.logger.error(e.getMessage());
            MQServer.logger.info("Delete que :"+MQServer.queueName);
            server.stop();
        }
    }
}
