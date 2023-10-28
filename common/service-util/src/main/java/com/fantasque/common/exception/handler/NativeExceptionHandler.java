package com.fantasque.common.exception.handler;

import com.fantasque.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author LaFantasque
 * @version 1.0
 */
@RestControllerAdvice
public class NativeExceptionHandler {
    @ExceptionHandler(Exception.class) // 捕获全局异常
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }
    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("算数异常");
    }
}
