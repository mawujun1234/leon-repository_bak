package com.mawujun.repository.mybatis.dialect;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

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
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey pageKey) {
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
		sqlBuilder.append(sql);
		sqlBuilder.append(" LIMIT ? offset ? ");
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