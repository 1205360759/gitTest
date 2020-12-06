package com.example.service;

import com.example.common.RRException;
import com.example.entity.VerCodeEntity;
import com.example.mapper.VerCodeMapper;
import com.example.utils.DateUtils;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * 验证码
 */
@Service
public class VerCodeService {
    @Autowired
    private Producer producer;

    @Autowired
    private VerCodeMapper verCodeMapper;

    /**
     * 获取图片验证码
     */
    public BufferedImage getCaptcha(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new RRException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();
        VerCodeEntity captchaEntity = new VerCodeEntity();
        captchaEntity.setUuid(uuid);
        captchaEntity.setCode(code);
        //5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        verCodeMapper.insertSelective(captchaEntity);
        return producer.createImage(code);
    }

    /**
     * 验证码效验
     *
     * @param uuid uuid
     * @param code 验证码
     * @return true：成功  false：失败
     */
    public boolean validate(String uuid, String code) {
        VerCodeEntity captchaEntity = verCodeMapper.selectByPrimaryKey(uuid);
        //VerCodeEntity captchaEntity = this.selectOne(new EntityWrapper<VerCodeEntity>().eq("uuid", uuid));
        if (captchaEntity == null) {
            return false;
        }
        //长度为1的数组,批量删除
        String[] arr = new String[1];
        arr[0]=uuid;
        //删除验证码
        verCodeMapper.deleteBatch(arr);
        // this.deleteById(uuid);
        //验证码相等且库里面的时间大于当前时间（5分钟之内）
        if (captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis()) {
            return true;
        }
        return false;
    }
}
