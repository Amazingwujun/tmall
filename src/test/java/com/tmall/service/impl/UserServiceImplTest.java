package com.tmall.service.impl;

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
    @Transactional
    public void register() throws Exception {
        User user = new User();
        user.setUsername("伍俊");
        user.setEmail("2447833078@qq.com");
        user.setPassword("416471");
        user.setPhone("18578458286");
        user.setRole(1);

        boolean result = userService.register(user);
      //  Assert.assertTrue("register failure",result);
    }

}