package mq;

import mq.Enums.ELevelType;
import mq.Enums.EnvironmentType;
import net.sf.json.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;


/**
 * 日志服务
 * Created by hxx on 3/15/16.
 */
public class NormalLog {

    private static final Logger logger = LoggerFactory.getLogger(NormalLog.class);
    private String content;
    private String memberId;
    private String deviceId;
    private String module;
    private String ip;
    private EnvironmentType envType;
    private String timeStamp;
    private ELevelType level;



    public NormalLog(String log) throws Exception {
        logger.info("Parsing log :"+log);
        JSONObject json = JSONObject.fromObject(log);
        level = ELevelType.fromString(json.getString("level"));
        content = processContent(replaceNull(json.getString("content")));//处理下有fromTime和toTime的状态
        memberId = replaceNull(json.getString("memberId"));
        module = replaceNull(json.getString("module"));
        ip = replaceNull(json.getString("ip"));
        deviceId = replaceNull(json.getString("deviceId"));
        String environment = replaceNull(json.getString("environment"));
        timeStamp = replaceNull(json.getString("timeStamp"));
        envType = EnvironmentType.fromString(environment);
        if (module.equals("") || memberId.equals("") || timeStamp.equals("") || deviceId.equals("")) {
            String message = "module, memberId, deviceId and timeStamp should have value";
            throw new Exception(message);
        }
    }

    public String getContent() {
        return content;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getModule() {
        return module;
    }

    public String getIp() {
        return ip;
    }

    public EnvironmentType getEnvType() {
        return envType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public ELevelType getLevel() {
        return level;
    }

    private static String replaceNull(String st) {
        return st == null ? "" : st;
    }

    static private String processContent(String content) {
        JSONObject jsonObject = JSONObject.fromObject(content);
        HashMap<String, String> dic = new HashMap<>();
        Iterator it = jsonObject.keys();
        long fromTime = -1;
        long toTime = -1;
        while (it.hasNext()) {
            String key = (String) it.next();
            if (jsonObject.getString(key).equals("")) {
                continue;
            }
            dic.put(key, jsonObject.getString(key));
            if (key.equals("fromTime")) {
                try {
                    fromTime = jsonObject.getLong(key);
                    DateTime dt = new DateTime(fromTime);
                    dic.put("readableFromTime", dt.toString("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    fromTime = -1;
                }
            }
            if (key.equals("toTime")) {
                try {
                    toTime = jsonObject.getLong(key);
                    DateTime dt = new DateTime(toTime);
                    dic.put("readableToTime", dt.toString("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    toTime = -1;
                }
            }
        }
        if ((fromTime > 0) && (toTime > 0)) {
            dic.put("usedTime", (toTime - fromTime) + "");
        }
        return JSONObject.fromObject(dic).toString();
    }
}
