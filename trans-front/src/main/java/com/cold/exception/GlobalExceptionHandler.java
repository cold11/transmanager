package com.cold.exception;

import com.cold.common.ResultBaseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Auther: ohj
 * @Date: 2019/6/19 09:09
 * @Description: 全局异常
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultBaseDto handleException(Exception e) {
        log.error(ExceptionUtils.getMessage(e));  // 记录错误信息
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }else if(e instanceof UnauthorizedException){
            msg = "没有权限";
        }
        log.error(msg,e);
        return new ResultBaseDto(false,"500",msg);
    }
}