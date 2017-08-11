package com.tmall.controller.portal;


import com.tmall.entity.vo.JSONObject;
import com.tmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RequestMapping("product")
@RestController
public class ProductController {

    @Autowired
    private IProductService productService;


    @RequestMapping("{productId}/detail")
    public JSONObject detail(@PathVariable("productId") Integer productId) {
        System.out.println(productId);










        return null;
    }






}
