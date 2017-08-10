package com.tmall.service.impl;

import com.tmall.common.annotation.Datasource;
import com.tmall.common.constant.Euser;
import com.tmall.dao.DBDao.UserDao;
import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import com.tmall.utils.dynamicDatasource.DynamicDatasourceHandle;
import com.tmall.utils.email.EmailUtils;
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
     *
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
     *
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
     *
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
     *
     * @return
     */
    @Override
    public boolean register(User user) {
        Assert.notNull(user, "注册用户不能为空");

        //保证username,email,phone的唯一性
        if (userExist(user.getEmail(), 2) || userExist(user.getPhone(), 3) ||
                userExist(user.getUsername(), 1)) {
            log.debug("用户注册的用户名或邮箱或电话已经存在");
            return false;
        }

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
     *
     * @return true, 如果操作成功
     */
    @Override
    @Transactional
    @Datasource(DynamicDatasourceHandle.REMOTE_DB)
    public boolean emailValidate(String username, String token, Cache cache) {
        if (StringUtils.hasText(username) || StringUtils.hasText(token) || cache == null) {
            throw new IllegalArgumentException("方法参数异常");
        }

        User user = userDao.selectByUsername(username);
        if (user == null) {
            log.error("用户ID:{} 不存在", username);
            return false;
        }

        if (user.getValidate()) {
            log.debug("用户邮箱:{} 已经验证", user.getEmail());
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
     * @param query 查询参数
     * @param type  1_username,2_email,3_phone
     *
     * @return true, 如果操作成功
     */
    public boolean userExist(String query, Integer type) {
        if (StringUtils.hasText(query) || type == null) {
            throw new IllegalArgumentException("方法参数异常");
        }

        Integer result = 0;

        if (Euser.USERNAME.getKey() == type) {
            result = userDao.queryUserByUsername(query);
        }

        if (Euser.EMAIL.getKey() == type) {
            result = userDao.queryUserByEmail(query);
        }

        if (Euser.PHONE.getKey() == type) {
            result = userDao.queryUserByPhone(query);
        }

        return result > 0;
    }

    /**
     * 忘记密码,发送验证邮件
     *
     * @param key  确定用户身份的key,username or email
     * @param type 1_username,2_email
     *
     * @return true, 如果操作成功
     */
    public boolean forgetPassword(String key, Integer type) {
        if (StringUtils.hasText(key) || type == null) {
            throw new IllegalArgumentException("方法参数异常");
        }

        User user;

        if (Euser.USERNAME.getKey() == type) {
            //用户名
            user = userDao.selectByUsername(key);
            synchronized (this) {
                emailUtils.setStrategy(user, Euser.USERNAME.getKey());
                executor.execute(emailUtils);
            }
            return true;
        } else if (Euser.EMAIL.getKey() == type) {
            //邮箱
            user = userDao.selectByEmail(key);
            synchronized (this) {
                emailUtils.setStrategy(user, Euser.EMAIL.getKey());
                executor.execute(emailUtils);
            }
            return true;
        } else {
            log.error("参数type:{} 无法处理", type);
            return false;
        }
    }

    /**
     * 重置密码:
     *
     * @param username 用户名
     * @param password 新密码
     * @param token    用于重置密码的令牌
     *
     * @return true, 如果操作成功
     */
    public boolean resetPassword(String username, String password, String token) {
        if (StringUtils.hasText(username) || StringUtils.hasText(password) || StringUtils.hasText(token)) {
            throw new IllegalArgumentException("方法参数异常");
        }

        String key = EmailUtils.EMAIL_FORGETPASSWORD_TOKEN + username; //获得key
        Cache<Object, Object> cache = cacheManager.getCache(com.tmall.common.constant.Cache.COMMON_USE_CACHE);
        String tokenCache = (String) cache.get(key);
        if (!token.equals(tokenCache)) {
            log.info("用户:{} 重置密码的token不匹配", username);
            return false;
        } else {
            log.debug("从缓存中移除token");
            cache.remove(key);
        }

        Integer result = userDao.updatePasswordByUsername(username, DigestUtils.md5DigestAsHex(password.getBytes()));
        return result > 0;
    }

    /**
     * 登录状态下修改密码
     *
     * @param username    用户名
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     *
     * @return true, 如果操作成功
     */
    @Transactional
    public boolean onlineResetPassword(String username, String newPassword, String oldPassword) {
        if (StringUtils.hasText(username) || StringUtils.hasText(newPassword) || StringUtils.hasText(oldPassword)) {
            throw new IllegalArgumentException("方法参数异常");
        }

        User user = userDao.selectByUsername(username);
        if (user == null) {
            log.debug("用户:{} 不存在", username);
            return false;
        }

        if (!DigestUtils.md5DigestAsHex(oldPassword.getBytes()).equals(user.getPassword())) {
            log.debug("用户:{} 的旧密码错误", username);
            return false;
        }

        Integer result = userDao.updatePasswordByUsername(username, DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        return result > 0;
    }

}
