package com.tmall.entity.po;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

public class User {

    private Integer id;

    @NotEmpty
    @Length(min = 6)
    private String username;

    @NotEmpty
    @Length(min = 6)
    private String password;

    @NotEmpty
    @Email
    private String email;

    private String phone;

    private Integer role;

    private Boolean validate;

    private Date createTime;

    private Date updateTime;

    public User(Integer id, String username, String password, String email, String phone, Integer role, Boolean validate, Date createTime, Date updateTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.validate = validate;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
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