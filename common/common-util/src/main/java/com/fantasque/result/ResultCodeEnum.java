package com.fantasque.result;

import lombok.Getter;

/**
 * 错误码和错误信息定义
 * 10 表示通用 200表示成功
 * 20 用户错误
 * @author LaFantasque
 * @version 1.0
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(10200,"成功"),//通用成功
    FAIL(10001,"系统未知异常"),//通用失败 系统未知异常
    DATA_ERROR(10004, "数据异常"),
    LOGIN_AUTH(20008, "未登陆"),//用户未登录
    PERMISSION(20009, "没有权限"),//用户没有权限
    SERVICE_ERROR(10012, "服务异常")
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
