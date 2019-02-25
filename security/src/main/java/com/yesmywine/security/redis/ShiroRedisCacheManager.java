package com.yesmywine.security.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by SJQ on 2017/5/27.
 */
@Component
@EnableCaching
public class ShiroRedisCacheManager implements CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(ShiroRedisCacheManager.class);

    // fast lookup by name map
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    @Autowired
    private ShiroRedisManager redisManager;

    /**
     * The Redis key prefix for caches
     */
    private String keyPrefix = "shiro_redis_cache:";

    /**
     * Returns the Redis session keys
     * prefix.
     * @return The prefix
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * Sets the Redis sessions key
     * prefix.
     * @param keyPrefix The prefix
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        System.out.println("获取名称为: " + name + " 的RedisCache实例");

        Cache c = caches.get(name);

        if (c == null) {

            // initialize the Redis manager instance
            redisManager.init();

            // create a new cache instance
            c = new ShiroRedisCache<K,V>(redisManager, keyPrefix);

            // add it to the cache collection
            caches.put(name, c);
        }
        return c;
    }

    public ShiroRedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(ShiroRedisManager redisManager) {
        this.redisManager = redisManager;
    }
}
