package com.tmall.service.impl;

import com.tmall.dao.UserDao;
import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("userService")
public class UserServiceImpl implements IUserService{

    @Autowired
    UserDao userDao;

    /**
     *
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByUsername(String username) {
        return userDao.selectByUsername(username);
    }

    public Set<String> getRoleNamesByUsername(String username) {
        return userDao.selectRolesNameByUserName(username);
    }

    public Set<String> getPermissionsByUserName(String username) {
        return userDao.selectPermissionsByUserName(username);
    }
}
