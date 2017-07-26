package com.tmall.controller;

import com.tmall.common.JSONObject;
import com.tmall.entity.po.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController extends BaseController{

    @RequestMapping("login")
    public JSONObject login(User user, BindingResult bindingResult) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();

        subject.login(token);

        return JSONObject.SuccessWithMessage("login success");
    }

    @RequestMapping("test")
    public JSONObject test() {

    }

}
