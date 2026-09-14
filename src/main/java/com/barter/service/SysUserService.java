package com.barter.service;

import com.barter.dto.LoginDTO;
import com.barter.dto.RegisterDTO;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface SysUserService {

    /**
     * 用户注册
     */
    void register(RegisterDTO dto);

    /**
     * 用户登录，返回 token 和用户信息
     */
    Map<String, Object> login(LoginDTO dto);
}
