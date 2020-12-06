package com.example.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 * 生成验证码配置
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        //边框
        properties.put("kaptcha.border", "no");
        //字体颜色
        properties.put("kaptcha.textproducer.font.color", "black");
        //间距
        properties.put("kaptcha.textproducer.char.space", "6");
        //长度
        properties.put("kaptcha.textproducer.char.length", "5");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
