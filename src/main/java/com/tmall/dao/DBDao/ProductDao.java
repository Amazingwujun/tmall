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

    /**
     * 根据关键字进行模糊查询
     *
     * @param keywords
     * @return
     */
    List<Product> listByKeywords(@Param("keywords") String keywords);

    /**
     * 根据产品所属分类查询
     *
     * @param categoryId
     * @return
     */
    List<Product> listByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 获取在售的产品
     *
     * @param productId
     * @return
     */
    Product selectByIdWithOnSale(@Param("productId") Integer productId);
}