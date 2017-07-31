package com.tmall.dao;

import com.tmall.entity.po.orderItem;

public interface OrderItemDao {
    int deleteByPrimaryKey(Integer id);

    int insert(orderItem record);

    int insertSelective(orderItem record);

    orderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(orderItem record);

    int updateByPrimaryKey(orderItem record);
}