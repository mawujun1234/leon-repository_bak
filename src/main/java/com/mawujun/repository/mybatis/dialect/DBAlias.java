package com.mawujun.repository.mybatis.dialect;

import com.mawujun.generator.db.DB2Query;
import com.mawujun.generator.db.IDbQuery;
import com.mawujun.generator.db.MariadbQuery;
import com.mawujun.generator.db.MySqlQuery;
import com.mawujun.generator.db.OracleQuery;
import com.mawujun.generator.db.PostgreSqlQuery;
import com.mawujun.generator.db.SqlServerQuery;

/**
 * 数据库别名枚举
* @author mawujun 16064988
* @createDate ：2018年12月6日 上午9:06:22
*/
public enum DBAlias {
	h2(H2Dialect.class,null)
	,hsqldb(HsqldbDialect.class,null)
	,derby(DerbyDialect.class,null)
	
	,sqlserver(SqlServerDialect.class,SqlServerQuery.class),sqlserver2005(SqlServer2005Dialect.class,SqlServerQuery.class),sqlserver2012(SqlServer2012Dialect.class,SqlServerQuery.class) //这三个都是sql server，随便哪一个都可以
	,db2(Db2Dialect.class,DB2Query.class)
	
	,mysql(MySqlDialect.class,MySqlQuery.class)
	,oracle(OracleDialect.class,OracleQuery.class)
	,postgresql(PostgreSQLDialect.class,PostgreSqlQuery.class)
	
	,informix(InformixDialect.class,null),informix_sqli(InformixDialect.class,null)
	,phoenix(HsqldbDialect.class,null)
	,mariadb(MySqlDialect.class,MariadbQuery.class)
	,sqlite(MySqlDialect.class,null)
	
	,dm(OracleDialect.class,null)//达梦数据库
	,edb(OracleDialect.class,null)//阿里云PPAS数据库
	;
	
	private Class<? extends Dialect> dialectClass;
	private Class<? extends IDbQuery> dbQueryClass;
	
	DBAlias(Class<? extends Dialect> dialect, Class<? extends IDbQuery> dbQuery){
		this.dialectClass=dialect;
		this.dbQueryClass=dbQuery;
	}

	public Class<? extends Dialect> getDialectClass() {
		return dialectClass;
	}

	public Class<? extends IDbQuery> getDbQueryClass() {
		return dbQueryClass;
	}



	

}
