package com.incident.management.exception;

import com.incident.management.common.BaseResponse;
import com.incident.management.common.ErrorCode;
import com.incident.management.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException:" + e.getMessage(), e);
        return ResultUtil.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(BusinessException e){
        log.error("runtimeException", e);
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
