package com.example.wapper;

/**
 * @Title:
 * @Description:
 * @Auther: YuPing
 * @Date: 2019/6/4 14:09
 */

/**
 * 前端没有表单提交，封装一个类用来接收前端传过来的对象
 */
public class UserParamWapper {

    private String account;
    private String password;
    private String roleId;
    private String name;
    private String phone;
    private String email;
    private String description;
    private String userId;

    public UserParamWapper() {
    }

    public UserParamWapper(String account, String password, String roleId, String name, String phone, String email, String description, String userId) {
        this.account = account;
        this.password = password;
        this.roleId = roleId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.userId = userId;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
