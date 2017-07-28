package com.tmall.controller;


import com.tmall.common.JSONObject;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 登录异常
     *
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({AuthenticationException.class})
    public JSONObject authenticationException(AuthenticationException e,HttpServletRequest request, HttpServletResponse response) {
        logger.info(e.getMessage());
        return JSONObject.error(e.getMessage(),1);
    }

    /**
     * 未登录异常
     *
     * @return
     */
    @ExceptionHandler({UnauthenticatedException.class})
    public JSONObject unAuthenticationException(UnauthenticatedException e) {
        logger.info(e.getMessage());

        return JSONObject.error(e.getMessage(),1);
    }


}
