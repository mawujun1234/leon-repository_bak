package com.mawujun.repository.mybatis.dialect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;

import com.mawujun.repository.mybatis.interceptor.MetaObjectUtil;

/**
 * Dialect for HSQLDB
 * @author mwj
 */
public class HsqlDialect extends AbstractDialect{

	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 20);
        sqlBuilder.append(sql);
        //if (page.getPageSize() > 0) {
            sqlBuilder.append(" LIMIT ? OFFSET ? ");
        //}
       //if (page.getStartRow() > 0) {
       //     sqlBuilder.append("  ");
       // }
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

//	public boolean supportsLimit() {
//		return true;
//	}
//
//	public boolean supportsLimitOffset() {
//		return true;
//	}
//
//	public String getLimitString(String sql, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
//		boolean hasOffset = offset>0;
//		return new StringBuffer( sql.length() + 10 )
//		.append( sql )
//		.insert( sql.toLowerCase().indexOf( "select" ) + 6, hasOffset ? " limit "+offsetPlaceholder+" "+limitPlaceholder : " top "+limitPlaceholder )
//		.toString();
//	}
//    
}
