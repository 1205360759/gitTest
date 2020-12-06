
package com.example.mapper;

import com.example.entity.VerCodeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 验证码
 */
@Mapper
public interface VerCodeMapper {

    int insertSelective(VerCodeEntity verCodeEntity);

    VerCodeEntity selectByPrimaryKey(String uuid);

    boolean deleteBatch(String[] uuid);

}
