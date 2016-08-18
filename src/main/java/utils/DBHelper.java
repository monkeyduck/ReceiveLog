package utils;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;


/**
 * Created by llc on 16/6/20.
 */
public class DBHelper {
    private static final String url;
    private static final String user;
    private static final String password;
    private static final Logger logger;
    private static final String name;

    static {
        url = "jdbc:mysql://rdsu1u8uau8193m7j1b7.mysql.rds.aliyuncs.com:3306/aibasis_stat?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
        user = "aibasis";
        password = "Wenjin1411";
        name = "com.mysql.jdbc.Driver";
        logger = LoggerFactory.getLogger(DBHelper.class);
    }

    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;

    public ResultSet getRs() {
        return rs;
    }

    public DBHelper() {
        try {
            Class.forName(name);
            // 获得 Transaction 管理对象
//            InitialContext ctx = new InitialContext();
//            userTx = (UserTransaction)ctx.lookup("java:comp/UserTransaction");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Connection getConnection(){
        if (conn == null){
            try{
                conn = DriverManager.getConnection(url, user, password);
            }catch (Exception e){
                logger.error(e.getMessage());
                System.out.println(e.getMessage());
            }
        }
        return conn;
    }


    public void executeSQL(String sql) throws SQLException{
        conn = this.getConnection();
        stmt = conn.createStatement();
        String no_splash = "SET @old_sql_mode=@@sql_mode";
        stmt.execute(no_splash);
        no_splash="SET @@sql_mode=CONCAT_WS(',', @@sql_mode, 'NO_BACKSLASH_ESCAPES')";
        stmt.execute(no_splash);
        stmt.execute(sql);
        no_splash = "SET @@sql_mode=@old_sql_mode";
        stmt.execute(no_splash);

    }

    public ResultSet executeQuery(String sql){
        try{
            conn = this.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        }catch (SQLException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    public void closeAll() {
        try{
            conn.close();
            stmt.close();
            preparedStatement.close();
            rs.close();
        }catch(SQLException e){
            logger.error(e.getMessage());
        }
    }

    private static String replaceNull(String str) {
        if (str != null) {
            String ret = str.replaceAll("'", "''");
            return ret == null ? "" : ret;
        } else {
            return "";
        }
    }
}
