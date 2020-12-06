package com.example.service;


import com.example.entity.User;
import com.example.entity.UserInfo;
import com.example.mapper.UserMapper;
import com.example.wapper.UserWapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getByUsernamePassword(String username,String password){
        User user = userMapper.getByUsernamePassword(username,password);
        return user;
    }

    public List<UserWapper> getUserList(Long userId){
        List<UserWapper> userList = userMapper.getUserList(userId);
        return userList;
    }

    public int saveUser(User user){
        int result = userMapper.saveUser(user);
        return result;
    }


    public User getByUserId(Long userId){
        User user = userMapper.getByUserId(userId);
        return user;
    }

    public int updateUser(User user){
        int index = userMapper.updateUser(user);
        return index;
    }

    public int deleteByUserId(Long userId){
        int index = userMapper.deleteByUserId(userId);
        return index;
    }

    public User getByAccout(String account){
        return userMapper.getByAccout(account);
    }

    public User getTokenValue(String token){
        return userMapper.getTokenValue(token);
    }

    public UserInfo getUserByUserName(String username) {
        UserInfo userInfo = userMapper.getUserByUserName(username);
        return userInfo;
    }

    public void addUser(UserInfo userInfo) {
        userMapper.addUser(userInfo);
    }
}
