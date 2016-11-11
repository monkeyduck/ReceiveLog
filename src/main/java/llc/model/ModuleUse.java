package llc.model;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;


/**
 * Created by llc on 16/8/18.
 */
public class ModuleUse {
    private static Logger logger = Logger.getLogger(ModuleUse.class);
    private String member_id;
    private String time;
    private String module;
    private String usedTime;
    private String content;
    private String date;
    private String methodName;

    public ModuleUse(String member_id, String time, String module, String usedTime, String content) {
        this.member_id = member_id;
        this.time = time;
        this.module = module;
        this.usedTime = usedTime;
        this.content = content;
        this.date = time.split(" ")[0];
        this.methodName = "";
        if (content.contains("methodName")){
            try{
                JSONObject json = JSONObject.fromObject(content);
                this.methodName = json.getString("methodName");
            }catch (Exception e){
                logger.error("Error occurs when parsing "+content+": "+e.getMessage());
            }

        }
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
