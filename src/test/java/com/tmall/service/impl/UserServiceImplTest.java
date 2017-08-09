package com.tmall.service.impl;

import com.tmall.dao.cacheDao.RedisCache;
import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        {"classpath:spring/spring-core.xml"}
)
public class UserServiceImplTest {
    @Autowired
    IUserService userService;

    @Test
    public void register() throws Exception {
        User user = new User();
        user.setValidate(false);
        user.setUsername("emailTest");
        user.setPassword("123456");
        user.setRole(2);
        user.setPhone("18878787878");
        user.setEmail("3306770886@qq.com");
        boolean result = userService.register(user);
        Assert.assertTrue(result);
    }

    @Test
    public void getUserByUsername() throws Exception {
    }

    @Test
    public void getRoleNamesByUsername() throws Exception {
    }

    @Test
    public void getPermissionsByUserName() throws Exception {
    }

    @Test
    public void emailValidate() throws Exception {
        boolean result = userService.emailValidate("emailemail", "14a4225f-6bc2-4fdb-aa22-7948cecbe04e", new RedisCache());
        Assert.assertTrue(result);
    }


    @Test
    public void testGetUserByUsername() throws Exception {
        User user = userService.getUserByUsername("wutian");
        Assert.assertNotNull(user);
    }

    @Test
    public void testGetRoleNamesByUsername() throws Exception {
        Set<String> wutian = userService.getRoleNamesByUsername("wutian");
        List list = new ArrayList<>();
        list.addAll(wutian);
        System.out.println(list);
    }

    @Test
    public void testGetPermissionsByUserName() throws Exception {
        Set<String> permissionsByUserName = userService.getPermissionsByUserName("伍俊");
        List list = new ArrayList<>();
        list.addAll(permissionsByUserName);
        System.out.println(list);
    }

    @Test
    public void testUserExsit() throws Exception {
        boolean userExsit = userService.userExist("85998282@qq.com", 2);
        Assert.assertTrue(userExsit);
    }
}