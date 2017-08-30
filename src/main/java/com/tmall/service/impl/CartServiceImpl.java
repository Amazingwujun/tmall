package com.tmall.service.impl;

import com.tmall.common.constant.Ecart;
import com.tmall.dao.DBDao.CartDao;
import com.tmall.dao.DBDao.ProductDao;
import com.tmall.entity.po.Cart;
import com.tmall.entity.po.Product;
import com.tmall.entity.vo.CartVO;
import com.tmall.entity.vo.ReturnBean;
import com.tmall.service.ICartService;
import com.tmall.utils.BigDecimalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 购物车服务
 */
@Service("cartService")
public class CartServiceImpl implements ICartService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    CartDao cartDao;

    @Autowired
    ProductDao productDao;

    /**
     * 根据用户ID获取购物车视图列表
     *
     * @param userId 用户ID
     * @return
     */
    public List<CartVO> listCart(Integer userId) {
        Assert.notNull(userId, "参数不能为null");

        return cartDao.selectCartVoListByUserId(userId);
    }


    /**
     * 新增购物车
     *
     * @param cart 新增的购物车
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ReturnBean addCart(Cart cart) {
        Assert.notNull(cart, "购物车对象不能为空");

        // 获取购物车对应产品
        Integer productId = cart.getProductId();
        Product product = productDao.selectByIdWithOnSale(productId);
        if (product == null) {
            log.debug("产品(ID:{})不在售或不存在", productId);
            return ReturnBean.error("产品不在售或不存在", 1);
        }
        Integer stock = product.getStock();
        Integer buyCount = cart.getQuantity();

        cart.setProductName(product.getName());
        cart.setMainImage(product.getMainImage());

        // 新增产品默认为勾选状态
        cart.setChecked(Ecart.CHECKED.getKey());
        cart.setPrice(product.getPrice());
        cart.setTotalPrice(BigDecimalUtils.mul(product.getPrice(), cart.getQuantity()));

        if (buyCount > product.getStock()) {
            return ReturnBean.error("产品库存不足", 1);
        }

        // 获取用户所有的购物车
        List<CartVO> cartVOList = listCart(cart.getUserId());
        for (CartVO cartVO : cartVOList) {

            if (cartVO.getProductId().equals(cart.getProductId())) { // 购物车已经存在

                int total = cartVO.getQuantity() + cart.getQuantity();

                cart.setId(cartVO.getId());
                cart.setQuantity(total);
                cart.setTotalPrice(BigDecimalUtils.mul(product.getPrice(), total));
                cart.setChecked(Ecart.CHECKED.getKey());

                int result = cartDao.updateByPrimaryKeySelective(cart);
                if (result == 0) {
                    return ReturnBean.error("购物车添加失败", 1);
                }
                product.setStock(stock - buyCount);
                productDao.updateByPrimaryKeySelective(product); // 更新产品库存

                return ReturnBean.success("添加成功", listCart(cart.getUserId()));
            }
        }

        int result = cartDao.insertSelective(cart);
        if (result == 0) {
            return ReturnBean.error("购物车添加失败", 1);
        }

        product.setStock(stock - buyCount);
        productDao.updateByPrimaryKeySelective(product); // 更新产品库存

        return ReturnBean.success("添加成功", listCart(cart.getUserId()));
    }

    /**
     * 更新购物车
     *
     * @param cart
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public ReturnBean updateCart(Cart cart) {
        Assert.notNull(cart, "参数不能为null");

        int result;

        if (cart.getQuantity() != null) {
            // 需要更新购物车中产品的数量
            Product product = productDao.selectByPrimaryKey(cart.getProductId());
            Integer stock = product.getStock(); // 产品库存

            // 旧购物车中的库存先返还到库存
            Cart oldCart = cartDao.selectByPrimaryKey(cart.getId());
            if (oldCart == null) {
                return ReturnBean.error("购物车不存在,无法更新", 1);
            }
            stock += oldCart.getQuantity();

            if (stock < cart.getQuantity()) {
                return ReturnBean.error("购买数量不能超过库存", 1);
            }

            product.setStock(stock - cart.getQuantity());
            cart.setTotalPrice(BigDecimalUtils.mul(oldCart.getPrice(),cart.getQuantity()));

            result = cartDao.updateByPrimaryKeySelective(cart);
            productDao.updateByPrimaryKeySelective(product);
        } else {
            result = cartDao.updateByPrimaryKeySelective(cart);
        }

        return result > 0 ? ReturnBean.success("更新成功", listCart(cart.getUserId())) :
                ReturnBean.error("更新失败", 1);
    }
}
