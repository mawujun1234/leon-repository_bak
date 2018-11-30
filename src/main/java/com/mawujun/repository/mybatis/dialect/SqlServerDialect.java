package com.mawujun.repository.mybatis.dialect;

import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

/**
 * https://www.cnblogs.com/fengxiaojiu/p/7994124.html 分页方法
 * @author mwj
 */
public class SqlServerDialect extends AbstractDialect{


	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
//	       pageKey.update(rowBounds.getOffset());
//	        pageKey.update(rowBounds.getLimit());
//	        String cacheSql = CACHE_PAGESQL.get(sql);
//	        if (cacheSql == null) {
//	            cacheSql = sql;
//	            cacheSql = replaceSql.replace(cacheSql);
//	            cacheSql = pageSql.convertToPageSql(cacheSql, null, null);
//	            cacheSql = replaceSql.restore(cacheSql);
//	            CACHE_PAGESQL.put(sql, cacheSql);
//	        }
//	        cacheSql = cacheSql.replace(String.valueOf(Long.MIN_VALUE), String.valueOf(rowBounds.getOffset()));
//	        cacheSql = cacheSql.replace(String.valueOf(Long.MAX_VALUE), String.valueOf(rowBounds.getLimit()));
	        return null;
	}

	@Override
	public Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds,
			BoundSql boundSql, CacheKey pageKey) {
		// TODO Auto-generated method stub
		return null;
	}
//
//	public boolean supportsLimitOffset(){
//		return false;
//	}
//	
//	public boolean supportsLimit() {
//		return true;
//	}
//	
//	static int getAfterSelectInsertPoint(String sql) {
//		int selectIndex = sql.toLowerCase().indexOf( "select" );
//		final int selectDistinctIndex = sql.toLowerCase().indexOf( "select distinct" );
//		return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
//	}
//
//	public String getLimitString(String sql, int offset, int limit) {
//		return getLimitString(sql,offset,null,limit,null);
//	}
//
//	public String getLimitString(String querySelect, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
//		if ( offset > 0 ) {
//			throw new UnsupportedOperationException( "sql server has no offset" );
//		}
////		if(limitPlaceholder != null) {
////			throw new UnsupportedOperationException(" sql server not support variable limit");
////		}
//		
//		return new StringBuffer( querySelect.length() + 8 )
//				.append( querySelect )
//				.insert( getAfterSelectInsertPoint( querySelect ), " top " + limit )
//				.toString();
//	}
//	
//	// TODO add Dialect.supportsVariableLimit() for sqlserver 
////	public boolean supportsVariableLimit() {
////		return false;
////	}

}
