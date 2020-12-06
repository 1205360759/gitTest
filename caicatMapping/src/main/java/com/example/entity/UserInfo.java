package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long userId;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * md5密码盐
     */
    private String salt;
    /**
     * 名字
     */
    private String name;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 性别(字典)
     */
    private String sex;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 电话
     */
    private String phone;
    /**
     * 角色id(多个逗号隔开)
     */
    private String roleId;
    /**
     * 部门id(多个逗号隔开)
     */
    private Long deptId;
    /**
     * 状态(字典)
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private Long updateUser;
    /**
     * 乐观锁
     */
    private Integer version;

    private String description;

    private String accessToken;

    private Date LastLoginTime;

    //最后一次登陆错误时间
    private Date LastLoginErrorTime;

    //登陆错误计数
    private Integer LoginErrorCount;

    //是否锁定（0、未锁定；1、锁定）
    private Integer IsLocked;

    public Date getLastLoginErrorTime() {
        return LastLoginErrorTime;
    }

    public void setLastLoginErrorTime(Date lastLoginErrorTime) {
        LastLoginErrorTime = lastLoginErrorTime;
    }

    public Integer getLoginErrorCount() {
        return LoginErrorCount;
    }

    public void setLoginErrorCount(Integer loginErrorCount) {
        LoginErrorCount = loginErrorCount;
    }

    public Integer getIsLocked() {
        return IsLocked;
    }

    public void setIsLocked(Integer isLocked) {
        IsLocked = isLocked;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getLastLoginTime() {
        return LastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        LastLoginTime = lastLoginTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
