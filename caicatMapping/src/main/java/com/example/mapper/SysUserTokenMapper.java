package com.example.mapper;

import com.example.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserTokenMapper {

    SysUserTokenEntity selectById(Long userId);

    void insert(SysUserTokenEntity tokenEntity);

    void updateById(SysUserTokenEntity tokenEntity);

    SysUserTokenEntity selectBytoken(String token);
}
