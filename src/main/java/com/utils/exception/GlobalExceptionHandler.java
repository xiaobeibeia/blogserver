package com.utils.exception;

import cn.hutool.json.JSONObject;
import com.model.comm.Results;
import com.utils.enums.ErrorInfoEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //参数错误异常处理
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Results handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return Results.error("参数错误", errors);
    }
    /**
     * 本系统自定义错误的拦截器
     * 拦截到此错误之后,就返回这个类里面的json给前端
     * 常见使用场景是参数校验失败,抛出此错,返回错误信息给前端
     */
    //必须加ResponseBody
    @ResponseBody
    @ExceptionHandler(BlogException.class)
    @ResponseStatus(HttpStatus.OK)
    public JSONObject commonJsonExceptionHandler(BlogException blogException) {
        return blogException.getResultJson();
    }

    @ResponseBody
    @ExceptionHandler(value = SignatureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Results signatureExceptionHandler(SignatureException exception) {
        log.error("SignatureException:{}", exception.getMessage());
        return Results.fromErrorInfo(ErrorInfoEnum.TOKEN_INVALID);
    }

    //404异常处理
    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Results noHandlerFoundExceptionHandler(NoHandlerFoundException exception) {
        log.error("NoHandlerFoundException:{}", exception.getMessage());
        return Results.fromErrorInfo(ErrorInfoEnum.RESOURCES_NOT_FOUND);
    }
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Results exceptionHandler(Exception exception) {
        exception.printStackTrace();
        log.error("Exception:{}", exception.getMessage());
        return Results.fromErrorInfo(ErrorInfoEnum.UNKNOWN_ERROR);
    }


}
/*@Slf4j ：lombok 注解，自动注入log对象到容器，可以使用log对象打印日志
@ControllerAdvice ： 表示这个类用于全局异常处理
@ExceptionHandler ： 表示此类处理什么类型的异常
@ResponseStatus ： 返回的http状态码*/