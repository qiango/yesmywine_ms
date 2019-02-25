package com.yesmywine.db.base.biz;

import com.yesmywine.db.base.bean.DataSource;
import com.yesmywine.db.base.bean.DatabaseType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class RunTest {

    public static void main(String[] args) {

        DataSource dataSource = new DataSource("88.88.88.211", 3306);
        dataSource.setDatabaseType(DatabaseType.mysql);
        dataSource.setUserName("root");
        dataSource.setPassword("adminroot");
        dataSource.setSchema("pay");

        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(dataSource.getDriver());
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUserName(), dataSource.getPassword());
            stmt = conn.createStatement();
            String sql = "select * from payment";
            ResultSet resultSet = stmt.executeQuery(sql);

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();
            List<Map<String, Object>> list = new ArrayList<>();
            while (resultSet.next()) {
                HashMap row = new HashMap(columns);
                for (int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), resultSet.getObject(i));
                }
                list.add(row);
            }

            System.out.println(list.size());
            System.out.println(list.toString());


            resultSet.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
