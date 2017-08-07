package com.tmall.security;


import com.tmall.dao.cacheDao.RedisCache;
import com.tmall.utils.RedisUtils;
import com.tmall.utils.SerializeUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 验证器，增加了登录次数校验功能
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {
    private static final Logger log = LoggerFactory.getLogger(RetryLimitCredentialsMatcher.class);

    //集群请使用redis作为cache
    private RedisCache<String, AtomicInteger> retryCache;

    private String defualtCacheName = "loginRetryCache";

    private int maxRetryCount = 5;

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public RetryLimitCredentialsMatcher(CacheManager cacheManager, String cacheName) {
        Assert.notNull(cacheManager, "缓存管理对象不能为空");

        if (cacheName == null) {
            retryCache = (RedisCache) cacheManager.getCache(defualtCacheName);
        } else {
            defualtCacheName = cacheName;
            retryCache = (RedisCache) cacheManager.getCache(cacheName);
        }
    }

    /**
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();

        AtomicInteger retryCount = retryCache.get(defualtCacheName + ":" + username);

        if (null == retryCount) {
            retryCount = new AtomicInteger(0);

            //五分钟过期时间
            retryCache.put(defualtCacheName + ":" + username, 5 * 60, retryCount);
        }

        if (retryCount.incrementAndGet() > maxRetryCount) {
            log.warn("用户: " + username + " 登录超过五次");
            throw new ExcessiveAttemptsException("用户: " + username + " 登录超过五次");
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //clear retry data
            retryCache.remove(defualtCacheName + ":" + username);
        } else {
            retryCache.put(defualtCacheName + ":" + username, 5 * 60, retryCount);
        }
        return matches;
    }
}