package com.mawujun.repository.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 列的额外属性定义
 * @author admin
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface ColDefine {
	/**
	 * 注释
	 * @return
	 */
    String comment() default "";  
  
   /**
    * 中文标签,如果没有comment的话，如果comment没有定义的话，默认为添加到comment中
    * @return
    */
    String label() default "";  
  
    /**
     * 默认值
     * @return
     */
    String defaultValue() default "";  
    
    /**
     * 是否作为查询条件,会自动生成查询条件
     * @return
     */
    boolean cndable() default false;  
    /**
     * 设置为true，将会在前端自动生成文件上传，下载，删除的代码
     * @return
     */
    boolean uploadable() default false;  
    
    /**
     * 设置为true，就表示在前端生成的文本框等内容是不可编辑的
     * @return
     */
    boolean disabled() default false;  
      
  
    
    
}
