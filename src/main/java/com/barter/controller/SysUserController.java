package com.barter.controller;

import com.barter.common.Result;
import com.barter.dto.LoginDTO;
import com.barter.dto.RegisterDTO;
import com.barter.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户模块控制器
 */
@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success("注册成功", null);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        Map<String, Object> data = userService.login(dto);
        return Result.success("登录成功", data);
    }
}
