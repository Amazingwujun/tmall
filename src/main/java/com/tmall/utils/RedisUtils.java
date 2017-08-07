package com.tmall.utils;


import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

public class RedisUtils {

    //日志
    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    //config
    private static JedisPool jedisPool;
    private JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

    //初始化Jedis连接池
    static {
        jedisPool = new JedisPool();
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }


}
