package com.mawujun.repository.mybatis.dialect;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;

import com.mawujun.repository.mybatis.interceptor.MetaObjectUtil;
import com.mawujun.utils.DateUtils;

/**
 * @author mwj
 */
public class PostgreSQLDialect extends AbstractDialect{
	//https://blog.csdn.net/snn1410/article/details/7741283
	private  Map<String, String> date_pattern_map=new LinkedHashMap<String,String>(){{
		
		this.put("yyyy-MM-dd","YYYY-MM-DD");
		this.put("yyyy-MM-dd HH:mm:ss","YYYY-MM-DD HH24:MI:SS");
		this.put("HH:mm:ss","HH24:MI:SS");
		this.put("yyyy-MM","YYYY-MM");
		this.put("yyyy-MM-dd HH:mm", "YYYY-MM-DD HH24:MI");
	}};

	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(sql);
		sqlBuilder.append(" limit ? offset ? ");
		return sqlBuilder.toString();
	}

	@Override
	public Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds,
			BoundSql boundSql, CacheKey pageKey) {
		paramMap.put(PAGEPARAMETER_FIRST, rowBounds.getLimit());
		paramMap.put(PAGEPARAMETER_SECOND, rowBounds.getOffset());
		// 处理pageKey
		//pageKey.update(rowBounds.getOffset());
		//pageKey.update(rowBounds.getLimit());
		// 处理参数配置
		if (boundSql.getParameterMappings() != null) {
			List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>(boundSql.getParameterMappings());
//	            if (page.getStartRow() == 0) {
//	                newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_SECOND, Integer.class).build());
//	            } else {
			newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_FIRST, Integer.class).build());
			newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_SECOND, Integer.class).build());
//	            }
			MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
			metaObject.setValue("parameterMappings", newParameterMappings);
		}
		return paramMap;
	}

	@Override
	public String getDateFormatFunction() {
		// TODO Auto-generated method stub
		return "to_char";
	}

	@Override
	public String getDateFormatStr(String dateStr) {
		String date_pattern=DateUtils.resolverDateFormat(dateStr);
		String db_pattern=date_pattern_map.get(date_pattern);
		if(db_pattern==null) {
			throw new IllegalArgumentException("当前的日期格式不支持:"+dateStr);
		}
		//return new String[] {db_pattern};
		return db_pattern;
	}
	
	@Override
	public DBAlias getAlias() {
		// TODO Auto-generated method stub
		return DBAlias.postgresql;
	}

	@Override
	public void addDateFormatStr(String java_pattern, String db_pattern) {
		// TODO Auto-generated method stub
		date_pattern_map.put(java_pattern, db_pattern);
	}
}
