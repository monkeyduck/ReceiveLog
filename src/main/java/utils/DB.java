package utils;

/**
 * Created by llc on 16/7/15.
 */
public class DB {
    private static DBHelper db = new DBHelper();

    public static DBHelper getDB(){
        return db;
    }
}
