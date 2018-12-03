package com.mawujun.repository.mybatis.dialect;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    Map<String,String> columtypes=new HashMap<String,String>();
    private static final Pattern p = Pattern.compile("order\\s+by\\s+([\\w*|\\w+\\.\\w+](asc|desc|\\s*){1},?)+", Pattern.CASE_INSENSITIVE);
    
    /**
     * 判断是否存在order by子句，在子查询中也存在order by也会返回true
     * @return
     */
    public boolean existsOrderBy(String sql) {
    	Matcher m = p.matcher(sql);
    	return m.find();
    }
    /**
     * 判断sql的结尾是否存在order by子句
     * @param sql
     * @return
     */
    public boolean existsEndOrderBy(String sql) {
    	int len=sql.length();
    	Matcher match = p.matcher(sql);
    	while(match.find()) {
    		//System.out.println( " matches \"" + m.group(0) );
	        //System.out.println("start:" + m.start() + " end:" + m.end());
    		if(match.end()==len) {
    			return true;
    		}
    	}
    	return false;
    	
//    	MatchResult ms = null;
//        while (match.find()) {
//            ms = match.toMatchResult();
//            System.out.print("匹配对象的组结果：");
//            for (int i = 0; i < ms.groupCount(); i++) {
//                System.out.print(String.format("\n\t第%s组的结果是:%s",i+1,ms.group(i + 1)));
//            }
//            System.out.print("\n匹配的整个结果:");
//            System.out.println(ms.group());
//        }
    }
    /**
     * 获取最后的order by子句，如果没有就返回，则返回 空字符串
     * @param sql
     * @return
     */
    public String getEndOrderBy(String sql) {
    	int len=sql.length();
    	Matcher match = p.matcher(sql);
    	while(match.find()) {
    		//System.out.println( " matches \"" + m.group(0) );
	        //System.out.println("start:" + m.start() + " end:" + m.end());
    		if(match.end()==len) {
    			return match.group(0);
    		}
    	}
    	return "";
    	
    }
    /**
     * 去除所有的order by子句，
     * @param sql
     * @return
     */
    public String removeOrderby(String sql) {
    	Matcher m = p.matcher(sql);
    	StringBuffer sb = new StringBuffer();
    	while(m.find()) {
    		m.appendReplacement(sb, "");
    	}
    	m.appendTail(sb);
    	return sb.toString();
    }
    
    

	public AbstractDialect() {
		super();
		// TODO Auto-generated constructor stub
		//this.columtypes.put(Boolean.class.getName(), "bit"); sql server
		//this.columtypes.put(Boolean.class.getName(), "boolean");
		this.columtypes.put(Boolean.class.getName(), "tinyint");
		this.columtypes.put(Boolean.class.getName()+"_yn", "char(1)");
		this.columtypes.put(Boolean.class.getName()+"_tf", "char(1)");
		
		this.columtypes.put(Byte.class.getName(), "tinyint");
		this.columtypes.put(Short.class.getName(), "smallint");
		this.columtypes.put(Integer.class.getName(), "integer");
		this.columtypes.put(Long.class.getName(), "bigint");
		
		this.columtypes.put(Float.class.getName(), "float");
		this.columtypes.put(Double.class.getName(), "double");
		this.columtypes.put(BigDecimal.class.getName(), "numeric");
		this.columtypes.put(BigInteger.class.getName(), "numeric");
		
		//this.columtypes.put(7, "real");
		this.columtypes.put(Date.class.getName(), "date");
		this.columtypes.put(Date.class.getName()+"_time", "time");
		this.columtypes.put(Date.class.getName()+"_timestamp", "time");
		this.columtypes.put(java.sql.Date.class.getName(), "date");
		this.columtypes.put(Time.class.getName(), "time");
		this.columtypes.put(Timestamp.class.getName(), "timestamp");
		this.columtypes.put(Calendar.class.getName(), "timestamp");
		this.columtypes.put(Calendar.class.getName()+"_date", "date");
		
		//this.columtypes.put(-3, "bit varying($l)");
		//this.columtypes.put(-4, "bit varying($l)");
		
		this.columtypes.put(Character.class.getName(), "char");
		this.columtypes.put(String.class.getName(), "varchar");
		this.columtypes.put(String.class.getName()+"_clob", "clob");
		this.columtypes.put(Clob.class.getName(), "CLOB");
		this.columtypes.put("Character[]", "CLOB");
		this.columtypes.put("char[]", "CLOB");
		
		this.columtypes.put("Byte[]", "BLOB");
		this.columtypes.put("byte[]", "BLOB");	
		this.columtypes.put(Clob.class.getName(), "BLOB");
		this.columtypes.put(Serializable.class.getName(), "BLOB");//个属性实现了java.io.Serializable同时也不是基本类型, 并且没有在该属性上使用@Lob注解
		
		
//		this.columtypes.put(2004, "blob");
//		this.columtypes.put(1, "char($l)");
//		this.columtypes.put(12, "varchar($l)");
//		this.columtypes.put(-1, "varchar($l)");
//		this.columtypes.put(2005, "clob");
//		this.columtypes.put(-15, "nchar($l)");
//		this.columtypes.put(-9, "nvarchar($l)");
//		this.columtypes.put(-16, "nvarchar($l)");
//		this.columtypes.put(2011, "nclob");
		
//		this.columtypes(-7, "bit");
//		this.columtypes(16, "boolean");
//		this.columtypes(-6, "tinyint");
//		this.columtypes(5, "smallint");
//		this.columtypes(4, "integer");
//		this.columtypes(-5, "bigint");
//		this.columtypes(6, "float($p)");
//		this.columtypes(8, "double precision");
//		this.columtypes(2, "numeric($p,$s)");
//		this.columtypes(7, "real");
//		this.columtypes(91, "date");
//		this.columtypes(92, "time");
//		this.columtypes(93, "timestamp");
//		this.columtypes(-3, "bit varying($l)");
//		this.columtypes(-4, "bit varying($l)");
//		this.columtypes(2004, "blob");
//		this.columtypes(1, "char($l)");
//		this.columtypes(12, "varchar($l)");
//		this.columtypes(-1, "varchar($l)");
//		this.columtypes(2005, "clob");
//		this.columtypes(-15, "nchar($l)");
//		this.columtypes(-9, "nvarchar($l)");
//		this.columtypes(-16, "nvarchar($l)");
//		this.columtypes(2011, "nclob");
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
