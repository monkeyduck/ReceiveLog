package llc.dao;

import llc.model.*;
import mq.NormalLog;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by llc on 16/8/12.
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SqlSessionTemplate sqlSession;


    public void insertUsedTime(ModuleUse moduleUse) {
        sqlSession.insert("insertUsedTime");
    }

    @Override
    public void insertHourCache(NormalLog log) {
        sqlSession.insert("insertHourCache");
    }

    @Override
    public void insertUsedTime_alpha(ModuleUse moduleUse) {
        sqlSession.insert("insertUsedTime_alpha");
    }

    @Override
    public void insertUsedTime_beta(ModuleUse moduleUse) {
        sqlSession.insert("insertUsedTime_beta");
    }

}
