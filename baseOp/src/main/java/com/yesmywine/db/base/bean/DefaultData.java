package com.yesmywine.db.base.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class DefaultData {

    public static DataSource dataSource = null;
    public static Integer defaultPageSize = 10;


    public static Map<DatabaseType, String> dbDriverMap = new HashMap<>();
    public static Map<DatabaseType, String> dbUrlHead = new HashMap<>();


    static {
        dbDriverMap.put(DatabaseType.mysql, "com.mysql.jdbc.Driver");
        dbUrlHead.put(DatabaseType.mysql, "jdbc:mysql://");
    }

    public static String getDriver(DatabaseType dbType) {
        return dbDriverMap.get(dbType);
    }

    public static String getUrlHead(DatabaseType dbType) {
        return dbUrlHead.get(dbType);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void setDataSource(DataSource dataSource) {
        DefaultData.dataSource = dataSource;
    }
}
