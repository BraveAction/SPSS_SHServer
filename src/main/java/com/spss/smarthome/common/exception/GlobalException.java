package com.spss.smarthome.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 全局异常
 */
public abstract class GlobalException extends RuntimeException {
    //全局默认异常码
    public static Integer ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public GlobalException(String message) {
        super(message);
    }

}
