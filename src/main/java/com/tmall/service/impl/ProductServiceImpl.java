package com.tmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tmall.dao.DBDao.ProductDao;
import com.tmall.entity.po.Product;
import com.tmall.entity.vo.ProductDetailVO;
import com.tmall.entity.vo.ProductListVO;
import com.tmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service("productService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductDao productDao;

    /**
     *
     *
     * @param productId
     * @return
     */
    @Override
    public ProductDetailVO getProductDetail(Integer productId) {
        Assert.notNull(productId, "产品查询参数不能为空");

        ProductDetailVO productDetailVO = assembleProductVO(productDao.selectByPrimaryKey(productId));

        return productDetailVO;
    }

    /**
     * 根据关键字获取产品列表
     *
     * @param keywords
     * @param orderBy   "price_desc" 约定参数格式,"price desc"实际需要的格式
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo listByKeywords(String keywords, String orderBy, Integer pageNum, Integer pageSize) {
        Assert.hasText(keywords,"查询关键字不能为空");
        //开始分页
        PageHelper.startPage(pageNum, pageSize);

        //排序
        if (StringUtils.hasText(orderBy)) {
            int index = orderBy.indexOf("_");
            orderBy = orderBy.substring(0, index) +" "+ orderBy.substring(index + 1);
            PageHelper.orderBy(orderBy);
        }
        //启动模糊查询 select * from table where field like %keywords%
        keywords = new StringBuilder().append("%").append(keywords).append("%").toString();

        List<Product> products = productDao.listByKeywords(keywords);

        PageInfo<Product> pageInfo = new PageInfo<>(products);

        List productListVO = assembleProductListVO(products);
        pageInfo.setList(productListVO);

        return pageInfo;
    }

    /**
     * 根据分类ID获取产品列表
     *
     * @param categoryId
     * @param orderBy
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo listByCategoryId(Integer categoryId, String orderBy, Integer pageNum, Integer pageSize) {
        Assert.notNull(categoryId,"分类ID不能为空");
        //开始分页
        PageHelper.startPage(pageNum, pageSize);

        //排序
        if (StringUtils.hasText(orderBy)) {
            int index = orderBy.indexOf("_");
            orderBy = orderBy.substring(0, index) +" "+ orderBy.substring(index + 1);
            PageHelper.orderBy(orderBy);
        }

        List<Product> products = productDao.listByCategoryId(categoryId);

        PageInfo<Product> pageInfo = new PageInfo<>(products);

        List productListVO = assembleProductListVO(products);
        pageInfo.setList(productListVO);

        return pageInfo;
    }

    /**
     * ProductVO装配
     *
     * @param product
     * @return
     */
    private ProductDetailVO assembleProductVO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setName(product.getName());
        productDetailVO.setSubTitle(product.getSubTitle());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setSubImage(product.getSubImage());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setImageHost(null);// TODO: 2017/8/14 0014 图片服务器暂不设置
        productDetailVO.setParentCategoryId(null); // TODO: 2017/8/14 0014 父分类ID暂不设置
        return productDetailVO;
    }

    /**
     * ProductListVO装配
     *
     * @param products
     * @return
     */
    private List assembleProductListVO(List<Product> products) {
        if (CollectionUtils.isEmpty(products)) {
            return new ArrayList();
        }

        List<ProductListVO> list = new ArrayList();

        for (Product product : products) {
            ProductListVO productListVO = new ProductListVO();

            productListVO.setName(product.getName());
            productListVO.setCategoryId(product.getCategoryId());
            productListVO.setImageHost(null);// TODO: 2017/8/14 0014 图片服务器地址后期统一设定
            productListVO.setPrice(product.getPrice());
            productListVO.setMainImage(product.getMainImage());
            productListVO.setSubTitle(product.getSubTitle());
            list.add(productListVO);
        }

        return list;
    }
}
