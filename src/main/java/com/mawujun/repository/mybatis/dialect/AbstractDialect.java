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

public abstract class AbstractDialect implements Dialect {
	  //处理SQL
    protected CountSqlParser countSqlParser = new CountSqlParser();

	@Override
	public String getCountSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, PageInfo pageInfo, CacheKey pageKey) {
	        String countColumn = pageInfo.getCountColumn();
	        if (StringUtils.hasText(countColumn)) {
	            return countSqlParser.getSmartCountSql(boundSql.getSql(), countColumn);
	        }
	        return countSqlParser.getSmartCountSql(boundSql.getSql());
	}

    
	@Override
	public Object processParameterObject(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql, CacheKey pageKey) {
//		// 处理参数
//		Page page = getLocalPage();
//		// 如果只是 order by 就不必处理参数
//		if (page.isOrderByOnly()) {
//			return parameterObject;
//		}
		Map<String, Object> paramMap = null;
		if (parameterObject == null) {
			paramMap = new HashMap<String, Object>();
		} else if (parameterObject instanceof Map) {
			// 解决不可变Map的情况
			paramMap = new HashMap<String, Object>();
			paramMap.putAll((Map) parameterObject);
		} else {
			paramMap = new HashMap<String, Object>();
			// 动态sql时的判断条件不会出现在ParameterMapping中，但是必须有，所以这里需要收集所有的getter属性
			// TypeHandlerRegistry可以直接处理的会作为一个直接使用的对象进行处理
			boolean hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass());
			MetaObject metaObject = MetaObjectUtil.forObject(parameterObject);
			// 需要针对注解形式的MyProviderSqlSource保存原值
			if (!hasTypeHandler) {
				for (String name : metaObject.getGetterNames()) {
					paramMap.put(name, metaObject.getValue(name));
				}
			}
			// 下面这段方法，主要解决一个常见类型的参数时的问题
			if (boundSql.getParameterMappings() != null && boundSql.getParameterMappings().size() > 0) {
				for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
					String name = parameterMapping.getProperty();
					if (!name.equals(PAGEPARAMETER_FIRST) && !name.equals(PAGEPARAMETER_SECOND) && paramMap.get(name) == null) {
						if (hasTypeHandler || parameterMapping.getJavaType().equals(parameterObject.getClass())) {
							paramMap.put(name, parameterObject);
							break;
						}
					}
				}
			}
		}
		return processPageParameter1(ms, paramMap, rowBounds, boundSql, pageKey);
	}

	// @Override
	public Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds, BoundSql boundSql, CacheKey pageKey) {
		paramMap.put(PAGEPARAMETER_FIRST, rowBounds.getOffset());
		paramMap.put(PAGEPARAMETER_SECOND, rowBounds.getLimit());
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



}
