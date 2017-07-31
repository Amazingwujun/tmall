package com.tmall.security;

import com.tmall.entity.po.User;
import com.tmall.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class UserRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    IUserService userService;

    /**
     * 权限认证
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principals);

        Set<String> roleNames = userService.getRoleNamesByUsername(username);
        Set<String> permissions = userService.getPermissionsByUserName(username);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 身份认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken loginToken = (UsernamePasswordToken) token;
        String username = loginToken.getUsername();

        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        User user = userService.getUserByUsername(username);
        String password = user.getPassword();

        if (password == null) {
            throw new UnknownAccountException("未找到用户 [" + username + "]");
        }

        return new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
    }
}
