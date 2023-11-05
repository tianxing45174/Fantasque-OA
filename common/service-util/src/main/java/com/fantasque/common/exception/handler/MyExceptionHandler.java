package com.fantasque.common.exception.handler;

import com.fantasque.common.exception.MyException;
import com.fantasque.common.result.Result;
import com.fantasque.common.result.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * @author LaFantasque
 * @version 1.0
 */
@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(MyException.class)
    public Result error(MyException e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }
    /**
     * spring security 权限异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.fail().resultCodeEnum(ResultCodeEnum.PERMISSION);
    }
}
