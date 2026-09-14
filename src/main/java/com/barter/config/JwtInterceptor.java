package com.barter.config;

import com.barter.common.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 登录拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取token
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\",\"data\":null}");
            return false;
        }
        try {
            // 将用户信息存入 request 作用域（用 JwtUtil 处理好的类型，避免 Integer/Long 问题）
            request.setAttribute("userId", JwtUtil.getUserId(token));
            request.setAttribute("username", JwtUtil.getUsername(token));
            return true;
        } catch (ExpiredJwtException e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"msg\":\"登录已过期，请重新登录\",\"data\":null}");
            return false;
        } catch (SignatureException e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"msg\":\"token无效\",\"data\":null}");
            return false;
        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"msg\":\"认证失败\",\"data\":null}");
            return false;
        }
    }
}
