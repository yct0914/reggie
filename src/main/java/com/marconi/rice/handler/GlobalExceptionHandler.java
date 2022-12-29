package com.marconi.rice.handler;

import com.marconi.rice.common.CustomException;
import com.marconi.rice.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局Exception处理器 可以全局处理多种错误
 * @author Marconi
 * @date 2022/6/4
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler {

    /**
     * 添加已存在员工时的重复处理
     * @param e
     * @return
     * @see SQLIntegrityConstraintViolationException
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Response handler(SQLIntegrityConstraintViolationException e){
        String message = e.getMessage();
        log.error(message);
        String msg = "Duplicate entry";
        if (message.contains(msg)){
            String[] split = message.split(" ");
            String key = split[2];
            return new Response(HttpStatus.BAD_REQUEST.value(),key+"已存在！");
        }
        return new Response(HttpStatus.BAD_REQUEST.value(), "添加失败");
    }

    /**
     * 处理业务逻辑错误
     * 该错误为自定义错误类型
     * @param e
     * @return
     * @see CustomException
     */
    @ExceptionHandler(CustomException.class)
    public Response handler(CustomException e){
        log.error(e.getMessage());
        return new Response(400,e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public Response handler(NullPointerException e){
        log.error(e.getLocalizedMessage());
        log.error(e.getCause().getMessage());
        return new Response(400,"值为null");
    }

}
