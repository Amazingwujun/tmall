package com.tmall.entity.po;

import java.math.BigDecimal;
import java.util.Date;

public class ProductWithBLOBs extends Product {
    private String detail;

    private String subImage;

    public ProductWithBLOBs(Integer id, Integer categoryId, String name, String subTitle, String mainImage, BigDecimal price, Integer stock, Integer status, Date createTime, Date updateTime, String detail, String subImage) {
        super(id, categoryId, name, subTitle, mainImage, price, stock, status, createTime, updateTime);
        this.detail = detail;
        this.subImage = subImage;
    }

    public ProductWithBLOBs() {
        super();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage == null ? null : subImage.trim();
    }
}