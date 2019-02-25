package com.yesmywine.db.base.biz;

import com.yesmywine.db.base.bean.DataSource;
import com.yesmywine.db.base.bean.DefaultData;

import java.sql.*;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class BaseUpdate {


    public static Integer run(String tableName, Map<String, Object> data, String condition) {
        return run(DefaultData.getDataSource(), tableName, data, condition);
    }

    private static Integer run(DataSource dataSource, String tableName, Map<String, Object> data, String condition) {
        Connection conn = null;
        PreparedStatement stmt = null;
        Integer cnt = null;
        try {
            Class.forName(dataSource.getDriver());
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUserName(), dataSource.getPassword());
            conn.setAutoCommit(true);

            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append(" update ");
            sqlBuffer.append(tableName);
            sqlBuffer.append(" set ");

            boolean flag = true;

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object obj = entry.getValue();
                if (flag) {
                    sqlBuffer.append(entry.getKey());
                    sqlBuffer.append("=");
                    sqlBuffer.append(DBMethod.getDataSql(obj));
                    flag = false;
                } else {
                    sqlBuffer.append(" , ");
                    sqlBuffer.append(entry.getKey());
                    sqlBuffer.append("=");
                    sqlBuffer.append(DBMethod.getDataSql(obj));
                }
            }

            if (null != condition) {
                String tempCondition = condition.toLowerCase().trim();
                if (tempCondition.startsWith("where")) {
                    sqlBuffer.append(" " + condition + " ");
                } else if (tempCondition.startsWith("and") || tempCondition.startsWith("or")) {
                    sqlBuffer.append(" where 1=1 ");
                    sqlBuffer.append(condition);
                } else {
                    sqlBuffer.append(" where 1=1 and ");
                    sqlBuffer.append(condition);
                }
            }

            System.out.println(sqlBuffer.toString());
            stmt = conn.prepareStatement(sqlBuffer.toString());
            cnt = stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;

    }


}
