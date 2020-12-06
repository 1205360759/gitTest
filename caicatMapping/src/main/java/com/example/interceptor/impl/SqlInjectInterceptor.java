package com.example.interceptor.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手动防止sql注入
 * @author yp
 * @date 2019/7/22 9:42
 */
@Component
public class SqlInjectInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            Enumeration<String> names = request.getParameterNames();
            while(names.hasMoreElements()){
                String name = names.nextElement();
                String[] values = request.getParameterValues(name);
                for(String value: values){
                     if(judgeXSS(value.toLowerCase())){
                        response.setContentType("text/html;charset=UTF-8");
                        response.getWriter().print("参数含有非法攻击字符,已禁止继续访问！");
                        return false;
                    }else if(judgeParamLen(value)){
                        response.setContentType("text/html;charset=UTF-8");
                        response.getWriter().print("参数长度过长，有非法攻击倾向，已禁止继续访问！");
                        return false;
                    }
                    //跨站xss清理
                    clearXss(value);
                }
            }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 判断参数是否含有攻击串
     * @param value
     * @return
     */
    public static boolean judgeXSS(String value){
        if(value == null || "".equals(value)){
            return false;
        }
        String xssStr = "select|update|delete|drop|truncate|alter|%20|=|--|'|%|#|+|//|\\|!=|<|>";
        String[] xssArr = xssStr.split("\\|");
        for(int i=0;i<xssArr.length;i++){
            if(value.indexOf(xssArr[i])>-1){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断输入字符的长度
     * @param value
     * @return
     */
    public boolean judgeParamLen(String value){
        if(value == null || "".equals(value)){
            return false;
        }
        if(value.length() >= 255){
            return true;
        }
        return false;
    }
    /**
     * 处理跨站xss字符转义
     *
     * @param value
     * @return
     */
    private String clearXss(String value) {
        if(StringUtils.isEmpty(value)){
            return value;
        }
        else{
            if (value != null) {
                if (value != null) {
                    // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                    // avoid encoded attacks.
                    // value = ESAPI.encoder().canonicalize(value);
                    // Avoid null characters
                    value = value.replaceAll("", "");
                    // Avoid anything between script tags
                    Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
                    value = scriptPattern.matcher(value).replaceAll("");
                    // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e­xpression
                    // 会误伤百度富文本编辑器
//                    scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
//                    value = scriptPattern.matcher(value).replaceAll("");
//                    scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
//                    value = scriptPattern.matcher(value).replaceAll("");
                    // Remove any lonesome </script> tag
                    scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
                    value = scriptPattern.matcher(value).replaceAll("");
                    // Remove any lonesome <script ...> tag
                    scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                    value = scriptPattern.matcher(value).replaceAll("");
                    // Avoid eval(...) e­xpressions
                    scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                    value = scriptPattern.matcher(value).replaceAll("");
                    // Avoid e­xpression(...) e­xpressions
                    scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                    value = scriptPattern.matcher(value).replaceAll("");
                    // Avoid javascript:... e­xpressions
                    scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
                    value = scriptPattern.matcher(value).replaceAll("");
                    // Avoid vbscript:... e­xpressions
                    scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
                    value = scriptPattern.matcher(value).replaceAll("");
                    // Avoid onload= e­xpressions
                    scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                    value = scriptPattern.matcher(value).replaceAll("");
                }
            }
            return value;
        }
    }

    private boolean regDomainjudge(String domain){
        boolean flag ;
        Pattern p =Pattern.compile("(\\w*\\.?){1,3}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(domain);
        if (matcher.find()){
            flag = true;
            System.out.println(matcher.group());
        }else{
            flag = false;
        }
        System.out.println(flag);
        return flag;
    }
}
