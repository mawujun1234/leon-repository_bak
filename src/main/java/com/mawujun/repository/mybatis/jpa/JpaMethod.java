package com.mawujun.repository.mybatis.jpa;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 加了这个注解就表示这个方法，调动的是JpaDao，如果没有就表示调用的是mybatis
 * @author mawujun
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface JpaMethod {

}
