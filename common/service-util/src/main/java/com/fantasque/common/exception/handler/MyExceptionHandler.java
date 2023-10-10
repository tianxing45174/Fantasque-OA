package com.fantasque.common.exception.handler;

import com.fantasque.common.exception.MyException;
import com.fantasque.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
}
