package com.fantasque.common.result;

import lombok.Getter;

/**
 * 错误码和错误信息定义
 * 10 表示通用 200表示成功
 * 20 登录
 * 30 用户
 * 40 权限菜单
 * 50 权限
 * @author LaFantasque
 * @version 1.0
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(10200,"成功"),//通用成功
    FAIL(10001,"系统未知异常"),//通用失败 系统未知异常
    DATA_ERROR(10004, "数据异常"),
    SERVICE_ERROR(10012, "服务异常"),
    LOGIN_ERROR(20001, "未知登录错误"),
    LOGIN_ADMIN_ERROR(20002, "用户未登陆"),//用户未登录
    LOGIN_USER_ERROR(20102, "用户不存在"),
    LOGIN_PASSWORD_ERROR(20103, "用户密码错误"),
    LOGIN_STATUS_ERROR(20104, "用户已被禁用"),
    // 用户注册错误
    REGISTER_ERROR(30101, "该用户已存在"),//用户已存在
    PHONE_ERROR(30102, "电话号码格式错误"),
    REMOVE_MENU_ERROR(40005,"该菜单含有子菜单，不能删除"),
    PERMISSION(50005, "该用户没有权限"),//用户没有权限
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
