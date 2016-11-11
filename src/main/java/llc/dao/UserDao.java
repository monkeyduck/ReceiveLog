package llc.dao;

import llc.model.*;
import mq.NormalLog;


/**
 * Created by llc on 16/8/12.
 */
public interface UserDao {
    void insertUsedTime(ModuleUse moduleUse);

    void insertHourCache(NormalLog log);

    void insertUsedTime_alpha(ModuleUse moduleUse);

    void insertUsedTime_beta(ModuleUse moduleUse);
}