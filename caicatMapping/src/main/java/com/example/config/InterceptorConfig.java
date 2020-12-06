package com.example.config;

import com.example.interceptor.impl.AuthInterceptor;
import com.example.interceptor.impl.SqlInjectInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author yp
 * @date 2019/7/4 15:10
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 拦截器是在spring 的配置文件加载之前执行的，
     * 若拦截器使用了dao或者service层，需要先注入到spring容器。
     *
     * @return
     */
    @Resource
    public AuthInterceptor authInterceptor;

    @Resource
    private SqlInjectInterceptor sqlInjectInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")//拦截所有接口请求
                .excludePathPatterns("/api/login")//除去 登录接口
                .excludePathPatterns("/api/verCode")//除去 验证码接口
                .excludePathPatterns("/api/downloadFileExcel")//除去下载
                .excludePathPatterns("/api/downTemplate")//除去下载
                .excludePathPatterns("/api/getUserManualDocument");//除去 下载用户手册接口
        registry.addInterceptor(sqlInjectInterceptor);
    }
}
