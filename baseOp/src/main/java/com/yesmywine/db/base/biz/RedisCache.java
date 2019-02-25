package com.yesmywine.db.base.biz;

import com.yesmywine.db.base.bean.DataSource;
import com.yesmywine.db.base.bean.DefaultData;
import com.yesmywine.util.basic.ValueUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by WANG, RUIQING on 12/13/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class RedisCache {

    private static Map<String, JedisPool> pools = new HashMap<>();

    private static Jedis getConnect(DataSource dataSource) {
        String key = dataSource.getRedisHost() + ":" + dataSource.getRedisPort();
        JedisPool connectPool = pools.get(key);
        if (null == connectPool) {
            JedisPool pool = new JedisPool(dataSource.getRedisHost(), dataSource.getRedisPort());
            pools.put(key, pool);
            return pool.getResource();
        } else {
            return connectPool.getResource();
        }
    }

    public static String get(DataSource dataSource, String key) {
//		Jedis redis = new Jedis(dataSource.getRedisHost(),dataSource.getRedisPort());
        Jedis redis = getConnect(dataSource);
        String data = redis.get(key);
        redis.close();
        return data;
    }

    public static void set(DataSource dataSource, String key, Object data) {
        Jedis redis = getConnect(dataSource);
        redis.set(key, ValueUtil.toString(data));
        redis.expire(key, dataSource.getExpireSecond());
        redis.close();
    }

    public static void set(DataSource dataSource, String sql, String data, Integer cacheSeconds) {
        Jedis redis = getConnect(dataSource);
        redis.set(sql, data);
        redis.expire(sql, cacheSeconds);
        redis.close();
    }

    public static String get(String key) {
//		Jedis redis = new Jedis(dataSource.getRedisHost(),dataSource.getRedisPort());
        Jedis redis = getConnect(DefaultData.getDataSource());
        String data = redis.get(key);
        redis.close();
        return data;
    }

    public static String get(String key, Integer index) {
//		Jedis redis = new Jedis(dataSource.getRedisHost(),dataSource.getRedisPort());
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.select(index);
        String data = redis.get(key);
        redis.close();
        return data;
    }

    public static void set(String key, Object data) {
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.set(key, ValueUtil.toString(data));
        redis.expire(key, DefaultData.getDataSource().getExpireSecond());
        redis.close();
    }

    public static void set(String key, Object data, Integer index) {
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.select(index);
        redis.set(key, ValueUtil.toString(data));
        redis.expire(key, DefaultData.getDataSource().getExpireSecond());
        redis.close();
    }

    public static void set(String sql, String data, Integer cacheSeconds) {
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.set(sql, data);
        redis.expire(sql, cacheSeconds);
        redis.close();
    }

    //刷新时间
    public static void expire(DataSource dataSource, String sql, Integer cacheSeconds) {
        Jedis redis = getConnect(dataSource);
        redis.expire(sql, cacheSeconds);
        redis.close();
    }

    public static void expire(String sql, Integer cacheSeconds) {
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.expire(sql, cacheSeconds);
        redis.close();
    }

    public static void expire(String sql, Integer cacheSeconds, Integer index) {
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.select(index);
        redis.expire(sql, cacheSeconds);
        redis.close();
    }

    public static void putQueue(String key, String value) {
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.lpush(key, value);
        redis.close();
    }

    public static void getQueue(String key) {
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.rpop(key);
        redis.close();
    }

    public static void putStack(String key, String value) {
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.lpush(key, value);
        redis.close();
    }

    public static void getStack(String key) {
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.lpop(key);
        redis.close();
    }

    public static void putQueue(DataSource dataSource, String key, String value) {
        Jedis redis = getConnect(dataSource);
        redis.lpush(key, value);
        redis.close();
    }

    public static void getQueue(DataSource dataSource, String key) {
        Jedis redis = getConnect(dataSource);
        redis.rpop(key);
        redis.close();
    }

    public static void putStack(DataSource dataSource, String key, String value) {
        Jedis redis = getConnect(dataSource);
        redis.lpush(key, value);
        redis.close();
    }

    public static void getStack(DataSource dataSource, String key) {
        Jedis redis = getConnect(dataSource);
        redis.lpop(key);
        redis.close();
    }

    public static void selectDatabase(Integer index){
        Jedis redis = getConnect(DefaultData.getDataSource());
        redis.select(index);
        redis.close();
    }

    public String getUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        Jedis redis = getConnect(DefaultData.getDataSource());
        String result = redis.get(token);
        if(ValueUtil.isEmpity(result)){
            return "";
        }else {
            redis.expire(token, 30 * 60);
            return ValueUtil.getFromJson(result, "data");
        }
    }


}
