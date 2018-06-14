package com.spss.smarthome.controller;

import com.spss.smarthome.controller.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
public class BaseController implements ErrorController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ERROR_PATH = "/error";

    /**
     * 全局异常处理
     * 1：接口调用权限异常
     * 2：接口不存在异常
     *
     * @param response
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    public Result handleForbiddenError(HttpServletResponse response) {
        if (response.getStatus() == HttpStatus.FORBIDDEN.value()) {
            return Result.failed(response.getStatus(), "您还没有登录!");
        } else if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            return Result.failed(response.getStatus(), "您请求的接口不存在!");
        }
        return Result.failed(response.getStatus(), "系统修复中，服务已停止!");
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
