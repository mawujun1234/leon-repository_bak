package com.mawujun.repository.mybatis.dialect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;

import com.mawujun.repository.mybatis.interceptor.CountSqlParser;
import com.mawujun.repository.mybatis.interceptor.MetaObjectUtil;
import com.mawujun.repository.utils.PageInfo;
import com.mawujun.utils.string.StringUtils;

/**
 * @author mwj
 */
public class MySQLDialect extends AbstractDialect {



	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey pageKey) {
//		String sql = boundSql.getSql();
//	        Page page = getLocalPage();
//	        //支持 order by
//	        String orderBy = page.getOrderBy();
//	        if (StringUtil.isNotEmpty(orderBy)) {
//	            pageKey.update(orderBy);
//	            sql = OrderByParser.converToOrderBySql(sql, orderBy);
//	        }
//	        if (page.isOrderByOnly()) {
//	            return sql;
//	        }
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
		sqlBuilder.append(sql);
		sqlBuilder.append(" LIMIT ?, ? ");
		return sqlBuilder.toString();
	}


}
