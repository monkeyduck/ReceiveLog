package mq;


import net.sf.json.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBHelper;

/**
 * 接受log
 * Created by hxx on 5/3/16.
 */
class Receiver {
    static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    public static DBHelper db;
    private int cnt = 0;
    static {
        db = new DBHelper();
    }
    public void handleMessage(String slog) {
        cnt++;
        logger.info("receive "+cnt+":{}", slog);
        System.out.println(cnt+" receive: "+slog);
        NormalLog log;
        try {
            log = new NormalLog(slog);
            String level = log.getLevel().toString();
            String memberId = log.getMemberId();
            String deviceId = log.getDeviceId();
            String logTime = log.getTimeStamp();
            DateTime dateTime = new DateTime(Long.parseLong(logTime));
            String sDate = dateTime.toString("yyyy-MM-dd HH:mm:ss");
            String ip = log.getIp();
            String module = log.getModule();
            String content = log.getContent();
            String insert = String.format("insert into `cachetoday` (level, device_id, member_id, log_time, time, ip, " +
                    "modtrans, content) values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", level, deviceId,
                    memberId, logTime, sDate, ip, module, content);
            logger.info("Executing sql: "+insert);
            db.executeSQL(insert);
            insert = String.format("insert into `stat_module` (level, device_id, member_id, time, module) " +
                    "values ('%s', '%s', '%s', '%s', '%s')", level, deviceId, memberId, sDate, module);
            logger.info("Executing sql: "+insert);
            db.executeSQL(insert);
            if (module.equals("FrontEnd")){
                JSONObject json = JSONObject.fromObject(content);
                String version = json.getString("version");
                insert = String.format("insert into `stat_version` (level, device_id, member_id, time, version) " +
                        "values ('%s', '%s', '%s', '%s', '%s')", level, deviceId, memberId, sDate, version);
                db.executeSQL(insert);
            }

        } catch (Exception ex) {
            logger.error("log:" + slog + "throws:", ex);
        }

    }
}
