package com.mawujun.repository.identity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import com.mawujun.mvc.ValidatorUtils;

/**
 * 统一定义id的entity基类.是使用UUID作为生成策略
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * 子类可重载getId()函数重定义id的列名映射和生成策略.
 * @author mawujun
 *
 */
@MappedSuperclass
public abstract class UUIDValidate  extends UUID  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1985201725486995395L;
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
