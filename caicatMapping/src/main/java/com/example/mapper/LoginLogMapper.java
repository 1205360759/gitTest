package com.example.mapper;

import com.example.entity.LoginLog;
import com.example.wapper.LoginLogWapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title:
 * @Description:
 * @Auther: YuPing
 * @Date: 2018/7/18 19:17
 */
@Mapper
public interface LoginLogMapper {
    /**
     * 添加登录日志
     * @param loginLog
     * @return
     */
    int saveLoginLog(@Param("loginLog")LoginLog loginLog);

    /**
     * 登录日志列表接口
     * @return
     */
    List<LoginLogWapper> getLoginLogList();

    /**
     * 删除操作日志记录
     * @param id
     * @return
     */
    int deleteByPrimaryKey(@Param("loginLogId")Long id);
}