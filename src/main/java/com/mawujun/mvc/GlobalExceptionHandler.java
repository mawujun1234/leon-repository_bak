package com.mawujun.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mawujun.exception.BusinessException;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有不可知的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    R handleException(Exception e){
    	logger.error(e.getMessage(), e);

        return R.error("未知异常，请稍后重试或联系管理员!");
    }

    /**
     * 处理所有业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    R handleBusinessException(BusinessException e){
    	logger.error(e.getMessage(), e);
    	if(e.getErrorCode()!=null){
            return R.error(e.getErrorCode().getNumber(),e.getMessage());

    	} else {
            return R.error(e.getMessage());

    	}
    }

}
