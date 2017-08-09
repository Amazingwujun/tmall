package com.tmall.service.impl;

import com.tmall.common.annotation.Datasource;
import com.tmall.dao.DBDao.UserDao;
import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import com.tmall.utils.dynamicDatasource.DynamicDatasourceHandle;
import com.tmall.utils.email.EmailUtils;
import org.apache.shiro.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("userService")
public class UserServiceImpl implements IUserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailUtils emailUtils;

    private ExecutorService executor = Executors.newFixedThreadPool(3);

    /**
     * 通过用户名获取用户对象
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByUsername(String username) {
        Assert.notNull(username, "用户名不能为空");

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
        Assert.notNull(username, "用户名不能为空");

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
        Assert.notNull(username, "用户名不能为空");

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
                userExist(user.getPhone(), 3)||
                userExist(user.getUsername(),1)) return false; //保证username,email,phone的唯一性

            String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        int result = userDao.insertSelective(user);
        if (result > 0) {   //新开线程执行邮件发送任务,防止阻塞
            emailUtils.setUser(user); //注入邮件发送对象
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(emailUtils);
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
        User user = userDao.selectByUsername(username);
        if (user == null) {
            log.error("用户ID:{} 不存在", username);
            return false;
        }

        if (user.getValidate()) {
            log.info("用户邮箱:{} 已经验证", user.getEmail());
            return true;
        }

        String prefix = EmailUtils.prefix + user.getUsername();
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
}
