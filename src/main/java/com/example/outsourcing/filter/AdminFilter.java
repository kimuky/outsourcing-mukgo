package com.example.outsourcing.filter;

import com.example.outsourcing.entity.User;
import com.example.outsourcing.status.Authority;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class AdminFilter implements Filter {

    private static final String[] WHITE_LIST = {"/admin/**"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

//        if (!isWhiteList(requestURI)) {
            HttpSession session = httpServletRequest.getSession(false);

            log.info(requestURI);

            User user = (User) session.getAttribute("user");

            if (!user.getAuthority().equals(Authority.ADMIN)) {
                throw new RuntimeException("관리자가 아닙니다.");
            }
//        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

}
