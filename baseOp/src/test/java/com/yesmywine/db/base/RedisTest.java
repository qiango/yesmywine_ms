package com.yesmywine.db.base;

import com.yesmywine.db.base.bean.DataSource;
import com.yesmywine.db.base.bean.DefaultData;
import com.yesmywine.db.base.biz.RedisCache;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by WANG, RUIQING on 12/13/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class RedisTest {


    static {
        DataSource dataSource = new DataSource("88.88.88.211", 3306);
        dataSource.setRedisHost("88.88.88.211");
        dataSource.setExpireSecond(3);

        DefaultData.setDataSource(dataSource);
    }


    @Test
    public void test() {
        Jedis jedis = new Jedis("localhost");
        String key = "heljfkd fjak f da lo";
        jedis.set(key, "1111122354");
        jedis.expire(key, 3);
        System.out.println(jedis.ping());

        if (null == jedis.get(key)) {
            System.out.println("this is null ");
        } else {
            System.out.println(jedis.get(key));
        }
    }


    @Test
    public void testI() {
        String sql = "select * from db ";
        RedisCache.set(sql, "jfkdaf", 100000);
        for (int i = 0; i < 1000; i++) {
            System.out.println(i + ":" + RedisCache.get(sql));
        }


    }


//	@Test
//	public void redislist(){
//		Jedis redis = RedisCache.getRedis();
//		String key = "testKey";
//		redis.lpush(key,"AAA");
//		redis.lpush(key,"bbb");
//		redis.lpush(key,"ccc");
//
//		for (int i = 0; i < 6; i++) {
//			String val = redis.rpop(key);
//			if (null == val) {
//				System.out.println("no value");
//			} else {
//				System.out.println(val);
//			}
//		}
//
//	}


}
