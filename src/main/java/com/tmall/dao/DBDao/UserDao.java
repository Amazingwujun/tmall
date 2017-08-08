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

    Set<String> selectRolesNameByUserName(@Param("username") String username);

    Set<String> selectPermissionsByUserName(@Param("username") String username);

    /**
     * 查询关键参数是否被使用过
     *
     * @param username
     * @param email
     * @param phone
     * @return
     */
    Integer queryUserByEmailAndUsernameAndPhone(@Param("username") String username, @Param("email") String email,
                                                @Param("phone") String phone);
}