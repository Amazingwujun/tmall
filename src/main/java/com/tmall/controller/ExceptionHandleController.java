package com.tmall.controller;


import com.tmall.common.JSONObject;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandleController.class);

    /**
     * 登录异常
     *
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public JSONObject authenticationExceptionHandel(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) {
        logger.info(e.getMessage());
        return JSONObject.error(e.getMessage(), 1);
    }

    /**
     * 未登录异常
     *
     * @return
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public JSONObject unAuthenticationExceptionHandle(UnauthenticatedException e) {
        logger.info(e.getMessage());

        return JSONObject.error(e.getMessage(), 1);
    }

    /**
     * 数据校检异常
     *
     * @return
     */
    @ExceptionHandler(BindException.class)
    public JSONObject validExcetpionHandle(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            logger.error(error.getField() + ":" + error.getDefaultMessage());
        }

        return JSONObject.error(e.getMessage(), 1);
    }
}
