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
 * A dialect compatible with the H2 database.
 * 
 * @author mwj
 *
 */
public class H2Dialect extends AbstractDialect {

//    public boolean supportsLimit() {
//        return true;
//    }
//
//	public String getLimitString(String sql, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
//		return new StringBuffer(sql.length() + 40).
//			append(sql).
//			append((offset > 0) ? " limit "+limitPlaceholder+" offset "+offsetPlaceholder : " limit "+limitPlaceholder).
//			toString();
//	}
//
//	@Override
//	public boolean supportsLimitOffset() {
//		return true;
//	}

	 @Override
	public Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds, BoundSql boundSql, CacheKey pageKey) {
		paramMap.put(PAGEPARAMETER_FIRST, rowBounds.getLimit());
		paramMap.put(PAGEPARAMETER_SECOND, rowBounds.getOffset());
		// 处理pageKey
		pageKey.update(rowBounds.getOffset());
		pageKey.update(rowBounds.getLimit());
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
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey pageKey) {
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(sql);
		sqlBuilder.append(" limit ? offset ? ");
		return sqlBuilder.toString();
	}
    
//    public boolean bindLimitParametersInReverseOrder() {
//        return true;
//    }    
//
//    public boolean bindLimitParametersFirst() {
//        return false;
//    }

    

}