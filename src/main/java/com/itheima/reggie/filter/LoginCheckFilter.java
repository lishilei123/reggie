package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter{
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;




        String requestURI=request.getRequestURI();
        log.info("拦截到请求:{}",requestURI);
        String[] urls= new String[]{
                "/employee/login",
                "/employee/logout",
                "/back/**",
                "/front/**"

        };
        boolean check=check(urls,requestURI);
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }


        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，用户id为{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;

        }

        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
         return;


        log.info("拦截到请求：{}",request.getRequestURI());

        filterChain.doFilter(request,response);
    }
    public boolean check(String[]urls,String requestURI){
        for(String url:urls){
            boolean match=PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;

    }
}
