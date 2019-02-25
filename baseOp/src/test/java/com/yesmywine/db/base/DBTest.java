package com.yesmywine.db.base;

//import DataSource;
//import DatabaseType;
//import DefaultData;
//import PageResult;
//import com.hzbuvi.db.base.biz.*;
//import com.hzbuvi.log.DefaultLogger;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;

/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class DBTest {

//
//	static {
//		DataSource dataSource = new DataSource("88.88.88.211",3306);
//		dataSource.setDatabaseType(DatabaseType.mysql);
//		dataSource.setUserName("root");
//		dataSource.setPassword("adminroot");
//		dataSource.setSchema("dic");
//
//		DefaultData.setDataSource(dataSource);
//	}
//
//
//	@Test
//	public void test(){
//		DataSource dataSource = new DataSource("88.88.88.211",3306);
//		dataSource.setDatabaseType(DatabaseType.mysql);
//		dataSource.setUserName("root");
//		dataSource.setPassword("adminroot");
//		dataSource.setSchema("dic");
//
//		DefaultData.setDataSource(dataSource);
//
////		System.out.println(BaseQuery.run("dicentity","id=1 ").toString());
//
//		PageResult pageResult = new PageResult(2,2);
//		pageResult = PageQuery.run("dicentity",pageResult);
//		System.out.println(pageResult);
//
//
//
//	}
//
//	@Test
//	public void testStr(){
//		String strClass = String.class.toString();
//		String integerClass = Integer.class.toString();
//		String intClass = int.class.toString();
//		String dateClass = Date.class.toString();
//
//		System.out.println(strClass);
//		System.out.println(integerClass);
//		System.out.println(intClass);
//		System.out.println(dateClass);
//
//	}
//
//	@Test
//	public void insert(){
//		Map<String,Object> map = new HashMap<>();
//		map.put("entityCode","testjdbc");
//		map.put("entityValue","jjfkd23242kfdajfh");
//		map.put("sysCode","en");
//
//		Map<String,Object> map1 = new HashMap<>();
//		map1.put("entityCode","testjjkfdjakfdbc");
//		map1.put("entityValue","j343jfk");
//		map1.put("sysCode","en");
//		List<Map<String,Object>> list = new ArrayList<>();
//		list.add(map);
//		list.add(map1);
//
//		BaseInsert.run(DefaultData.getDataSource(),"dicentity",list);
//
//	}
//
//	@Test
//	public void update(){
//		Map<String,Object> map = new HashMap<>();
//		map.put("entityCode","hello");
//		map.put("entityValue","j000000");
//		map.put("sysCode","enh");
//		BaseUpdate.run("dicentity",map," id = 17 ");
//
//	}
//
//	@Test
//	public void delete (){
//		BaseDelete.run("dicentity","id = 16");
//	}
//
//
//	@Test
//	public void lgotest(){
//		DefaultLogger.debug("hello from base op");
//		Logger logger = LoggerFactory.getLogger(DBTest.class);
//		logger.debug("jfkdajf");
//	}

}
