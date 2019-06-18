package com.mawujun.repository.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
//@Target(value= {ElementType.FIELD,ElementType.TYPE,ElementType.ANNOTATION_TYPE})
public @interface FK {
	/**
	 * 外键的名字，如果没有定义，将会自动生成,fk_md5值
	 * @return
	 */
	String name() default "";
//	/**
//	 * 表的名称,可以通过T.xxx.tablename引用
//	 * 如果放在字段上，这个就不需要填了，以字段所在的类对应的表为准
//	 * table和entity选一个填写就好
//	 * @return
//	 */
//	String table()  default "";
	/**
	 * 列的名称，可以通过T.xxx.xxx引用
	 * 如果放在字段上，这个就不需要填了，以字段对应的属性为准
	 * column和field选一个填写就好
	 * @return
	 */
	String[] columnNames() default {};
//	/**
//	 * 表的名称,可以通过T.xxx.tablename引用
//	 * 必填，refEntity和refTable选一个填写就好
//	 * @return
//	 */
//	String refTable();
	/**
	 * 列的名称，可以通过T.xxx.xxx引用
	 * 必填，refColumn和refField选一个填写就好
	 * @return
	 */
	String[] refColumnNames()  default {};
//	/**
//	 * 实体类,，也可以直接通过City.class
//	 * 如果放在字段上，这个就不需要填了，以字段所在的类为准
//	 * table和entity选一个填写就好
//	 * @return
//	 */
//	Class<?> entity()  default Void.class;
//	/**
//	 * 实体类对应的字段名称，可以通过M.xxx.yyy应用
//	 * 如果放在字段上，这个就不需要填了,以字段为准
//	 * column和field选一个填写就好
//	 * @return
//	 */
//	String field() default "";
	
	
	/**
	 * 外键指向的实体类,，也可以直接通过City.class
	 * 必填，refEntity和refTable选一个填写就好
	 * @return
	 */
	Class<?> refEntity();//  default Void.class;加了这个就不是必填了
//	/**
//	 * 外键的实体类对应的字段名称，可以通过M.xxx.yyy应用
//	 * 必填，refColumn和refField选一个填写就好
//	 * @return
//	 */
//	String refField() default "";
	/**
	 * 设置为true的话，就表示不生产外键的sql语句
	 * @return
	 */
	boolean noddl() default false;


}
