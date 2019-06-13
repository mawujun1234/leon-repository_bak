package com.mawujun.repository.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.persistence.UniqueConstraint;

@Retention(RetentionPolicy.RUNTIME) 
public @interface TableDefine {
	/**
	 * 表格的注释，也可以使用@org.hibernate.annotations.Table(comment="表注释", appliesTo = "t_city") 
	 * 达到这个目的
	 * @return
	 */
    String comment() default "";
    /**
     * 定义表格的外键。
     * 
     * 如果同个类引用通个另外的类的列，那就变成复合外键
     * 唯一性约束，请再@Table注解上使用。
     * 索引，也请再@Table上定义
     * @return
     */
    FK[] fks() default {};
}
