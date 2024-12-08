package com.example.outsourcing.filter;

import com.example.outsourcing.entity.User;
import com.example.outsourcing.status.Authority;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 로그를 찍기위해 서블릿리퀘스트
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        log.info(requestURI);

        // 유저 세션을 가져와서 권한을 확인
        HttpSession session = httpServletRequest.getSession(false);
        User user = (User) session.getAttribute("user");
        if (!user.getAuthority().equals(Authority.ADMIN)) {
            throw new RuntimeException("관리자가 아닙니다.");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
