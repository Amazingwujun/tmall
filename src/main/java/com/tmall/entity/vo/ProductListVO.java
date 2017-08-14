package com.tmall.entity.vo;

import java.math.BigDecimal;

public class ProductListVO {

    //所属品类ID
    private Integer categoryId;

    //产品名称
    private String name;

    //产品子标题
    private String subTitle;

    //产品主图
    private String mainImage;

    //产品价格
    private BigDecimal price;

    //图片服务器地址
    private String imageHost;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    @Override
    public String toString() {
        return "ProductListVO{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", price=" + price +
                ", imageHost='" + imageHost + '\'' +
                '}';
    }
}
