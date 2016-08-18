package mq;

import mq.Enums.ELevelType;
import mq.Enums.EnvironmentType;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
        JSONObject json = JSONObject.fromObject(log);
        level = ELevelType.fromString(json.getString("level"));
        memberId = replaceNull(json.getString("memberId"));
        module = replaceNull(json.getString("module"));
        ip = replaceNull(json.getString("ip"));
        deviceId = replaceNull(json.getString("deviceId"));
        String environment = replaceNull(json.getString("environment"));
        timeStamp = replaceNull(json.getString("timeStamp"));
        envType = EnvironmentType.fromString(environment);
        content = json.getString("content");

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

    static private String processContent(String memberId, String timeStamp, String module, String content)
            throws Exception{
        if (module.equals("") || memberId.equals("") || timeStamp.equals("")) {
            String message = "module, memberId and timeStamp should have value";
            throw new Exception(message);
        }
        return content;
    }

    public boolean containUsedTime(){
        return content.contains("fromTime") && content.contains("toTime") && !module.equals("FrontEnd");
    }

    public String getUsedTime(){
        JSONObject json = JSONObject.fromObject(content);
        long fromTime = -1;
        long toTime = -1;
        String sfromTime = json.getString("fromTime");
        String stoTime = json.getString("toTime");
        if (!sfromTime.equals("") && !stoTime.equals("")) {
            fromTime = Long.parseLong(sfromTime);
            toTime = Long.parseLong(stoTime);
            if (fromTime > 0 && toTime > 0 && toTime > fromTime) {
                long usePeriods = toTime - fromTime;
                return "" + usePeriods;
            }
        }
        return "0";
    }
}
