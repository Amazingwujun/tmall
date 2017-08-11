package com.tmall.service.impl;

import com.tmall.dao.DBDao.ProductDao;
import com.tmall.entity.vo.JSONObject;
import com.tmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Service("productService")
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductDao productDao;




}
