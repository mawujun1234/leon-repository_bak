package com.mawujun.repository.mybatis.dialect;

import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import com.mawujun.utils.DateUtils;

/**
 * https://www.cnblogs.com/fengxiaojiu/p/7994124.html
 * 支持sql server2005以前版本的分页
 * @author admin
 *
 */
public class SqlServerDialect extends AbstractDialect{


	/**
	 * ROW_NUMBER() OVER()方式适用于2005以上版本
	 * top not in方式 （适应于数据库2012以下的版本）
	 */
	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
		throw new RuntimeException("暂不支持，因为2005以前的版本都是使用top的形式来分页的，性能较差，所以还是自己写分页语句，在mapperx.ml文件中写格式为xxxx和xxxx_count的select标签");
	}

	@Override
	public Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds,
			BoundSql boundSql, CacheKey pageKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDateFormatFunction() {
		// TODO Auto-generated method stub
		return "CONVERT";
	}

	@Override
	public String getDateFormatStr(String dateStr) {
		String date_pattern=DateUtils.resolverDateFormat(dateStr);
		String db_pattern=SqlServer2012Dialect.date_pattern_map.get(date_pattern);
		if(db_pattern==null) {
			throw new IllegalArgumentException("当前的日期格式不支持:"+dateStr+",需要新增的话，新建date.pattern.properties文件，按"+getAlias()+".yyyy-MM-dd=yyyy-MM-dd,同时添加regular.yyyy-MM-dd=^\\\\\\\\d{4}-\\\\\\\\d{1,2}-\\\\\\\\d{1,2}$模式编写");
		}
		//return new String[] {"varchar(100)",db_pattern};
		return db_pattern;
	}
	
	@Override
	public DBAlias getAlias() {
		// TODO Auto-generated method stub
		return DBAlias.sqlserver;
	}

	@Override
	public void addDateFormatStr(String java_pattern, String db_pattern) {
		// TODO Auto-generated method stub
		SqlServer2012Dialect.date_pattern_map.put(java_pattern, db_pattern);
	}

}
