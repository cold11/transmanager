package com.cold.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: ohj
 * @Date: 2019/7/4 09:02
 * @Description: 拦截处理
 */
@ControllerAdvice
public class RequestFilter extends OncePerRequestFilter {
    /*
     * 拦截请求
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String urlStr = request.getRequestURI();
        if(null != SecurityUtils.getSubject()) {//只允许一个用户登录同一个浏览器窗口
            String htmlUserAccount = request.getParameter("userAccount");
            Subject currentUser = SecurityUtils.getSubject();
            String sessionUserAccount = (String) currentUser.getSession().getAttribute("userAccount");
            if(StringUtils.isNotBlank(htmlUserAccount)&&StringUtils.isNotBlank(sessionUserAccount)) {
                if(!StringUtils.equals(htmlUserAccount, sessionUserAccount)) {
                    response.addHeader("sessionstatus", "timeOut");
                    return;
                }
            }
        }




        if(urlStr.equals("/main")) {
            Subject currentUser = SecurityUtils.getSubject();
            if(currentUser.isAuthenticated()) {
                request.getRequestDispatcher("/qkkjapp/views/main.html").forward(request, response);
            }
            else {
                response.sendRedirect("/");
            }
        }else if (urlStr.equals("/login")) {
            Subject currentUser = SecurityUtils.getSubject();
            if(null != currentUser) {
                currentUser.logout();
            }
            filterChain.doFilter(request, response);
        }
        else {
            filterChain.doFilter(request, response);
        }
    }
}
