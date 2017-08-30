package com.tmall.controller.portal;

import com.tmall.common.validatorOrder.cart.Add;
import com.tmall.common.validatorOrder.cart.Update;
import com.tmall.entity.po.Cart;
import com.tmall.entity.po.User;
import com.tmall.entity.vo.ReturnBean;
import com.tmall.service.ICartService;
import com.tmall.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购物车接口
 */

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    IUserService userService;

    @Autowired
    ICartService cartService;

    /**
     *
     *
     * @return
     */
    @RequestMapping("list")
    @RequiresAuthentication
    public ReturnBean list(){
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        Integer userId = userService.getUserByUsername(username).getId();

        return ReturnBean.success(null,cartService.listCart(userId));
    }

    /**
     * 新增购物车
     *
     * @param cart
     * @return
     */
    @RequestMapping("add")
    @RequiresAuthentication
    public ReturnBean add(@Validated(Add.class) Cart cart){
        //获取用户
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        Integer userId = userService.getUserByUsername(username).getId();

        cart.setUserId(userId);

        return cartService.addCart(cart);
    }

    @RequestMapping("update")
    @RequiresAuthentication
    public ReturnBean update(@Validated(Update.class) Cart cart) {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        Integer userId = userService.getUserByUsername(username).getId();

        cart.setUserId(userId);

        return cartService.updateCart(cart);
    }





}
