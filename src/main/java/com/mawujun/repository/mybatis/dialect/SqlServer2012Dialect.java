package com.mawujun.repository.mybatis.dialect;

import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

/**
 *
 * @author mwj
 *
 */
// Hibernate BUG: http://opensource.atlassian.com/projects/hibernate/browse/HHH-2655
// TODO 完善并测试SQLServer2005Dialect
public class SqlServer2012Dialect extends AbstractDialect{

	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 64);
        sqlBuilder.append(sql);
        sqlBuilder.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
        pageKey.update(rowBounds.getLimit());
        return sqlBuilder.toString();
	}

	@Override
	public Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds,
			BoundSql boundSql, CacheKey pageKey) {
		paramMap.put(PAGEPARAMETER_FIRST, rowBounds.getOffset());
        paramMap.put(PAGEPARAMETER_SECOND, rowBounds.getLimit());
        //处理pageKey
        pageKey.update(rowBounds.getOffset());
        pageKey.update(rowBounds.getLimit());
        //处理参数配置
        handleParameter(boundSql, ms);
        return paramMap;
	}

	
}
