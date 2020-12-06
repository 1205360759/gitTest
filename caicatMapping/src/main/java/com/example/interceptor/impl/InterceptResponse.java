package com.example.interceptor.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.utils.MD5Util;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取responseBody返回值
 * @author PingYu
 * @date 2020/4/13 17:59
 */
@ControllerAdvice
public class InterceptResponse implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    /**
     * 拦截返回值,并且操作返回值,这是一个全局过滤
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
        ServletServerHttpResponse servletServerHttpResponse = (ServletServerHttpResponse) response;
        HttpServletResponse httpServletResponse = servletServerHttpResponse.getServletResponse();
        httpServletRequest.setAttribute("responseBody",body);
        Object result = httpServletRequest.getAttribute("responseBody");//获取返回结果
        String jsonResult = "";//转json
        if(result != null) {
            jsonResult = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
        }
        //只保留中英文数字
        jsonResult = jsonResult.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]","");
        int codelength = jsonResult.length();
        String code = MD5Util.MD5(codelength+"");
        //为保证数据完整性，需要将接口数据的哈希码 给前端，进行验证
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "code");
        httpServletResponse.setHeader("code",code);
        return body;
    }
}
