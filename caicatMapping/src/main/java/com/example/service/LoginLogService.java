package com.example.service;


import com.example.common.LocalAddress;
import com.example.entity.LoginLog;
import com.example.mapper.LoginLogMapper;
import com.example.wapper.LoginLogWapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;

@Service
public class LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;


    /**
     * 添加登录日志
     * @param userId
     * @param loginTime
     * @param exitTime
     * @param logName
     * @param message
     * @param succeed
     * @param userName
     * @return
     */
    public int saveLoginLog(Long userId,Date loginTime,Date exitTime, String logName,
                            String message, String succeed, String userName){
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);//登录用户id
        InetAddress netAddress = LocalAddress.getInetAddress();
        loginLog.setIpAddress(LocalAddress.getHostIp(netAddress)); //用户IP
        loginLog.setCreateTime(new Date());//日志创建时间
        loginLog.setLoginTime(loginTime);//登录时间
        loginLog.setExitTime(exitTime);//退出时间
        loginLog.setLogName(logName);//日志名称
        loginLog.setMessage(message);//日志详情
        loginLog.setSucceed(succeed);//是否成功
        loginLog.setUserName(userName);//用户名称
        loginLog.setStatus(1);//默认状态存在
        int index = loginLogMapper.saveLoginLog(loginLog);
        return index;
    }

    /**
     * 登录日志接口
     * @return
     */
    public List<LoginLogWapper> getLoginLogList(){
        List<LoginLogWapper> loginLogWappers = loginLogMapper.getLoginLogList();
        return loginLogWappers;
    }

    /**
     * 删除操作日志
     * @param logId
     * @return
     */
    public int deleteByPrimaryKey(Long logId){
        int index = loginLogMapper.deleteByPrimaryKey(logId);
        return  index;
    }

}
