package mq;


import mq.Enums.EnvironmentType;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DB;
import utils.DBHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 接受log
 * Created by hxx on 5/3/16.
 */
class Receiver {
    static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    private static List<String> cacheLogList = new ArrayList<String>();

    public void handleMessage(String slog) {
        logger.info("receive "+":{}", slog);
        int maxSize = 99;
        NormalLog log;
        try {
            log = new NormalLog(slog);
            EnvironmentType envType = log.getEnvType();
            if (!log.getModule().equals("FrontEnd")){
                if (envType.equals(EnvironmentType.Online) || envType.equals(EnvironmentType.Release)){
                    String level = log.getLevel().toString();
                    String memberId = log.getMemberId();
                    String deviceId = log.getDeviceId();
                    String logTime = log.getTimeStamp();
                    DateTime dateTime = new DateTime(Long.parseLong(logTime));
                    String sDate = dateTime.toString("yyyy-MM-dd HH:mm:ss");
                    String ip = log.getIp();
                    String module = log.getModule();
                    String content = log.getContent();
                    // 单引号特殊处理
                    content = content.replaceAll("'","''");
                    cacheLogList.add(String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'),", level, deviceId,
                            memberId, logTime, sDate, ip, module, content));
                    if (cacheLogList.size()>maxSize){
                        String insert = "insert into `cachehour` (level, device_id, member_id, log_time, time, ip, " +
                                "modtrans, content) values ";
                        for (String cachelog: cacheLogList){
                            insert += cachelog;
                        }
                        cacheLogList.clear();
                        insert = insert.substring(0,insert.length()-1);
                        logger.info("Start to execute sql: "+insert);
                        DB.getDB().executeSQL(insert);
                        logger.info("Insert into cachehour successfully");
                    }
                }
                else if (log.containUsedTime()){
                    String usedTime = log.getUsedTime();
                    if (!usedTime.equals("0")){
                        String env = envType.name().toLowerCase();
                        String tableName = env+"_stat_module";
                        String memberId = log.getMemberId();
                        String logTime = log.getTimeStamp();
                        DateTime dateTime = new DateTime(Long.parseLong(logTime));
                        String sDate = dateTime.toString("yyyy-MM-dd HH:mm:ss");
                        String module = log.getModule();
                        String content = log.getContent();
                        String insert = String.format("insert into %s (member_id, time, module, usedTime, content) values " +
                                "('%s', '%s', '%s', '%s', '%s')", tableName, memberId, sDate, module, usedTime, content);
                        DB.getDB().executeSQL(insert);
                        logger.info("Insert into "+tableName+" successfully");
                    }
                }
            }

        } catch (Exception ex) {
            logger.error("Insert error!! Throws:" + ex.getMessage());
        }

    }

    public static void clearCacheList(){
        cacheLogList.clear();
    }

    public static void main(String args[]){
        String cont = "'wozuohao'";
        System.out.println(cont);
        cont = cont.replaceAll("'","'''");
        System.out.println(cont);
    }
}
