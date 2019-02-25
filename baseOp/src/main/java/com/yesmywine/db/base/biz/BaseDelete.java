package com.yesmywine.db.base.biz;

import com.yesmywine.db.base.bean.DataSource;
import com.yesmywine.db.base.bean.DefaultData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class BaseDelete {


    public static Integer run(String tableName, String condition) {
        return run(DefaultData.getDataSource(), tableName, condition);
    }

    private static Integer run(DataSource dataSource, String tableName, String condition) {
        Connection conn = null;
        PreparedStatement stmt = null;
        Integer cnt = null;
        try {
            Class.forName(dataSource.getDriver());
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUserName(), dataSource.getPassword());
            conn.setAutoCommit(true);

            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append(" delete from ");
            sqlBuffer.append(tableName);

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
