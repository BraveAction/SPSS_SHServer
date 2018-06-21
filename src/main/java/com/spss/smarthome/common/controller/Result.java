package com.spss.smarthome.common.controller;

/**
 * 统一返回结果
 * {"ERROR_CODE":200,"message":"","data":{}/[]/""}
 */

import org.springframework.http.HttpStatus;

public class Result {
    private int code;
    private String message = "";
    private Object data = null;


    public static Result success(Object data) {
        Result successResult = new Result();
        successResult.code = HttpStatus.OK.value();
        successResult.data = data;
        return successResult;
    }

    public static Result success(String message) {
        Result successResult = new Result();
        successResult.code = HttpStatus.OK.value();
        successResult.message = message;
        return successResult;
    }

    public static Result success(String message, Object data) {
        Result successResult = new Result();
        successResult.code = HttpStatus.OK.value();
        successResult.message = message;
        successResult.data = data;
        return successResult;
    }

    public static Result failed(Integer code, String message) {
        Result failedResult = new Result();
        failedResult.code = code;
        failedResult.message = message;
        return failedResult;
    }


    public Result setCode(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

}
