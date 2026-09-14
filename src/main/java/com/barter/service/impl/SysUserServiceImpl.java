package com.barter.service.impl;

import com.barter.common.JwtUtil;
import com.barter.dto.LoginDTO;
import com.barter.dto.RegisterDTO;
import com.barter.entity.SysUser;
import com.barter.mapper.SysUserMapper;
import com.barter.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public void register(RegisterDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new RuntimeException("账号不能为空");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        // 检查用户名是否已存在
        SysUser exist = userMapper.selectByUsername(dto.getUsername());
        if (exist != null) {
            throw new RuntimeException("账号已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // 简单存储密码
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        userMapper.insert(user);
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new RuntimeException("账号或密码不能为空");
        }
        SysUser user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        // 生成 token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        return result;
    }
}
