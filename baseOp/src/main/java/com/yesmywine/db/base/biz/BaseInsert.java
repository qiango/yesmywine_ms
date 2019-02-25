package com.yesmywine.db.base.biz;

import com.yesmywine.db.base.bean.DataSource;
import com.yesmywine.db.base.bean.DefaultData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class BaseInsert {


    public static Integer run(String tableName, Map<String, Object> data) {
        return run(DefaultData.getDataSource(), tableName, data);
    }

    private static Integer run(DataSource dataSource, String tableName, Map<String, Object> data) {
        Connection conn = null;
        PreparedStatement stmt = null;
        Integer autoId = null;
        try {
            Class.forName(dataSource.getDriver());
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUserName(), dataSource.getPassword());
            conn.setAutoCommit(true);

            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("insert into  ");
            sqlBuffer.append(tableName);
            sqlBuffer.append(" ( ");
            List<String> keys = new ArrayList<>();
            boolean flag = true;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (flag) {
                    sqlBuffer.append(entry.getKey());
                    flag = false;
                } else {
                    sqlBuffer.append("," + entry.getKey());
                }
                keys.add(entry.getKey());
            }
            sqlBuffer.append(" ) values ( ");
            for (int i = 0; i < keys.size(); i++) {
                Object obj = data.get(keys.get(i));
                if (i == 0) {
                    sqlBuffer.append(DBMethod.getDataSql(obj));
                } else {
                    sqlBuffer.append(" , " + DBMethod.getDataSql(obj));
                }
            }


            sqlBuffer.append(" ) ");

            System.out.println(sqlBuffer.toString());
            stmt = conn.prepareStatement(sqlBuffer.toString(), Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet result = stmt.getGeneratedKeys();
            result.next();
            autoId = result.getInt(1);

            result.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autoId;

    }


    public static void run(String tableName, List<Map<String, Object>> data) {
        run(DefaultData.getDataSource(), tableName, data);
    }

    public static void run(DataSource dataSource, String tableName, List<Map<String, Object>> data) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(dataSource.getDriver());
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUserName(), dataSource.getPassword());
            conn.setAutoCommit(true);


            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("insert into  ");
            sqlBuffer.append(tableName);
            sqlBuffer.append(" ( ");
            List<String> keys = new ArrayList<>();
            boolean flag = true;
            for (Map.Entry<String, Object> entry : data.get(0).entrySet()) {
                if (flag) {
                    sqlBuffer.append(entry.getKey());
                    flag = false;
                } else {
                    sqlBuffer.append("," + entry.getKey());
                }
                keys.add(entry.getKey());
            }
            sqlBuffer.append(" ) values  ");
            for (int i = 0; i < data.size(); i++) {
                if (i == 0) {
                    sqlBuffer.append(" ( ");
                } else {
                    sqlBuffer.append(" ,( ");
                }


                for (int j = 0; j < keys.size(); j++) {
                    Object obj = data.get(i).get(keys.get(j));
                    if (j == 0) {
                        sqlBuffer.append(DBMethod.getDataSql(obj));
                    } else {
                        sqlBuffer.append("," + DBMethod.getDataSql(obj));
                    }
                }
                sqlBuffer.append(" ) ");
            }


            System.out.println(sqlBuffer.toString());
            stmt = conn.prepareStatement(sqlBuffer.toString(), Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
