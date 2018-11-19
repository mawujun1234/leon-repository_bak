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
    
    

	public AbstractDialect() {
		super();
		// TODO Auto-generated constructor stub
		this.registerColumnType(-7, "bit");
		this.registerColumnType(16, "boolean");
		this.registerColumnType(-6, "tinyint");
		this.registerColumnType(5, "smallint");
		this.registerColumnType(4, "integer");
		this.registerColumnType(-5, "bigint");
		this.registerColumnType(6, "float($p)");
		this.registerColumnType(8, "double precision");
		this.registerColumnType(2, "numeric($p,$s)");
		this.registerColumnType(7, "real");
		this.registerColumnType(91, "date");
		this.registerColumnType(92, "time");
		this.registerColumnType(93, "timestamp");
		this.registerColumnType(-3, "bit varying($l)");
		this.registerColumnType(-4, "bit varying($l)");
		this.registerColumnType(2004, "blob");
		this.registerColumnType(1, "char($l)");
		this.registerColumnType(12, "varchar($l)");
		this.registerColumnType(-1, "varchar($l)");
		this.registerColumnType(2005, "clob");
		this.registerColumnType(-15, "nchar($l)");
		this.registerColumnType(-9, "nvarchar($l)");
		this.registerColumnType(-16, "nvarchar($l)");
		this.registerColumnType(2011, "nclob");
	}


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
	
    protected void handleParameter(BoundSql boundSql, MappedStatement ms){
        if (boundSql.getParameterMappings() != null) {
            List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>(boundSql.getParameterMappings());
            newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_FIRST, Integer.class).build());
            newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_SECOND, Integer.class).build());
            MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
            metaObject.setValue("parameterMappings", newParameterMappings);
        }
    }

	public abstract Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds, BoundSql boundSql, CacheKey pageKey);



}
