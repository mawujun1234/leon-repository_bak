package com.mawujun.generator.other;

import com.mawujun.util.StringUtil;


/**
 * 规则就是简单的前缀加上类名(全小写)，形成表名，而且会把驼峰表示的属性转换为下划线
 * 字段名也还一个道理，也会加前缀，而且会把驼峰表示的属性转换为下划线
 * @author mawujun email:16064988@qq.com qq:16064988
 *
 */
public class DefaultNameStrategy implements NameStrategy {
	//public static  String columnPrefix = "";
	//public static  String tablePrefix = "t_";
	public String classToTableName(String className,String tablePrefix) {
		// TODO Auto-generated method stub
		return tablePrefix + StringUtil.camelToUnderline(StringUtil.uncapitalize(className));
	}
	@Override
	public String classToAlias(String className) {
		// TODO Auto-generated method stub
		return className.toLowerCase();
	}

	/**
	 * 默认无前缀，并且驼峰变下划线
	 */
	public String propertyToColumnName(String propertyName,String columnPrefix) {
		if(StringUtil.hasText(columnPrefix)) {
			return columnPrefix + StringUtil.camelToUnderline(propertyName);
		} else {
			return  StringUtil.camelToUnderline(propertyName);
		}
		
	}

	
	@Override
	public String columnNameToProperty(String name, String... prefix) {
		// TODO Auto-generated method stub
		return removePrefixAndCamel(name,prefix);
	}

    /**
     * 去掉下划线前缀且将后半部分转成驼峰格式
     *
     * @param name
     * @param tablePrefix
     * @return
     */
    public String removePrefixAndCamel(String name, String[] tablePrefix) {
        return underlineToCamel(removePrefix(name, tablePrefix));
    }
    
    public String underlineToCamel(String name) {
    	return StringUtil.underlineToCamel(name);
    }
	
    /**
     * 去掉指定的前缀
     *
     * @param name
     * @param prefix
     * @return
     */
    public  String removePrefix(String name, String... prefix) {
        if (StringUtil.isEmpty(name)) {
            return "";
        }
        if (null != prefix) {
            for (String pf : prefix) {
                if (name.toLowerCase().matches("^" + pf.toLowerCase() + ".*")) {
                    // 判断是否有匹配的前缀，然后截取前缀
                    // 删除前缀
                    return name.substring(pf.length());
                }
            }
        }
        return name;
    }

	
}
