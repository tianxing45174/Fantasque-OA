package com.fantasque.common.exception;

import com.fantasque.result.ResultCodeEnum;
import lombok.Data;
import lombok.ToString;

/**
 * @author LaFantasque
 * @version 1.0
 */
@Data
@ToString
public class MyException extends RuntimeException{

    private Integer code;
    private String message;

    public MyException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public MyException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

}
