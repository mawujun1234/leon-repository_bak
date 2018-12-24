package com.mawujun.repository.identity;

import javax.persistence.MappedSuperclass;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import com.mawujun.repository.validate.ValidatorUtils;


/**
 * 添加了验证的方法
 * @author mawujun
 *
 * @param <ID> 这个参数是指定什么类型的id，是int还是long
 */
@MappedSuperclass
public abstract class IdentityIdValidate extends IdentityId {
	/**
	 * 会抛出ConstraintViolationException异常
	 * @exception ConstraintViolationException
	 */
	public void validate(){
		ValidatorUtils.validate(this);
	}
	/**
	 * 将错误信息返回以separator分隔符进行分割，默认分隔符是；,并放在了ValidationException异常里面
	 * @param separator
	 * @exception ValidationException
	 */
	public void validate(String... separator){	
		ValidatorUtils.validateAndReturnMessage(this);
	}
}
