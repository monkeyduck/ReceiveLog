package llc.service.impl;

import llc.dao.UserDao;
import llc.model.*;
import llc.service.UserService;
import mq.NormalLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by llc on 16/8/12.
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public void insertUsedTime(ModuleUse moduleUse) {
        userDao.insertUsedTime(moduleUse);
    }

    @Override
    public void insertHourCache(NormalLog log) {
        userDao.insertHourCache(log);
    }

    @Override
    public void insertUsedTime_alpha(ModuleUse moduleUse) {
        userDao.insertUsedTime_alpha(moduleUse);
    }

    @Override
    public void insertUsedTime_beta(ModuleUse moduleUse) {
        userDao.insertUsedTime_beta(moduleUse);
    }

}
