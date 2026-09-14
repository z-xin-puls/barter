package com.barter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果封装类
 */
@Data
public class Result<T> implements Serializable {

    private Integer code;   // 状态码 200成功 500失败
    private String msg;     // 提示信息
    private T data;         // 返回数据

    public static <T> Result<T> success() {
        return build(200, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return build(200, "success", data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return build(200, msg, data);
    }

    public static <T> Result<T> error(String msg) {
        return build(500, msg, null);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return build(code, msg, null);
    }

    public static <T> Result<T> build(Integer code, String msg, T data) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
