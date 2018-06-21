package com.spss.smarthome.common.exception;

/**
 * 用户请求参数错误 1001
 */
public class RequestParameterException extends GlobalException {
    public RequestParameterException(String message) {
        super(message);
        ERROR_CODE = 1001;
    }

    public RequestParameterException() {
        this("参数有误！");
    }
}
