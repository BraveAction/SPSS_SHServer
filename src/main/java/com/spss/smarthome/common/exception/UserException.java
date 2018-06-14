package com.spss.smarthome.common.exception;

/**
 * 由于用户的权限，参数错误无法执行的业务抛此异常 1002
 */
public class UserException extends GlobalException {
    public UserException(String message) {
        super(message);
        code = 1002;
    }
}
