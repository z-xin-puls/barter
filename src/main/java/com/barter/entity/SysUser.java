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
    /** 学号 */
    private String studentNo;
    /** 院系 */
    private String department;
    /** 头像URL */
    private String avatar;
    /** 1正常 0封禁 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
