package com.tmall.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class LoginToken extends UsernamePasswordToken{

    /**
     * 登录类型,1_username,2_email,3_phone
     *
     */
    private Integer loginType;

    public LoginToken(String principle, String password, Integer loginType) {
        super(principle, password);
        this.loginType = loginType;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

}
