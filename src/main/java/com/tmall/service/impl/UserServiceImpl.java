package com.tmall.service.impl;

import com.tmall.dao.DBDao.UserDao;
import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.util.Set;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    UserDao userDao;

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
        return result > 0;
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
