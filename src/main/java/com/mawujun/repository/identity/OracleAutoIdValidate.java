package com.mawujun.repository.identity;

import javax.persistence.MappedSuperclass;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import com.mawujun.repository.validate.ValidatorUtils;

/**
 * 要对Entity类进行改造。因为Oracle一般使用SEQUCENCE作为主键生成策略，而且每种Entity类使用一个独立的Sequence。
 * 此时统一的IdEntity基类就不再合适了，最好把它变为一个Id接口，然后在每个Entity中定义id及其Sequence。
 * 注意这里有个性能优化的地方，Hibernate一次问Oracle要了20个id自己慢慢用。相应的，sequence创建时需要修改increment by=20
 * create sequence SEQ_USER start with 100 increment by 20;这是要在oracle中执行的

 * @author mawujun
 *
 */
@MappedSuperclass
public class OracleAutoIdValidate  extends SequenceAutoId  implements IdEntity<Long> {
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
