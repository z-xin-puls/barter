package com.barter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员权限拦截器 —— 只允许 userType=admin 的管理员访问 /api/admin/**
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // JwtInterceptor 已经把 userType 放到 request attribute 里了
        Object typeObj = request.getAttribute("userType");
        if (typeObj == null || !"admin".equals(typeObj.toString())) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"code\":403,\"msg\":\"无权访问，仅管理员可操作\",\"data\":null}");
            return false;
        }
        return true;
    }
}
