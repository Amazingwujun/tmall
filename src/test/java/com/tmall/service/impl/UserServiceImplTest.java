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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

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
        boolean result = userService.emailValidate(25, "14a4225f-6bc2-4fdb-aa22-7948cecbe04e", new RedisCache());
        Assert.assertTrue(result);
    }

}