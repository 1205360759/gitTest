package com.example.interceptor.impl;

import com.alibaba.fastjson.JSON;
import com.example.entity.SysUserTokenEntity;
import com.example.service.SysUserTokenService;
import com.example.service.UserService;
import com.example.utils.MD5Util;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "sysUserTokenService")
    private SysUserTokenService sysUserTokenService;

//    private static Logger log = LoggerFactory.getLogger(RequestFilter.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String token = request.getHeader("token");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With," +
                        "If-Modified-Since,Cache-Control,Content-Type, Accept-Language,Accept" +
                        "Origin, Accept-Encoding,token,Access-Control-Expose-Headers,code");
//        HttpSession session = request.getSession();
//        session.setMaxInactiveInterval(30 * 60);//保持时间半小时
        boolean isLogout = false;
        if (null == token || token == "") {//未登录
            isLogout = false;
        } else {//token不为空时（1.token与数据库最后一次登录的token匹配  2.token与数据库最后一次登录的token不匹配）
            //查询数据库
            //User usert = userService.getTokenValue(token);
            SysUserTokenEntity userTokenEntity = sysUserTokenService.selectBytoken(token);
            if (userTokenEntity == null) {
                isLogout = false;
            } else {
                if (null == userTokenEntity.getToken()) {//与数据库token不匹配
                    isLogout = false;
                } else {
                    String tokenInRedis = userTokenEntity.getToken();
                    if (!tokenInRedis.equals(token)) {
                        isLogout = false;
                    } else {//与数据库token匹配,过期时间大于当前时间
                        if (userTokenEntity.getExpireTime().getTime() > System.currentTimeMillis()) {
                            isLogout = true;
                        } else {
                            isLogout = false;
                        }
                    }
                }
            }
        }
        if (!isLogout) {
            response.getWriter().println("401");
            response.getWriter().println("Not logged in");
            //throw new RRException("请重新登陆");
        }
        return isLogout;
    }

    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}
