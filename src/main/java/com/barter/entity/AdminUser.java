package com.barter.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员实体（独立于普通用户表 sys_user）
 */
@Data
public class AdminUser {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    /** 头像URL */
    private String avatar;
    /** 1正常 0禁用 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
