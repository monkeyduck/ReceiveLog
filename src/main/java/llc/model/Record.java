package llc.model;

/**
 * Created by llc on 16/8/18.
 */
public class Record {
    private String member_id;
    private String device_id;
    private String modtrans;
    private String time;
    private String content;

    public String getMember_id() {
        return member_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getModtrans() {
        return modtrans;
    }

    public void setModtrans(String modtrans) {
        this.modtrans = modtrans;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
