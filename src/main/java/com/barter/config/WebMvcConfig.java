package com.barter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * WebMvc 配置 - 注册拦截器、静态资源映射
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private AdminAuthInterceptor adminAuthInterceptor;

    /** 上传文件存储目录（绝对路径） */
    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/user/register",
                        "/api/user/login",
                        "/api/user/resetPassword",
                        "/api/admin/login",
                        "/api/category/list",
                        "/api/item/page",
                        "/api/item/detail/**",
                        "/api/review/user/**",
                        "/api/review/rating/**"
                );

        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/api/admin/**")
                .excludePathPatterns("/api/admin/login");
    }

    /**
     * 映射上传文件目录，使 /uploads/** 可直接通过 HTTP 访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 转为绝对路径，确保 file: 协议能正确定位
        File absPath = new File(uploadPath).getAbsoluteFile();
        String location = absPath.getAbsolutePath().replace('\\', '/');
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/" + location);
    }
}
