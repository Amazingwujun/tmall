package com.tmall.service.impl;

import com.tmall.common.annotation.Datasource;
import com.tmall.dao.DBDao.UserDao;
import com.tmall.dao.cacheDao.RedisCache;
import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import com.tmall.utils.DynamicDatasourceHandle;
import com.tmall.utils.EmailUtils;
import org.apache.shiro.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

@Service("userService")
public class UserServiceImpl implements IUserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    UserDao userDao;

    @Autowired
    EmailUtils emailUtils;

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

        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        int result = userDao.insertSelective(user);
        if (result > 0) {
            // TODO: 2017/8/8 应该使用线程池异步处理
            emailUtils.send(user); //发送验证邮箱
        }
        return result > 0;
    }


    /**
     * 验证用户邮箱
     *
     * @param userId 用户ID
     * @param code   用户上传验证码
     * @param cache  RedisCache
     * @return
     */
    @Override
    @Transactional
    @Datasource(DynamicDatasourceHandle.LOCAL_DB)
    public boolean emailValidate(Integer userId, String code, Cache cache) {
        User user = userDao.selectByPrimaryKey(userId);
        if (user == null) {
            log.error("用户ID:{} 不存在", userId);
            return false;
        }

        if (user.getValidate()) {
            log.info("用户邮箱:{} 已经验证", user.getEmail());
            return true;
        }

        String prefix = EmailUtils.prefix + user.getUsername();
        String cacheCode = (String) cache.get(prefix);
        if (cacheCode != null && cacheCode.equals(code)) {
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
     * @param type
     * @return
     */
    public boolean userExsit(String query, Integer type) {
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
