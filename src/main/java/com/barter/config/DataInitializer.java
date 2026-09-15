package com.barter.config;

import com.barter.entity.AdminUser;
import com.barter.entity.SysUser;
import com.barter.mapper.AdminUserMapper;
import com.barter.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 启动时初始化默认用户（BCrypt 加密密码）
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 普通用户 → sys_user 表
        createUserIfAbsent("test", "123456", "测试用户", "13800138000");
        // 管理员 → admin_user 表
        createAdminIfAbsent("admin", "admin123", "系统管理员", "13900000000");
    }

    private void createUserIfAbsent(String username, String rawPassword, String realName, String phone) {
        SysUser existing = userMapper.selectByUsername(username);
        if (existing == null) {
            SysUser user = new SysUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRealName(realName);
            user.setPhone(phone);
            user.setStatus(1);
            userMapper.insert(user);
            log.info("初始化默认普通用户: {}", username);
        }
    }

    private void createAdminIfAbsent(String username, String rawPassword, String realName, String phone) {
        AdminUser existing = adminUserMapper.selectByUsername(username);
        if (existing == null) {
            AdminUser admin = new AdminUser();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(rawPassword));
            admin.setRealName(realName);
            admin.setPhone(phone);
            admin.setStatus(1);
            adminUserMapper.insert(admin);
            log.info("初始化默认管理员: {}", username);
        }
    }
}
