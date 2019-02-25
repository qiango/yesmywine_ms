package com.yesmywine.db.base.biz;

import com.yesmywine.db.base.bean.DataSource;
import com.yesmywine.db.base.bean.DefaultData;
import com.yesmywine.db.base.bean.PageResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class PageQuery {

    private static PageResult query(DataSource dataSource, String sql, PageResult pageResult) {
//		DataSource dataSource = new DataSource("88.88.88.211",3306);
//		dataSource.setDatabaseType(DatabaseType.mysql);
//		dataSource.setUserName("root");
//		dataSource.setPassword("adminroot");
//		dataSource.setSchema("pay");
        Connection conn = null;
        Statement stmt = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Class.forName(dataSource.getDriver());
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUserName(), dataSource.getPassword());
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            String lowerSql = sql.toLowerCase();
            String cntSql = "select count(1) as cnt " + sql.substring(lowerSql.indexOf("from"));
            System.out.println(cntSql);
            ResultSet cntSet = stmt.executeQuery(cntSql);
            Map<String, Object> cntMap = DBMethod.resultToList(cntSet).get(0);
            pageResult.setTotalCount(Integer.valueOf(cntMap.get("cnt").toString()));
            System.out.println(sql);
            ResultSet resultSet = stmt.executeQuery(DBMethod.getPageSql(sql, pageResult, dataSource.getDatabaseType()));
            list = DBMethod.resultToList(resultSet);
            pageResult.setContent(list);
            resultSet.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pageResult;

    }


    public static PageResult runSql(String sql, PageResult pageResult) {
        return query(DefaultData.getDataSource(), sql, pageResult);
    }

    public static PageResult runSql(DataSource dataSource, String sql, PageResult pageResult) {
        return query(dataSource, sql, pageResult);
    }


    public static PageResult run(String tableName, PageResult pageResult) {
        return run(DefaultData.getDataSource(), tableName, pageResult);
    }


    public static PageResult run(DataSource dataSource, String tableName, PageResult pageResult) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" select * from ");
        sqlBuffer.append(tableName);
        String condition = pageResult.getCondition();
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
        return query(dataSource, sqlBuffer.toString(), pageResult);
    }


}
