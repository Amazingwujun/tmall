package com.tmall.controller;

import com.tmall.common.JSONObject;
import com.tmall.entity.po.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController extends BaseController{

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("login")
    public JSONObject login(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError error : errors) {
                log.error(error.getDefaultMessage());
            }
        }

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();

        subject.login(token);

        return JSONObject.SuccessWithMessage("login success");
    }

    @RequiresRoles("admin")
    @RequestMapping("test")
    public JSONObject test() {
        return null;
    }

}
