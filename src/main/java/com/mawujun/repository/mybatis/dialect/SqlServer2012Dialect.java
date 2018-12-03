package com.mawujun.repository.mybatis.dialect;

import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import com.mawujun.exception.BusinessException;

/**
 *
 *适用于sql server 2012之上的版本，
 *并且一定要在offset之前加上order by 子句才可以
 *
 *https://www.cnblogs.com/xunziji/archive/2012/08/06/2625563.html
 *https://www.cnblogs.com/fengxiaojiu/p/7994124.html
 * @author mwj
 *
 */
// Hibernate BUG: http://opensource.atlassian.com/projects/hibernate/browse/HHH-2655
// TODO 完善并测试SQLServer2005Dialect
public class SqlServer2012Dialect extends AbstractDialect{

	/**
	 * offset fetch next方式（SQL2012以上的版本才支持：推荐使用 ）
	 * 必须要有order by子句，在mapper.xml文件中编写sql的时候，必须加上order by 子句
	 */
	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
		String sql = boundSql.getSql();
		sql = sql.toLowerCase();
		if(!super.existsEndOrderBy(sql)) {
			throw new RuntimeException("sql的最后一定要加上order by子句，可以添加order by id");
		}
		 
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
