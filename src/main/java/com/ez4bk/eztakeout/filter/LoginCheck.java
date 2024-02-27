package com.ez4bk.eztakeout.filter;

import com.alibaba.fastjson.JSON;
import com.ez4bk.eztakeout.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheck implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        log.info("requestURI: {}", requestURI);
        String[] urls = new String[]{
                "/api/employee/login",
                "/api/employee/logout",
                "/employee/**",
                "/customer/**",
        };

        boolean pass = checkURIs(requestURI, urls);
        if (pass) {
            filterChain.doFilter(request, response);
            return;
        }
        Object employee = request.getSession().getAttribute("employee");
        if (employee != null) {
            filterChain.doFilter(request, response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    public boolean checkURIs(String requestURI, String[] urls) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
