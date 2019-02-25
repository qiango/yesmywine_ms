package com.yesmywine.security.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by SJQ on 2017/6/5.
 */
public class ShiroCache<K, V> implements Cache<K, V> {
    private static final String REDIS_SHIRO_CACHE = "weiyou-shiro-cache:";
    private String cacheKey;
    private RedisTemplate<K, V> redisTemplate;
    private long globExpire = 30;

    @SuppressWarnings("rawtypes")
    public ShiroCache(String name, RedisTemplate client) {
        this.cacheKey = REDIS_SHIRO_CACHE + name + ":";
        this.redisTemplate = client;
    }

    public V get(K key) throws CacheException {
        redisTemplate.boundValueOps(getCacheKey(key)).expire(globExpire, TimeUnit.MINUTES);
        return redisTemplate.boundValueOps(getCacheKey(key)).get();
    }

    public V put(K key, V value) throws CacheException {
        V old = get(key);
        redisTemplate.boundValueOps(getCacheKey(key)).set(value);
        return old;
    }

    public V remove(K key) throws CacheException {
        V old = get(key);
        redisTemplate.delete(getCacheKey(key));
        return old;
    }

    public void clear() throws CacheException {
        redisTemplate.delete(keys());
    }

    public int size() {
        return keys().size();
    }

    public Set<K> keys() {
        return redisTemplate.keys(getCacheKey("*"));
    }

    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<V>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }

    private K getCacheKey(Object k) {
        return (K) (this.cacheKey + k);
    }
}
