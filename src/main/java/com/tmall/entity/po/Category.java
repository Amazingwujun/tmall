package com.tmall.entity.po;

import com.tmall.common.validatorOrder.category.Add;
import com.tmall.common.validatorOrder.category.Update;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Category {
    //品类ID
    @NotNull(groups = {Update.class},message = "品类ID不能为空")
    private Integer id;

    //父品类
    private Integer parentId;

    //品类名称
    @NotBlank(groups = {Add.class,Update.class},message = "品类名不能为空")
    private String name;

    //品类状态
    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Category(Integer id, Integer parentId, String name, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Category() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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