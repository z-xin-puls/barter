package com.barter.service.impl;

import com.barter.common.BusinessException;
import com.barter.common.JwtUtil;
import com.barter.dto.LoginDTO;
import com.barter.dto.RegisterDTO;
import com.barter.entity.SysUser;
import com.barter.mapper.SysUserMapper;
import com.barter.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void register(RegisterDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BusinessException("账号不能为空");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        SysUser exist = userMapper.selectByUsername(dto.getUsername());
        if (exist != null) {
            throw new BusinessException("账号已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setStudentNo(dto.getStudentNo());
        user.setDepartment(dto.getDepartment());
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new BusinessException("账号或密码不能为空");
        }
        SysUser user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) {
            throw new BusinessException("账号不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被封禁，请联系管理员");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), "user");

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("avatar", user.getAvatar());
        result.put("userType", "user");
        return result;
    }
}
