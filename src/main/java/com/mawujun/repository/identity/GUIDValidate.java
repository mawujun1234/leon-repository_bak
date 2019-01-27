package com.mawujun.repository.identity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import com.mawujun.mvc.ValidatorUtils;

/**
 * guid 采用数据库底层的guid算法机制，对应MySQL的uuid()函数，SQL Server的newid()函数，ORCALE的rawtohex(sys_guid())函数等
 * @author mawujun
 *
 */
@MappedSuperclass
public abstract class GUIDValidate extends GUID implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 79567480559673949L;
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
