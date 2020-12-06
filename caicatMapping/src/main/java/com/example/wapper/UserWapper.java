package com.example.wapper;

import java.util.Date;

/**
 * @Title:
 * @Description:
 * @Auther: YuPing
 * @Date: 2019/5/28 10:22
 */
//用户实体的包装类
public class UserWapper {

    private Long userId;
    private String account;
    private String name;
    private String roleId;
    private String status;
    private Date createTime;
    private Long creatUser;
    private Date updateTime;
    private Long updateUser;
    private String description;

    public UserWapper() { }


    public UserWapper(Long userId,String roleId, String account, String name, String status, Date createTime, Long creatUser, Date updateTime, Long updateUser, String description) {
        this.userId = userId;
        this.roleId = roleId;
        this.account = account;
        this.name = name;
        this.status = status;
        this.createTime = createTime;
        this.creatUser = creatUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.description = description;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getCreatUser() {
        return creatUser;
    }

    public void setCreatUser(Long creatUser) {
        this.creatUser = creatUser;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
