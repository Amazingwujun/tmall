package com.tmall.entity.po;

import java.math.BigDecimal;
import java.util.Date;

public class Cart {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private String productName;

    private BigDecimal price;

    private Integer quantity;

    private String mainImage;

    private BigDecimal totalPrice;

    private Integer checked;

    private Date createTime;

    private Date updateTime;

    public Cart(Integer id, Integer userId, Integer productId, String productName, BigDecimal price, Integer quantity, String mainImage, BigDecimal totalPrice, Integer checked, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.mainImage = mainImage;
        this.totalPrice = totalPrice;
        this.checked = checked;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Cart() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage == null ? null : mainImage.trim();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}