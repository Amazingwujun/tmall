package com.tmall.controller.portal;

import com.tmall.entity.vo.JSONObject;
import com.tmall.common.validator.Login;
import com.tmall.common.validator.Register;
import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import com.tmall.utils.EmailUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    IUserService userService;

    @Autowired
    CacheManager cacheManager;

    /**
     * 用户登录,登录异常全部交给{@link com.tmall.controller.ExceptionHandleController} 处理
     *
     * @param user
     * @return JSONObject
     */
    @RequestMapping("login")
    public JSONObject login(@Validated(Login.class) User user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token); //异常抛到 {@code ExceptionHandleController 处理}

        User returnUser = userService.getUserByUsername(user.getUsername());
        returnUser.setPassword("");
        return JSONObject.success("login success", returnUser);
    }

    /**
     * 用户注销
     *
     * @return JSONObject
     */
    @RequestMapping("logout")
    public JSONObject logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() == null) {
            return JSONObject.successWithMessage("用户已经注销");
        }

        subject.logout();
        return JSONObject.successWithMessage("注销成功");
    }


    /**
     * 普通用户注册
     *
     * @param user
     * @return
     */
    @RequestMapping("register")
    public JSONObject register(@Validated(Register.class) User user) {
        user.setRole(1);//普通用户角色
        boolean result = userService.register(user);
        return result ? JSONObject.successWithMessage("注册成功") : JSONObject.error("注册失败", 1);
    }

    /**
     * @param query
     * @param type  1_username,2_email,3_phone
     * @return
     */
    @RequestMapping("checkUserExist")
    public JSONObject checkUserExist(String query, Integer type) {
        switch (type) {
            case 1:
                //根据用户名查询
                break;
            case 2:
                //根据邮箱查询


        }


        return null;
    }

    @RequestMapping("emailValidate")
    public JSONObject emailValidate(Integer userId, String code) {
        boolean result = userService.emailValidate(userId, code, cacheManager.getCache("COMMON_CACHE"));

        return result ? JSONObject.successWithMessage("邮箱验证成功") : JSONObject.error("邮箱验证失败", 1);
    }
}
