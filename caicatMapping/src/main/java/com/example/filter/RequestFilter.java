package com.example.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@Component
public class RequestFilter implements Filter {


    private static Logger log = LoggerFactory.getLogger(RequestFilter.class);

    private final String loginPath = "/login.html";


    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        // 获得在下面代码中要用的request,respogetRootUrlnse,session对象
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //"*"存在风险，建议指定可信任的域名来接收响应信息，如"http://www.sosoapi.com", 处理跨域请求
        resp.addHeader("Access-Control-Allow-Origin", "*");
        //如果存在自定义的header参数，需要在此处添加，逗号分隔
        resp.addHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, "
                + "If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, "
                + "Content-Type, X-E4M-With");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        log.info(servletRequest.getRequestURI());
        HttpSession session = servletRequest.getSession();
        session.setMaxInactiveInterval(30 * 60);
        // 获得用户请求的URI
        String requestPath = servletRequest.getRequestURI();
//        if (needFilter(requestPath)) {
//            resp.sendRedirect(((HttpServletRequest) request).getContextPath()+loginPath);
//        }else
//            {
//            chain.doFilter(servletRequest, resp);
//            return;
//        }
        if(true){
            chain.doFilter(servletRequest, resp);
            return;
        }
    }

    @Override
    public void destroy() {

    }


    /*** 请求地址是否需要过滤。
     *
     * @param requestPath 请求地址。
     * @return 请求地址是否需要过滤。
     */
    private boolean needFilter(String requestPath) {

        // 登录页面无需过滤
        if (requestPath.equals(loginPath)) {
            return false;
        }

        // 静态资源无需过滤
        if (requestPath.toLowerCase().startsWith("/css/") || requestPath.toLowerCase().startsWith("/css/")
            || requestPath.toLowerCase().startsWith("/data/") || requestPath.toLowerCase().startsWith("/js/")
            || requestPath.toLowerCase().startsWith("/fonts/") || requestPath.toLowerCase().startsWith("/scripts/")
            || requestPath.toLowerCase().startsWith("/img/") || requestPath.toLowerCase().startsWith("/fonts/")
            || requestPath.toLowerCase().startsWith("/js/") || requestPath.toLowerCase().startsWith("/user/pcaptcha")) {
            return false;
        }

        return true;
    }
}
