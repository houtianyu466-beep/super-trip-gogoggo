package com.example.tripagent.interceptor;

import com.example.tripagent.common.auth.LoginUser;
import com.example.tripagent.common.auth.UserContext;
import com.example.tripagent.common.exception.BusinessException;
import com.example.tripagent.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || token.trim().isEmpty()) {
            throw new BusinessException("请先登录");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        LoginUser loginUser = JwtUtil.parseToken(token);
        UserContext.set(loginUser);
        return true;

    }
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
