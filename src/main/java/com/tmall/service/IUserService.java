package com.tmall.service;

import com.tmall.entity.po.User;
import org.apache.shiro.cache.Cache;

import java.util.Set;

public interface IUserService {

    User getUserByUsername(String usernam);

    Set<String> getRoleNamesByUsername(String username);

    Set<String> getPermissionsByUserName(String username);

    boolean register(User user);

    boolean emailValidate(Integer userId, String code, Cache cache);
}
