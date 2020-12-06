package com.example.mapper;

import com.example.entity.User;
import com.example.entity.UserInfo;
import com.example.wapper.UserWapper;
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
public interface UserMapper {

    /**
     * 根据用户名密码查用户信息
     * @param username
     * @param password
     * @return
     */
    User getByUsernamePassword(@Param("username") String username,
                                      @Param("password") String password);

    /**
     * 查用户列表
     * @return
     */
    List<UserWapper> getUserList(@Param("userId")Long userId);

    /**
     * 新增用户
     * @param user
     * @return
     */
    int saveUser(@Param("user")User user);

    /**
     * 根据用户id查用户信息
     * @param userId
     * @return
     */
    User getByUserId(@Param("userId") Long userId);

    /**
     * 更新用户
     * @param user
     * @return
     */
    int updateUser(@Param("user") User user);

    /**
     * 删除用户信息
     * @param userId
     * @return
     */
    int deleteByUserId(@Param("userId")Long userId);

    /**
     * 根据用户名判断用户是否存在
     * @param account
     * @return
     */
    User getByAccout(@Param("account")String account);

    /**
     * 根据token查用户
     * @param token
     * @return
     */
    User getTokenValue(@Param("token")String token);

    UserInfo getUserByUserName(@Param("username")String username);

    void addUser(@Param("userInfo")UserInfo userInfo);
}
