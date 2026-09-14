package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
public class SysUser {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    /** 0普通用户 1管理员 */
    private Integer role;
    private LocalDateTime createTime;
}
