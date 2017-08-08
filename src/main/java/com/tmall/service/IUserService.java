package com.tmall.service;

import com.tmall.entity.po.User;

import java.util.Set;

public interface IUserService {

    /**
     *
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     *
     * @param username
     * @return
     */
    Set<String> getRoleNamesByUsername(String username);

    /**
     *
     * @param username
     * @return
     */
    Set<String> getPermissionsByUserName(String username);

    /**
     *
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     *
     * @param query 查询字符串
     * @param type 1_username,2_email,3_phone
     * @return
     */
    boolean userExsit(String query, Integer type);
}
