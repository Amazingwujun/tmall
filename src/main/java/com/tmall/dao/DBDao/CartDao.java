package com.tmall.dao.DBDao;

import com.tmall.entity.po.Cart;
import com.tmall.entity.vo.CartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /**
     * 根据用户ID获取购物车列表
     *
     * @param userId 用户ID
     * @return
     */
    List<CartVO> selectCartVoListByUserId(@Param("userId") Integer userId);
}