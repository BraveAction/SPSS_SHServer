package com.spss.smarthome.common.exception;

/**
 * 程序问题造成的业务操作失败抛此异常 1000
 */
public class ServiceException extends GlobalException {
    public ServiceException(String message) {
        super(message);
        ERROR_CODE = 1000;
    }

    public ServiceException() {
        this("抱歉！服务端程序异常工程师正在修复。");
    }
}
