package com.tmall.dao.DBDao;

import com.tmall.entity.po.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {

    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> listByKeywords(@Param("keywords") String keywords);
}