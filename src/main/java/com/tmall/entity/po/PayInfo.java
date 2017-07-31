package com.tmall.entity.po;

import java.util.Date;

public class PayInfo {
    private Integer id;

    private Integer userId;

    private String orderNo;

    private String platformStatus;

    private String platformOrderNo;

    private Integer payPlatform;

    private Date createTime;

    private Date updateTime;

    public PayInfo(Integer id, Integer userId, String orderNo, String platformStatus, String platformOrderNo, Integer payPlatform, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.orderNo = orderNo;
        this.platformStatus = platformStatus;
        this.platformOrderNo = platformOrderNo;
        this.payPlatform = payPlatform;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public PayInfo() {
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

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus == null ? null : platformStatus.trim();
    }

    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo == null ? null : platformOrderNo.trim();
    }

    public Integer getPayPlatform() {
        return payPlatform;
    }

    public void setPayPlatform(Integer payPlatform) {
        this.payPlatform = payPlatform;
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