package mq;

/**
 * Created by llc on 16/7/14.
 */
public class Main {
    public static void main(String[] args){
        MQServer server = new MQServer();
        try{
            server.start();
        }catch (InterruptedException e){
            MQServer.logger.error(e.getMessage());
            MQServer.logger.info("Delete que :"+MQServer.queueName);
            server.stop();
        }
    }
}
