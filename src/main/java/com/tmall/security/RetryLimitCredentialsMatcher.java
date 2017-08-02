package com.tmall.security;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 验证器，增加了登录次数校验功能
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {
    private static final Logger log = LoggerFactory.getLogger(RetryLimitCredentialsMatcher.class);

    //集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
    //集群请使用redis作为cache
    private Cache<String, AtomicInteger> retryCache;

    private static final String defualtCacheName = "loginRetryCache";

    private int maxRetryCount = 5;


    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public RetryLimitCredentialsMatcher(CacheManager cacheManager, String cacheName) {
        if (cacheName == null) {
            retryCache = cacheManager.getCache(defualtCacheName);
        }else {
            retryCache = cacheManager.getCache(cacheName);
        }
    }

    /**
     *
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = retryCache.get(username);
        if (null == retryCount) {
            retryCount = new AtomicInteger(0);
            retryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            log.warn("username: " + username + " tried to login more than 5 times in period");
            throw new ExcessiveAttemptsException("username: " + username + " tried to login more than 5 times in period"
            );
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //clear retry data
            retryCache.remove(username);
        }
        return matches;
    }
}