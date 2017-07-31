package com.tmall.controller;


import com.tmall.common.JSONObject;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@ControllerAdvice
public class ExceptionHandleController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandleController.class);

    /**
     * 登录异常
     *
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public JSONObject authenticationExceptionHandel(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) {
        log.info(e.getMessage(),e);

        String msg = "异常未捕获";
        if (e instanceof IncorrectCredentialsException) {
            msg = "登录密码错误";
        } else if (e instanceof ExcessiveAttemptsException) {
            msg = "登录失败次数过多,五分钟后再试";
        } else if (e instanceof LockedAccountException) {
            msg = "帐号已被锁定.";
        } else if (e instanceof DisabledAccountException) {
            msg = "帐号已被禁用.";
        } else if (e instanceof ExpiredCredentialsException) {
            msg = "帐号已经过期";
        } else if (e instanceof UnknownAccountException) {
            msg = "帐号不存在";
        } else if (e instanceof AuthenticationException) {
            msg = "未知异常,登录失败";
        }

        return JSONObject.error(msg, 1);
    }

    /**
     * 未登录异常
     *
     * @return
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public JSONObject unAuthenticationExceptionHandle(UnauthenticatedException e) {
        log.info(e.getMessage(),e);

        return JSONObject.error(e.getMessage(), 1);
    }

    /**
     * 数据校检异常
     *
     * @return
     */
    @ExceptionHandler(BindException.class)
    public JSONObject validExcetpionHandle(BindException e) {
        log.info(e.getMessage(),e);

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            log.error(error.getField() + ":" + error.getDefaultMessage());
        }

        return JSONObject.error("参数绑定异常", 1);
    }
}
