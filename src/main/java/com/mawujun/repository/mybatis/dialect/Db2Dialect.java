package com.mawujun.repository.mybatis.dialect;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import com.mawujun.date.DateUtil;
import com.mawujun.generator.db.DbColumn;

/**
 * @author mwj
 */
public class Db2Dialect extends AbstractDialect{
	
	//https://blog.csdn.net/Happy_wu/article/details/52594245
	private  Map<String, String> date_pattern_map=new LinkedHashMap<String,String>(){{
		this.put("yyyy-MM-dd","YYYY-MM-DD");
		this.put("yyyy-MM-dd HH:mm:ss","YYYY-MM-DD HH24:MI:SS");
		this.put("HH:mm:ss","HH24:MI:Ss");
		this.put("yyyy-MM","YYYY-MM");
		this.put("yyyy","YYYY");
		this.put("yyyy-MM-dd HH:mm", "YYYY-MM-DD HH24:MI");
	}};
	
	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 140);
        sqlBuilder.append("SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID BETWEEN ? AND ?");
        return sqlBuilder.toString();
	}

	@Override
	public Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds,
			BoundSql boundSql, CacheKey pageKey) {
		 paramMap.put(PAGEPARAMETER_FIRST, rowBounds.getOffset()+ 1);
	        paramMap.put(PAGEPARAMETER_SECOND, rowBounds.getOffset()+rowBounds.getLimit());
//	        //处理pageKey
//	        pageKey.update(rowBounds.getOffset()+ 1);
//	        pageKey.update(rowBounds.getOffset()+rowBounds.getLimit());
	        //处理参数配置
	        handleParameter(boundSql, ms);
	        return paramMap;
	}

	@Override
	public String getDateFormatFunction() {
		// TODO Auto-generated method stub
		return "TO_CHAR";
	}

	@Override
	public String getDateFormatStr(String dateStr) {
		String date_pattern= DateUtil.resolverDateFormat(dateStr);
		String db_pattern=date_pattern_map.get(date_pattern);
		if(db_pattern==null) {
			throw new IllegalArgumentException("当前的日期格式不支持:"+dateStr+",需要新增的话，新建date.pattern.properties文件，按"+getAlias()+".yyyy-MM-dd=yyyy-MM-dd,同时添加regular.yyyy-MM-dd=^\\\\\\\\d{4}-\\\\\\\\d{1,2}-\\\\\\\\d{1,2}$模式编写");
		}
		//return new String[] {db_pattern};
		return db_pattern;
	}

	@Override
	public DBAlias getAlias() {
		// TODO Auto-generated method stub
		return DBAlias.db2;
	}

	@Override
	public void addDateFormatStr(String java_pattern, String db_pattern) {
		// TODO Auto-generated method stub
		date_pattern_map.put(java_pattern, db_pattern);
	}

	@Override
	public DbColumn columnTypeToProertyType(String columnType) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
