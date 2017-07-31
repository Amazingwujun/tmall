package com.tmall.entity.po;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private Integer id;

    private Integer userId;

    private String orderNo;

    private BigDecimal totalPrice;

    private BigDecimal postPrice;

    private Integer payType;

    private Integer shippingId;

    private Integer status;

    private Date startTime;

    private Date payTime;

    private Date closeTime;

    private Date postTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    public Order(Integer id, Integer userId, String orderNo, BigDecimal totalPrice, BigDecimal postPrice, Integer payType, Integer shippingId, Integer status, Date startTime, Date payTime, Date closeTime, Date postTime, Date endTime, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.orderNo = orderNo;
        this.totalPrice = totalPrice;
        this.postPrice = postPrice;
        this.payType = payType;
        this.shippingId = shippingId;
        this.status = status;
        this.startTime = startTime;
        this.payTime = payTime;
        this.closeTime = closeTime;
        this.postTime = postTime;
        this.endTime = endTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Order() {
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getPostPrice() {
        return postPrice;
    }

    public void setPostPrice(BigDecimal postPrice) {
        this.postPrice = postPrice;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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