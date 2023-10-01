package com.fantasque.result;

import lombok.Data;

/**
 * @author LaFantasque
 * @version 1.0
 */
@Data
public class Result<T> {
    // 状态码
    private Integer code;

    // 消息
    private String message;

    // 数据
    private T data;

    public Result() {}

    // 封装数据
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<T>();
        if (data != null)
            result.setData(data);
        return result;
    }
    public static <T> Result<T> build(T body, Integer code, String message) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }


    // 操作成功
    public static<T> Result<T> ok(){
        return Result.ok(null);
    }
    public static<T> Result<T> ok(T data){
        Result<T> result = build(data);
        return build(data, ResultCodeEnum.SUCCESS);
    }

    // 操作失败
    public static<T> Result<T> fail(){
        return Result.fail(null);
    }
    public static<T> Result<T> fail(T data){
        Result<T> result = build(data);
        return build(data, ResultCodeEnum.FAIL);
    }
    public static<T> Result<T> fail(ResultCodeEnum resultCodeEnum){
        Result<T> result = build(null);
        return build(null, resultCodeEnum);
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}
