package com.tmall.dao.cacheDao;

import com.tmall.utils.RedisUtils;
import com.tmall.utils.SerializeUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;

public class RedisCache<K, V> implements Cache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(RedisCache.class);

    @Override
    public V get(K key) throws CacheException {
        if (key == null) return null;

        Jedis cache = null;
        try {
            cache = RedisUtils.getJedis();
            byte[] obj = SerializeUtils.obj2Byte(key);
            return (V) SerializeUtils.byte2Obj(cache.get(obj));
        } catch (Throwable e) {
            throw new CacheException(e);
        }finally {
            cache.close();
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        Jedis cache = null;
        try {
            cache = RedisUtils.getJedis();
            V previous = get(key);

            byte[] keyObj = SerializeUtils.obj2Byte(key);
            byte[] valueObj = SerializeUtils.obj2Byte(value);

            //设置过期时间 5分钟
            cache.setex(keyObj, 60 * 5, valueObj);
            return previous;
        } catch (IOException e) {
            throw new CacheException(e);
        }finally {
            cache.close();
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        Jedis cache = null;
        try {
            cache = RedisUtils.getJedis();

            V previous = get(key);
            byte[] keyObj = SerializeUtils.obj2Byte(key);
            cache.del(keyObj);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }finally {
            cache.close();
        }
    }

    @Override
    public void clear() throws CacheException {
        Jedis cache = null;
        try {
            cache = RedisUtils.getJedis();

            cache.flushDB();
        } catch (Throwable t) {
            throw new CacheException(t);
        }finally {
            cache.close();
        }
    }

    @Override
    public int size() {
        Jedis cache = null;
        try {
            cache = RedisUtils.getJedis();

            return cache.dbSize().intValue();
        }finally {
            cache.close();
        }
    }

    @Override
    public Set<K> keys() {
        Jedis cache = null;
        try {
            cache = RedisUtils.getJedis();
            
            Set<byte[]> keys = cache.keys("*".getBytes());
            if (!CollectionUtils.isEmpty(keys)) {
                HashSet<K> result = new HashSet<>(keys.size());
                for (byte[] k : keys) {
                    result.add((K) SerializeUtils.byte2Obj(k));
                }

                return result;
            }

            return Collections.EMPTY_SET;
        } catch (Throwable e) {
            throw new CacheException(e);
        }finally {
            cache.close();
        }
    }

    @Override
    public Collection values() {
        Jedis cache = null;
        try {
            cache = RedisUtils.getJedis();

            Set<byte[]> keySet = cache.keys("*".getBytes());
            List<byte[]> list = cache.mget(keySet.toArray(new byte[keySet.size()][]));

            if (CollectionUtils.isEmpty(list)) {
                return Collections.EMPTY_LIST;
            }

            List<V> values = new ArrayList<>(keySet.size());

            for (byte[] value : list) {
                values.add((V) SerializeUtils.byte2Obj(value));
            }

            return values;
        } catch (Throwable e) {
            throw new CacheException(e);
        } finally {
            cache.close();
        }
    }

    public static void main(String args[]){
        RedisCache<Object, Object> cache = new RedisCache<>();
        cache.keys();
        cache.put("wuqo", "hah");
    }
}
