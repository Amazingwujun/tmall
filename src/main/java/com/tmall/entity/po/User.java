package com.tmall.entity.po;

import com.tmall.common.validatorOrder.Login;
import com.tmall.common.validatorOrder.Register;
import com.tmall.common.validatorOrder.ResetPassword;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.Date;

public class User {

    private Integer id;

    @NotBlank(message = "用户名不能为空", groups = {Login.class, Register.class, ResetPassword.class})
    private String username;

    @NotBlank(message = "密码不能为空", groups = {Login.class, Register.class, ResetPassword.class})
    @Length(message = "密码最少六位", min = 6, groups = {Login.class, Register.class, ResetPassword.class})
    private String password;

    @NotBlank(message = "邮箱不能为空", groups = {Register.class})
    @Pattern(message = "邮箱格式错误", regexp = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}",
            groups = {Register.class})
    private String email;

    @NotBlank(message = "手机号码不能为空", groups = {Register.class})
    @Pattern(message = "手机号码格式错误", regexp = "(13\\d|14[57]|15[^4,\\D]|17[13678]|18\\d)\\d{8}|170[0589]\\d{7}",
            groups = {Register.class})
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", validate=" + validate +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}