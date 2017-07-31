package com.tmall.dao;

import com.tmall.entity.po.Product;
import com.tmall.entity.po.ProductWithBLOBs;

public interface ProductDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductWithBLOBs record);

    int insertSelective(ProductWithBLOBs record);

    ProductWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ProductWithBLOBs record);

    int updateByPrimaryKey(Product record);
}