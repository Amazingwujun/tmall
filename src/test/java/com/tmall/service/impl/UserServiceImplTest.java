package com.tmall.service.impl;

import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
        boolean userExsit = userService.userExsit("85998282@qq.com", 2);
        Assert.assertTrue(userExsit);
    }
}