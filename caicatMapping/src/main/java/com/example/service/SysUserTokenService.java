package com.example.service;


import com.example.entity.SysUserTokenEntity;
import com.example.mapper.SysUserTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserTokenService {

    @Autowired
    private SysUserTokenMapper sysUserTokenMapper;


    public SysUserTokenEntity selectById(Long userId) {
       return sysUserTokenMapper.selectById(userId);
    }

    public SysUserTokenEntity selectBytoken(String token) {
        return sysUserTokenMapper.selectBytoken(token);
    }

    public void insert(SysUserTokenEntity tokenEntity) {
         sysUserTokenMapper.insert(tokenEntity);
    }

    public void updateById(SysUserTokenEntity tokenEntity) {
        sysUserTokenMapper.updateById(tokenEntity);
    }
}

