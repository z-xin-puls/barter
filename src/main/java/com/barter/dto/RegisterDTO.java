package com.barter.dto;

import lombok.Data;

/**
 * 注册请求
 */
@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String realName;
    private String phone;
    /** 学号 */
    private String studentNo;
    /** 院系 */
    private String department;
}
