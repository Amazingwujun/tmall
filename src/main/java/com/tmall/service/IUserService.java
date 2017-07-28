package com.tmall.service;

import com.tmall.entity.po.User;

import java.util.Set;

public interface IUserService {

    User getUserByUsername(String usernam);

    Set<String> getRoleNamesByUsername(String username);

    Set<String> getPermissionsByUserName(String username);
}
