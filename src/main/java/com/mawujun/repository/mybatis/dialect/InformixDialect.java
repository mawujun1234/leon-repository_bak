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

public class InformixDialect extends AbstractDialect {

	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 40);
		sqlBuilder.append("SELECT ");
		sqlBuilder.append(" SKIP ?  FIRST ? ");
//		if (page.getStartRow() > 0) {
//			sqlBuilder.append(" SKIP ? ");
//		}
//		if (page.getPageSize() > 0) {
//			sqlBuilder.append(" FIRST ? ");
//		}
		sqlBuilder.append(" * FROM ( ");
		sqlBuilder.append(sql);
		sqlBuilder.append(" ) TEMP_T ");
		return sqlBuilder.toString();
	}

	@Override
	public Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds,
			BoundSql boundSql, CacheKey pageKey) {
		paramMap.put(PAGEPARAMETER_FIRST, rowBounds.getOffset());
		paramMap.put(PAGEPARAMETER_SECOND, rowBounds.getLimit());
		// 处理pageKey
		pageKey.update(rowBounds.getOffset());
		pageKey.update(rowBounds.getLimit());
		// 处理参数配置
		if (boundSql.getParameterMappings() != null) {
			List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();
			//if (page.getStartRow() > 0) {
				newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_FIRST, Integer.class)
								.build());
			//}
			//if (page.getPageSize() > 0) {
				newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_SECOND, Integer.class)
								.build());
			//}
			newParameterMappings.addAll(boundSql.getParameterMappings());
			MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
			metaObject.setValue("parameterMappings", newParameterMappings);
		}
		return paramMap;
	}

}
