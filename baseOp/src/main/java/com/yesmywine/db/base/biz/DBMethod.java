package com.yesmywine.db.base.biz;

import com.yesmywine.db.base.bean.DatabaseType;
import com.yesmywine.db.base.bean.PageResult;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class DBMethod {

    public static List<Map<String, Object>> resultToList(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData md = resultSet.getMetaData();
        int columns = md.getColumnCount();
        while (resultSet.next()) {
            HashMap row = new HashMap(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), resultSet.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

    public static String getPageSql(String sql, PageResult pageResult, DatabaseType databaseType) {
        String pageSql = "";
        switch (databaseType) {
            case mysql:
                int startRow = (pageResult.getCurrentPage() - 1) * pageResult.getPageSize();
                pageSql = sql + " limit " + startRow + " , " + pageResult.getPageSize();
                break;
        }
        return pageSql;
    }

    public static String getDataSql(Object obj) {
        String dataSql = "";
        switch (obj.getClass().toString()) {
            case "class java.lang.String":
                dataSql = "'" + obj.toString() + "'";
                break;
            case "class java.lang.Integer":
                dataSql = obj.toString();
            case "int":
                dataSql = obj.toString();
                break;
            case "class java.util.Date":
                dataSql = obj.toString();
                break;
        }
        return dataSql;
    }

}
