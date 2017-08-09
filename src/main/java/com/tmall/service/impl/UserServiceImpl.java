package com.tmall.service.impl;

import com.tmall.common.annotation.Datasource;
import com.tmall.dao.DBDao.UserDao;
import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import com.tmall.utils.dynamicDatasource.DynamicDatasourceHandle;
import com.tmall.utils.email.EmailUtils;
import com.tmall.utils.redis.RedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("userService")
public class UserServiceImpl implements IUserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserDao userDao;

    /**
     *
     */
    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private CacheManager cacheManager;

    /**
     * 用来执行异步的任务，比如邮件发送
     */
    private ExecutorService executor = Executors.newFixedThreadPool(3);

    /**
     * 通过用户名获取用户对象
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByUsername(String username) {
        Assert.hasText(username, "用户名不能为空");

        return userDao.selectByUsername(username);
    }

    /**
     * 通过用户名获得用户角色
     *
     * @param username
     * @return
     */
    @Override
    public Set<String> getRoleNamesByUsername(String username) {
        Assert.hasText(username, "用户名不能为空");

        return userDao.selectRolesNameByUserName(username);
    }

    /**
     * 通过用户名获取权限
     *
     * @param username
     * @return
     */
    @Override
    public Set<String> getPermissionsByUserName(String username) {
        Assert.hasText(username, "用户名不能为空");

        return userDao.selectPermissionsByUserName(username);
    }

    /**
     * 普通用户注册
     *
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        Assert.notNull(user, "注册用户不能为空");

        if (userExist(user.getEmail(), 2) ||
                userExist(user.getPhone(), 3) ||
                userExist(user.getUsername(), 1)) return false; //保证username,email,phone的唯一性

        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        int result = userDao.insertSelective(user);
        if (result > 0) {   //新开线程执行邮件发送任务,防止阻塞
            synchronized (this) {
                emailUtils.setStrategy(user, 1); //注入邮件发送对象
                executor.execute(emailUtils);
            }
        }

        return result > 0;
    }


    /**
     * 验证用户邮箱
     *
     * @param username 用户ID
     * @param token    用户上传验证码
     * @param cache    RedisCache
     * @return
     */
    @Override
    @Transactional
    @Datasource(DynamicDatasourceHandle.REMOTE_DB)
    public boolean emailValidate(String username, String token, Cache cache) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token) || cache == null) {
            throw new IllegalArgumentException("方法参数异常");
        }

        User user = userDao.selectByUsername(username);
        if (user == null) {
            log.error("用户ID:{} 不存在", username);
            return false;
        }

        if (user.getValidate()) {
            log.info("用户邮箱:{} 已经验证", user.getEmail());
            return true;
        }

        String prefix = EmailUtils.EMAIL_VALIDATE_TOKEN + user.getUsername();
        String cacheCode = (String) cache.get(prefix);
        if (cacheCode != null && cacheCode.equals(token)) {
            user.setValidate(true);
            cache.remove(prefix);
            int i = userDao.updateByPrimaryKeySelective(user);
            return i > 0;
        } else {
            return false;   //校检码过期或校检码比对不通过
        }
    }

    /**
     * 通过查询参数和类型，检查用户是否存在
     *
     * @param query
     * @param type  1_username,2_email,3_phone
     * @return
     */
    public boolean userExist(String query, Integer type) {
        if (StringUtils.isEmpty(query) || type == null) {
            throw new IllegalArgumentException("方法参数异常");
        }

        Integer result;

        switch (type) {
            case 1:
                //根据用户名查询
                result = userDao.queryUserByUsername(query);
                break;
            case 2:
                //根据邮箱查询
                result = userDao.queryUserByEmail(query);
                break;
            case 3:
                //根据手机号码查询
                result = userDao.queryUserByPhone(query);
                break;
            default:
                throw new IllegalArgumentException("参数type:" + type + "异常");
        }

        return result > 0;
    }

    /**
     * @param key
     * @param type
     */
    public boolean forgetPassword(String key, Integer type) {
        if (StringUtils.isEmpty(key) || type == null) {
            throw new IllegalArgumentException("方法参数异常");
        }

        User user;

        if (1 == type) {
            //用户名
            user = userDao.selectByUsername(key);
            synchronized (this) {
                emailUtils.setStrategy(user, 2);
                executor.execute(emailUtils);
            }
            return true;
        } else if (2 == type) {
            //邮箱
            user = userDao.selectByEmail(key);
            synchronized (this) {
                emailUtils.setStrategy(user, 2);
                executor.execute(emailUtils);
            }
            return true;
        } else {
            log.error("参数type:{} 异常", type);
            return false;
        }
    }

    /**
     * 重置密码
     *
     * @param username
     * @param password
     * @param token
     * @return
     */
    public boolean resetPassword(String username, String password, String token) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(token)) {
            log.error("参数不能为空");
            return false;
        }

        String key = EmailUtils.EMAIL_FORGETPASSWORD_TOKEN + username; //获得key
        Cache<Object, Object> cache = cacheManager.getCache(com.tmall.common.constant.Cache.COMMON_USE_CACHE);
        String tokenCache = (String) cache.get(key);
        if (!token.equals(tokenCache)) {
            log.info("用户:{} 重置密码的token不匹配", username);
            return false;
        } else {
            cache.remove(key);
        }

        Integer result = userDao.updatePasswordByUsername(username, DigestUtils.md5DigestAsHex(password.getBytes()));
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

}
