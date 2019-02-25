package com.yesmywine.security.shiro;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by SJQ on 2017/3/23.
 */
public class RequestFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        return;
    }

    public void destroy() {

    }

//    private ShiroRedisSessionDAO redisSessionDAO;
//    private DefaultWebSecurityManager securityManager;
//
//    public void init(FilterConfig filterConfig) throws ServletException {
//        System.out.println("初始化");
//    }
//
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//
//
//
//        //        try {
////            HttpServletRequest request = (HttpServletRequest) servletRequest;
////            HttpServletResponse response = (HttpServletResponse) servletResponse;
////            String header = request.getHeader("Authorization");
////            // 获取当前页面文件名此处url为：/Gzlkh/login.jsp
////            String url = request.getRequestURI();
////            String method = request.getMethod();
////            String user = "";
////
////            chain.doFilter(request, response);
////        } catch (Exception e) {
////
////        }
//    }
//
//    public void destroy() {
//        System.out.println("销毁");
//    }
//


}
