package com.tmall.controller;


import com.tmall.entity.vo.JSONObject;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Controller 异常汇总处理
 */
@RestController
@ControllerAdvice
public class ExceptionHandleController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandleController.class);

    /**
     * 登录异常
     *
     * @param request
     * @param response
     * @return JSONObject
     */
    @ExceptionHandler(AuthenticationException.class)
    public JSONObject authenticationExceptionHandel(AuthenticationException e, HttpServletRequest request,
                                                    HttpServletResponse response) {
        log.debug(e.getMessage(), e);

        String msg;
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
        } else {
            msg = "未知异常,登录失败";
        }

        return JSONObject.error(msg, 1);
    }

    /**
     * 未登录异常
     *
     * @param e
     * @return JSONObject
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public JSONObject unAuthenticationExceptionHandle(UnauthenticatedException e) {
        log.debug(e.getMessage(), e);

        return JSONObject.error(e.getMessage(), 1);
    }

    /**
     * 数据校检异常
     *
     * @param e
     * @return JSONObject
     */
    @ExceptionHandler(BindException.class)
    public JSONObject validExceptionHandle(BindException e) {
        log.debug(e.getMessage(), e);

        String message = getMessage(e);

        return JSONObject.error(message, 1);
    }

    /**
     * 数据校检异常,当参数含有注解{@code @RequestBody}时,
     * {@link org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor} 抛出的异常
     * {@link MethodArgumentNotValidException}
     *
     * @param e
     * @return JSONObject
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JSONObject methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        log.debug(e.getMessage(), e);

        String message = getMessage(e);

        return JSONObject.error(message, 1);
    }

    /**
     * @param e
     * @return
     */
    private String getMessage(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;

            String message = "";
            List<FieldError> fieldErrors = validException.getBindingResult().getFieldErrors();
            for (FieldError error : fieldErrors) {
                log.error(error.getField() + ":" + error.getDefaultMessage());
                message = error.getDefaultMessage();
            }
            return message;
        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;

            String message = "";
            List<FieldError> fieldErrors = bindException.getBindingResult().getFieldErrors();
            for (FieldError error : fieldErrors) {
                log.error(error.getField() + ":" + error.getDefaultMessage());
                message = error.getDefaultMessage();
            }
            return message;
        } else {
            log.error(e.getMessage(), e);
            return "异常类型无法捕获";
        }
    }
}
