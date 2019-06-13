package com.mawujun.repository.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.mawujun.generator.code.ShowType;

@Retention(RetentionPolicy.RUNTIME) 
public @interface FieldDefine {
	/**
	 * 名称，标题，中文名，用于grid或者form
	 * @return
	 */
    String title();  
  
    /**
     * 排序，生成的时候排序的，数字越大，放在越前面。
     * 但是隐藏列还是放在最后，不受这个数字的影响
     * @return
     */
    int sort() default 0;  
  
    /**
     * 备注
     * @return
     */
    String remark() default "";  
      
    /**
     * 在界面显示的时候是不是隐藏列
     * @return
     */
    boolean hidden() default false;  
    
    /**
     * 如果hidden=true，这里无论设置成什么，在form中都只会显示为textfield
     * 指定在界面上显示时，以什么类型显示,默认是根据字段类型进行决定的，当指定这个值的时候
     * 才会按照指定的类型进行展示
     * 如果是枚举类型，枚举必须要有 String getName()方法
     * @author mawujun qq:16064988 mawujun1234@163.com
     * @return
     */
    ShowType showType() default ShowType.none;
    
    /**
     * 如果值为true，就表示要为这个字段生茶查询字段
     * @author mawujun 16064988@qq.com 
     * @return
     */
    boolean genQuery() default false; 
    
    
}
