package com.tmall.controller.portal;

import com.tmall.common.validatorOrder.Login;
import com.tmall.common.validatorOrder.Register;
import com.tmall.common.validatorOrder.ResetPassword;
import com.tmall.entity.po.User;
import com.tmall.entity.vo.JSONObject;
import com.tmall.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        token.setRememberMe(true); //记住登录状态
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            return JSONObject.successWithMessage("用户已登录");
        }

        subject.login(token);

        User returnUser = userService.getUserByUsername(user.getUsername());
        if (returnUser == null) {
            throw new UnknownAccountException();    //抛给ExceptionHandleController处理
        }

        returnUser.setPassword("");
        returnUser.setPhone(phoneDigest(returnUser.getPhone()));
        return JSONObject.success("login success", returnUser);
    }

    /**
     * 遮掩用户电话号码
     *
     * @param phone
     * @return
     */
    private String phoneDigest(String phone) {
        Assert.hasText(phone, "电话号码不能为空");

        StringBuilder builder = new StringBuilder();
        builder.append(phone.substring(0, 2)).append("*******").append(phone.charAt(phone.length() - 1));
        return builder.toString();
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
        user.setRole(2);//普通用户角色
        boolean result = userService.register(user);
        return result ? JSONObject.successWithMessage("注册成功") : JSONObject.error("注册失败", 1);
    }

    /**
     * 查询特定字段是否已被使用
     *
     * @param query
     * @param type  1_username,2_email,3_phone
     * @return
     */
    @RequestMapping("checkUserExist")
    public JSONObject checkUserExist(String query, Integer type) {
        if (StringUtils.isEmpty(query) || type == null) {
            return JSONObject.error("参数异常", 1);
        }

        boolean userExist = userService.userExist(query, type);

        return userExist ? JSONObject.successWithMessage("用户已经存在") : JSONObject.error("用户不存在", 1);
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    @RequestMapping("getUserInfo")
    public JSONObject getUserInfo() {
        Subject subject = SecurityUtils.getSubject();

        String username = (String) subject.getPrincipal();

        if (username == null) {
            return JSONObject.successWithMessage("用户未登录,无法获取用户信息");
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UnknownAccountException(); //抛给ExceptionHandleController处理
        }

        user.setPassword("");
        user.setPhone(phoneDigest(user.getPhone()));
        return JSONObject.success(null, user);
    }

    /**
     * 验证用户邮箱
     *
     * @param username
     * @param token
     * @return
     */
    @RequestMapping("emailValidate")
    public JSONObject emailValidate(String username, String token) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) {
            return JSONObject.error("参数异常", 1);
        }
        boolean result = userService.emailValidate(username, token, cacheManager.getCache("COMMON_CACHE"));

        return result ? JSONObject.successWithMessage("邮箱验证成功") : JSONObject.error("邮箱验证失败", 1);
    }

    /**
     * 忘记密码,发送重置密码邮件.
     *
     * @param key  用户名或邮箱
     * @param type 1_用户名,2_邮箱
     * @return
     */
    @RequestMapping("forgetPassword")
    public JSONObject forgetPassword(String key, Integer type) {
        if (StringUtils.isEmpty(key) || type == null) {
            return JSONObject.error("参数异常", 1);
        }

        //判断用户提供之参数是否存在
        boolean userExist = userService.userExist(key, type);
        if (!userExist) {
            return JSONObject.error("用户名或邮箱不存在", 1);
        }

        boolean result = userService.forgetPassword(key, type);
        return result ? JSONObject.successWithMessage("邮件发送成功") : JSONObject.error("邮件发送失败", 1);
    }

    /**
     * 重置密码
     *
     * @param user  接收username,password这两个参数
     * @param token 重置密码必须的令牌
     * @return
     */
    @RequestMapping("resetPassword")
    public JSONObject resetPassword(@Validated(ResetPassword.class) User user, String token) {
        if (StringUtils.isEmpty(token)) return JSONObject.error("token为空,无法重置密码", 1);

        boolean result = userService.resetPassword(user.getUsername(), user.getPassword(), token);

        return result ? JSONObject.successWithMessage("密码重置成功") : JSONObject.error("密码重置失败", 1);
    }
}
