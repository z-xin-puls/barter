package com.barter.config;

import com.barter.common.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 登录拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 标准 Authorization: Bearer <token>
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null || token.isEmpty()) {
            sendError(response, 401, "请先登录");
            return false;
        }
        try {
            request.setAttribute("userId", jwtUtil.getUserId(token));
            request.setAttribute("username", jwtUtil.getUsername(token));
            request.setAttribute("userType", jwtUtil.getUserType(token));
            return true;
        } catch (ExpiredJwtException e) {
            sendError(response, 401, "登录已过期，请重新登录");
            return false;
        } catch (SignatureException e) {
            sendError(response, 401, "token无效");
            return false;
        } catch (Exception e) {
            sendError(response, 401, "认证失败");
            return false;
        }
    }

    private void sendError(HttpServletResponse response, int status, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        response.getWriter().write("{\"code\":" + status + ",\"msg\":\"" + msg + "\",\"data\":null}");
    }
}
