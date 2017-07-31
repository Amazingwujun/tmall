package com.tmall.controller.portal;

import com.tmall.common.JSONObject;
import com.tmall.common.validator.First;
import com.tmall.common.validator.Second;
import com.tmall.entity.po.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController{

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @RequestMapping("login")
    public JSONObject login(@Validated(First.class) User user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        return JSONObject.successWithMessage("登录成功");
    }

    @RequestMapping("logout")
    public JSONObject logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() == null) {
            return JSONObject.successWithMessage("用户已经注销");
        }

        subject.logout();
        return JSONObject.successWithMessage("注销成功");
    }

    @RequiresRoles("admin")
    @RequestMapping("test")
    public JSONObject test(User user) {
        System.out.println(user);
        return JSONObject.successWithMessage("nani");
    }

    @RequestMapping("register")
    public JSONObject register(@Validated(Second.class) User user) {
        System.out.println(user);

        return null;
    }
}
