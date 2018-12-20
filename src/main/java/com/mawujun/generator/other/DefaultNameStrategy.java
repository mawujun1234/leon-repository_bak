package com.mawujun.generator.other;

import com.mawujun.utils.string.StringUtils;


/**
 * 规则就是简单的前缀加上类名(全小写)，形成表名，而且会把驼峰表示的属性转换为下划线
 * 字段名也还一个道理，也会加前缀，而且会把驼峰表示的属性转换为下划线
 * @author mawujun email:16064988@qq.com qq:16064988
 *
 */
public class DefaultNameStrategy implements NameStrategy {
	public static  String columnPrefix = "";
	public static  String tablePrefix = "t_";
	public String classToTableName(String className) {
		// TODO Auto-generated method stub
		return tablePrefix + StringUtils.camelToUnderline(StringUtils.uncapitalize(className));
	}
	@Override
	public String classToAlias(String className) {
		// TODO Auto-generated method stub
		return className.toLowerCase();
	}

	/**
	 * 默认无前缀，并且驼峰变下划线
	 */
	public String propertyToColumnName(String propertyName) {
		return columnPrefix + StringUtils.camelToUnderline(propertyName);
	}

	public static String getColumnPrefix() {
		return columnPrefix;
	}

	public static void setColumnPrefix(String columnPrefix) {
		DefaultNameStrategy.columnPrefix = columnPrefix;
	}

	public static String getTablePrefix() {
		return tablePrefix;
	}

	public static void setTablePrefix(String tablePrefix) {
		DefaultNameStrategy.tablePrefix = tablePrefix;
	}
	
}
