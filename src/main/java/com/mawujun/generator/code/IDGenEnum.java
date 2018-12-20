package com.mawujun.generator.code;

public enum IDGenEnum {
	none("没有任何id设置"),
	/**
	 * //自定义的字符串
	 */
	assigned_str("自定义_字符串"),
	/**
	 * //主要就是雪花模型
	 */
	assigned_long("自定义_长整型"),
	/**
	 * ，如DB2、SQL Server、MySQL、Sybase和HypersonicSQL等
	 */
	identity("数据库自增"),//
	/**
	 * //如oralce、DB、SAP DB、PostgerSQL、McKoi中的sequence。MySQL这种不支持sequence的数据库则不行（可以使用identity）。
	 */
	sequence("序列化"),
	/**
	 * //将当前主键的值单独保存到一个数据库的表中，主键的值每次都是从指定的表中查询来获得，这种生成主键的方式也是很常用的。这种方法生成主键的策略可以适用于任何的数据库，不必担心不同数据库不兼容造成的问题。
	 */
	table("主键存在另外的表中"),
	/**
	 * //只能作为主键，唯一缺点长度较大，32位（Hibernate将UUID中间的“-”删除了）的字符串，占用存储空间大
	 */
	uuid("uuid");
	
	
	private String name;
	
	IDGenEnum(String name) {
		this.name=name;
	}
}
