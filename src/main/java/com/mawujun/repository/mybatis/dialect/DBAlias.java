package com.mawujun.repository.mybatis.dialect;
/**
 * 数据库别名枚举
* @author mawujun 16064988
* @createDate ：2018年12月6日 上午9:06:22
*/
public enum DBAlias {
	h2(H2Dialect.class)
	,hsqldb(HsqldbDialect.class)
	,derby(DerbyDialect.class)
	
	,sqlserver(SqlServerDialect.class),sqlserver2005(SqlServer2005Dialect.class),sqlserver2012(SqlServer2012Dialect.class) //这三个都是sql server，随便哪一个都可以
	,db2(Db2Dialect.class)
	
	,mysql(MySqlDialect.class)
	,oracle(OracleDialect.class)
	,postgresql(PostgreSQLDialect.class)
	
	,informix(InformixDialect.class),informix_sqli(InformixDialect.class)
	,phoenix(HsqldbDialect.class),mariadb(MySqlDialect.class),sqlite(MySqlDialect.class)
	
	,dm(OracleDialect.class)//达梦数据库
	,edb(OracleDialect.class)//阿里云PPAS数据库
	;
	
	private Class<? extends Dialect> dialectClass;
	
	DBAlias(Class<? extends Dialect> dialect){
		this.dialectClass=dialect;
	}

	public Class<? extends Dialect> getDialectClass() {
		return dialectClass;
	}



	

}
