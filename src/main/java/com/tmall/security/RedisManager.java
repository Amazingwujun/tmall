package com.tmall.security;

import com.tmall.dao.cacheDao.RedisCache;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.ConcurrentHashMap;

public class RedisManager implements CacheManager {


    private ConcurrentHashMap<String, Cache> map = new ConcurrentHashMap<>();

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache cache = map.get(name);
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = new RedisCache<>();
                    map.put(name, cache);
                }
            }
        }

        return cache;
    }
}
