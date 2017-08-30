package com.tmall.service;

import com.github.pagehelper.PageInfo;
import com.tmall.entity.vo.ProductDetailVO;

import java.util.List;

public interface IProductService {

    /**
     *
     * @param productId
     * @return
     */
    ProductDetailVO getProductDetail(Integer productId);

    /**
     *
     * @param keywords
     * @param orderBy
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo listByKeywords(String keywords, String orderBy, Integer pageNum, Integer pageSize);

    /**
     *
     * @param categoryId
     * @param orderBy
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo listByCategoryId(Integer categoryId, String orderBy, Integer pageNum, Integer pageSize);
}
