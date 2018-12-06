package com.mawujun.repository.mybatis.dialect;
/**
 * 数据库别名枚举
* @author mawujun 16064988
* @createDate ：2018年12月6日 上午9:06:22
*/
public enum DBAlias {
	h2
	,sqlserver,sqlserver2005,sqlserver2012 //这三个都是sql server，随便哪一个都可以
	,db2,hsql,informix,mysql,oracle,postgresql;

}
