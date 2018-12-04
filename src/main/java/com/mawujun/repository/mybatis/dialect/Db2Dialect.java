package com.mawujun.repository.mybatis.dialect;

import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

/**
 * @author mwj
 */
public class Db2Dialect extends AbstractDialect{

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

	
}
