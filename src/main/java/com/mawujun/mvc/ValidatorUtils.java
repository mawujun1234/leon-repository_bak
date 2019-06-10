package com.mawujun.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.mawujun.exception.BizException;
import com.mawujun.util.StringUtils;

public class ValidatorUtils {
	 //private final Log logger = LogFactory.getLog(getClass());

	private static Validator validator;

	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
	}


	
    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws BizException  校验不通过，则报BizException异常
     */
    public static void validate(Object object, Class<?>... groups)
            throws BizException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for(ConstraintViolation<Object> constraint:  constraintViolations){
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new BizException(msg.toString());
        }
    }
    
	/**
	 * 如果验证异常，将会派出ValidationException异常，异常信息以字符串的形式返回，分隔符默认是分号，也可以自己指定
	 * @param separator
	 * @exception ValidationException
	 */
	public static <T> void validateAndReturnMessage(T t, String... separator) { 
		String errorMsg=null;
		try{
			ValidatorUtils.validate(t);
		} catch(ConstraintViolationException e){
			String sep=";";
			if(separator!=null && separator.length>0){
				sep=separator[0];
			}
			errorMsg=ValidatorUtils.convertMessage(e, sep);
			
		}
		throw new ValidationException(errorMsg); 
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolation>为字符串, 以separator分割.
	 */
	public static String convertMessage(Set<? extends ConstraintViolation> constraintViolations, String separator) {
		List<String> errorMessages = new ArrayList<String>();//Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getMessage());
		}
		return StringUtils.join(errorMessages, separator);
	}

	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>为字符串, 以separator分割.
	 */
	public static String convertMessage(ConstraintViolationException e, String separator) {
		return convertMessage(e.getConstraintViolations(), separator);
	}

}
