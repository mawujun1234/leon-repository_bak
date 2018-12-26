package com.mawujun.generator.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface LogicDelete {
	/**
	 * 默认值
	 * @return
	 */
	String defaultValue() default "0";
	/**
	 * 逻辑删除后的值
	 * @return
	 */
	String deleteValue() default "1";
}
