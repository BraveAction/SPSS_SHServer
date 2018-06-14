package com.spss.smarthome.controller.common;

import com.spss.smarthome.common.exception.GlobalException;

/**
 * 用户请求参数错误 1001
 */
public class RequestParameterException extends GlobalException {
    public RequestParameterException(String message) {
        super(message);
        code = 1001;
    }

    public RequestParameterException() {
        this("参数有误！");
    }
}
