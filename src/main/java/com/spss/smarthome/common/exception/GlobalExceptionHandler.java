package com.spss.smarthome.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spss.smarthome.common.controller.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;


/**
 * 全局异常统一返回格式
 * {"ERROR_CODE":异常码,"message":"异常信息","data":null}
 * 异常包括：
 * (1:用户请求接口方式错误 2:应用功能性异常）
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String logExceptionFormat = "全局异常(GlobalExceptionHandler)处理抓取的信息: 异常码: %s 描述: %s";

    //其他错误
//    @ExceptionHandler(value = Exception.class)
//    public Object exceptionn(HttpServletRequest request, Exception ex) {
//        return resultFormat(request, 14, ex);
//    }   protected Logger logger = LoggerFactory.getLogger(this.getClass());

//    //运行时异常
//    @ExceptionHandler(RuntimeException.class)
//    public Object runtimeExceptionHandler(HttpServletRequest request, RuntimeException ex) {
//        return resultFormat(request, 1, ex);
//    }

    //用户今牌异常(403)
    @ExceptionHandler(AuthenticationException.class)
    public Object runtimeExceptionHandler(HttpServletRequest request, AuthenticationException ex) {
        return resultFormat(request, HttpStatus.FORBIDDEN.value(), new UserException("令牌无效"));
    }

    //请示参数异常
    @ExceptionHandler(RequestParameterException.class)
    public Object runtimeExceptionHandler(HttpServletRequest request, RequestParameterException ex) {
        return resultFormat(request, GlobalException.ERROR_CODE, ex);
    }

    //业务异常
    @ExceptionHandler(ServiceException.class)
    public Object runtimeExceptionHandler(HttpServletRequest request, ServiceException ex) {
        return resultFormat(request, GlobalException.ERROR_CODE, ex);
    }

    //业务异常
    @ExceptionHandler(UserException.class)
    public Object runtimeExceptionHandler(HttpServletRequest request, UserException ex) {
        return resultFormat(request, GlobalException.ERROR_CODE, ex);
    }

    //数据库操作时异常
    @ExceptionHandler(SQLException.class)
    public Object runtimeExceptionHandler(HttpServletRequest request, SQLException ex) {
        return resultFormat(request, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex);
    }

    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    public Object nullPointerExceptionHandler(HttpServletRequest request, NullPointerException ex) {
        return resultFormat(request, 2, ex);
    }

    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public Object classCastExceptionHandler(HttpServletRequest request, ClassCastException ex) {
        return resultFormat(request, 3, ex);
    }

    //IO异常
    @ExceptionHandler(IOException.class)
    public Object iOExceptionHandler(HttpServletRequest request, IOException ex) {
        return resultFormat(request, 4, ex);
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    public Object noSuchMethodExceptionHandler(HttpServletRequest request, NoSuchMethodException ex) {
        return resultFormat(request, 5, ex);
    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Object indexOutOfBoundsExceptionHandler(HttpServletRequest request, IndexOutOfBoundsException ex) {
        return resultFormat(request, 6, ex);
    }

    //400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Object requestNotReadable(HttpServletRequest request, HttpMessageNotReadableException ex) {
        return resultFormat(request, 7, ex);
    }

    //400错误
    @ExceptionHandler({TypeMismatchException.class})
    public Object requestTypeMismatch(HttpServletRequest request, TypeMismatchException ex) {
        return resultFormat(request, 8, ex);
    }

    //400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Object requestMissingServletRequest(HttpServletRequest request, MissingServletRequestParameterException ex) {
        return resultFormat(request, 9, ex);
    }

    //405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Object request405(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        return resultFormat(request, HttpStatus.METHOD_NOT_ALLOWED.value(), ex);
    }

    //406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public Object request406(HttpServletRequest request, HttpMediaTypeNotAcceptableException ex) {
        return resultFormat(request, 11, ex);
    }

    //500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public Object server500(HttpServletRequest request, RuntimeException ex) {
        return resultFormat(request, 12, ex);
    }

    //栈溢出
    @ExceptionHandler({StackOverflowError.class})
    public Object requestStackOverflow(HttpServletRequest request, StackOverflowError ex) {
        return resultFormat(request, 13, ex);
    }


    private <T extends Throwable> Object resultFormat(HttpServletRequest request, Integer code, T ex) {

        //自定义异常
        if (ex instanceof GlobalException) {
            code = GlobalException.ERROR_CODE;
        }

        ex.printStackTrace();
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw, true));
        log.error(sw.toString());


        log.error(String.format(logExceptionFormat, code, ex.getMessage()));
        log.error("请示路径: " + request.getRequestURI() + "  方法：" + request.getMethod());

        /**
         * 打印请求数据
         */
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (request.getMethod().equalsIgnoreCase(RequestMethod.GET.name())) {
                log.error("请求参数: " + mapper.writeValueAsString(request.getParameterMap()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return Result.failed(RequestParameterException.ERROR_CODE, "请求参数格式不正确!");
        }

        return Result.failed(code, ex.getMessage());
    }

}
