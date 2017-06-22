package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by llc on 16/9/17.
 */
public class Cons {
    public static final String UNKNOWN = "unknown";
    public static final String REAL = "real";
    public static final String INDOOR = "indoor";
    public static final String MARKET = "market";
    public static final String INNERTEST = "innerTest";
    public static final String GRAY = "gray";
    public static final String VIP = "vip";
    public static List<String> memberTypeList = new ArrayList<>();
    public static Set<String> uselessMethod = new HashSet<>();
    static{
        memberTypeList.add(REAL);
        memberTypeList.add(INDOOR);
        memberTypeList.add(MARKET);
        memberTypeList.add(INNERTEST);
        memberTypeList.add(GRAY);
        memberTypeList.add(VIP);
        uselessMethod.add("enterIn");
        uselessMethod.add("exit");
        uselessMethod.add("dialogTopicCall");
        uselessMethod.add("pictureCall");
        uselessMethod.add("pushGrowUpNotification");
        uselessMethod.add("sendGrowUpLog");
        uselessMethod.add("nodeEnterIn");
        uselessMethod.add("synthetic");
        uselessMethod.add("talk");
    }
}
