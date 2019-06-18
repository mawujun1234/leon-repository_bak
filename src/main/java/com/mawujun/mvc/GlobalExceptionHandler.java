package com.mawujun.mvc;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mawujun.exception.BizException;

@ControllerAdvice
//@RestControllerAdvice
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
    	if("org.apache.shiro.authz.UnauthorizedException".equals(e.getClass().getName())) {
    		return R.error("没有权限!");
    	}

        return R.error("未知异常，请稍后重试或联系管理员!");
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    R handleException(DataIntegrityViolationException e){
    	
    	 return R.error(e.getRootCause().getMessage());
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    R handleException(ConstraintViolationException e){
    	logger.error(e.getMessage(), e);
    	Set<ConstraintViolation<?>> messages=e.getConstraintViolations();
    	StringBuilder msg=new StringBuilder();
    	for(ConstraintViolation<?> vio:messages) {
    		vio.getMessage();
    		msg.append(vio.getMessage());
    	}

        return R.error("数据完整性异常:"+msg);
    }

    /**
     * 处理所有业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    R handleBusinessException(BizException e){
    	logger.error(e.getMessage(), e);
    	if(e.getErrorCode()!=null){
            return R.error(e.getErrorCode().getNumber(),e.getMessage());

    	} else {
            return R.error(e.getMessage());

    	}
    }

}
