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

public class InformixDialect extends AbstractDialect {
	
	//http://lhbthanks.iteye.com/blog/1667174
	private  Map<String, String> date_pattern_map=new LinkedHashMap<String,String>(){{
		
		this.put("yyyy-MM-dd","%Y-%m-%d");
		this.put("yyyy-MM-dd HH:mm:ss","%Y-%m-%d %H:%M:%S");
		this.put("HH:mm:ss","%H:%M:%S");
		this.put("yyyy-MM","%Y-%m");
		this.put("yyyy-MM-dd HH:mm", "%Y-%m-%d %H:%M");
	}};

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

}
