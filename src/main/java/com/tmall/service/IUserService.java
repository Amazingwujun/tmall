package com.tmall.service;

import com.tmall.entity.po.User;
import org.apache.shiro.cache.Cache;

import java.util.Set;

public interface IUserService {

    /**
     * 通过用户名获取用户对象
     *
     * @param username
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 通过用户名获得用户角色
     *
     * @param username
     * @return
     */
    Set<String> getRoleNamesByUsername(String username);

    /**
     * 通过用户名获取权限
     *
     * @param username
     * @return
     */
    Set<String> getPermissionsByUserName(String username);

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * 通过查询参数和类型，检查用户是否存在
     *
     * @param query
     * @param type  1_username,2_email,3_phone
     * @return
     */
    boolean userExist(String query, Integer type);

    /**
     * 验证用户邮箱
     *
     * @param username 用户ID
     * @param token    用户上传验证码
     * @param cache    RedisCache
     * @return
     */
    boolean emailValidate(String username, String token, Cache cache);

    /**
     * 忘记密码
     *
     * @param key
     * @param type
     */
    boolean forgetPassword(String key,Integer type);

    /**
     * 重置密码
     *
     * @param username
     * @param password
     * @param token
     * @return
     */
    boolean resetPassword(String username, String password, String token);
}
