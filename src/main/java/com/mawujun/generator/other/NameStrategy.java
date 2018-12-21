package com.mawujun.generator.other;

/**
 * 类的名称到表的名称的转换规则
 * @author mawujun email:16064988@qq.com qq:16064988
 *
 */
public interface NameStrategy {
	/**
	 * 类名转换成别名胡规则同，默认所有字段变成小写
	 * @param className
	 * @return
	 */
	public String classToAlias(String className);
	/**
	 * 把类名转换为表名，默认表名前面加上t_
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param className
	 * @return
	 */
	public String classToTableName(String className,String tablePrefix);
	/**
	 * 把一个属性名称转换为表名,默认把驼峰形式改成下划线的形式
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param propertyName
	 * @return
	 */
	public String propertyToColumnName(String propertyName,String columnPrefix);
	
	public  String columnNameToProperty(String name, String... prefix);
}
