package com.tmall.dao.DBDao;

import com.tmall.entity.po.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

public interface UserDao {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUsername(@Param("username") String username);

    /**
     * 根据用户名获得用户角色
     *
     * @param username
     * @return
     */
    Set<String> selectRolesNameByUserName(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    /**
     * 根据用户名获取权限
     *
     * @param username
     * @return
     */
    Set<String> selectPermissionsByUserName(@Param("username") String username);

    /**
     * 根据用户名查询用户是否存在
     *
     * @param username
     * @return
     */
    Integer queryUserByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户是否存在
     *
     * @param email
     * @return
     */
    Integer queryUserByEmail(@Param("email") String email);

    /**
     * 根据电话号码查询用户是否存在
     *
     * @param phone
     * @return
     */
    Integer queryUserByPhone(@Param("phone") String phone);

    /**
     * 根据用户名重置密码
     *
     * @param username
     * @param password
     * @return
     */
    Integer updatePasswordByUsername(@Param("username") String username, @Param("password") String password);
}