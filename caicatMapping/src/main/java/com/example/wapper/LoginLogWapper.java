package com.example.wapper;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 登录记录
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public class LoginLogWapper  {

    /**
     * 主键
     */
    private Long loginLogId;
    /**
     * 日志名称
     */
    private String logName;
    /**
     * 管理员id
     */
    private Long userId;
    /**
     * 创建时间
     */
//    private Date createTime;
    private String createTime;
    /**
     * 是否执行成功
     */
    private String succeed;
    /**
     * 具体消息
     */
    private String message;
    /**
     * 登录ip
     */
    private String ipAddress;

    private String loginTime;

    private String exitTime;

    private String userName;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LoginLogWapper(Long loginLogId, String logName, Long userId, String createTime, String succeed, String message, String ipAddress, String loginTime, String exitTime, String userName,int status) {
        this.loginLogId = loginLogId;
        this.logName = logName;
        this.userId = userId;
        this.createTime = createTime;
        this.succeed = succeed;
        this.message = message;
        this.ipAddress = ipAddress;
        this.loginTime = loginTime;
        this.exitTime = exitTime;
        this.userName = userName;
        this.status = status;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getLoginLogId() {
        return loginLogId;
    }

    public void setLoginLogId(Long loginLogId) {
        this.loginLogId = loginLogId;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSucceed() {
        return succeed;
    }

    public void setSucceed(String succeed) {
        this.succeed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
                ", loginLogId=" + loginLogId +
                ", logName=" + logName +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", succeed=" + succeed +
                ", message=" + message +
                ", ipAddress=" + ipAddress +
                "}";
    }
}
