package com.tmall.service;

import com.tmall.entity.po.Cart;
import com.tmall.entity.vo.CartVO;
import com.tmall.entity.vo.ReturnBean;

import java.util.List;

public interface ICartService {

    /**
     * 根据用户ID获取购物车视图列表
     *
     * @param userId 用户ID
     * @return
     */
    List<CartVO> listCart(Integer userId);

    /**
     * 向购物车中添加商品
     *
     * @param cart 购物车对象
     * @return
     */
    ReturnBean addCart(Cart cart);

    /**
     * 更新购物车
     *
     * @param cart
     * @return
     */
    ReturnBean updateCart(Cart cart);
}
