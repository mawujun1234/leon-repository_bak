package com.mawujun.repository.mybatis.dialect;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
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

import com.mawujun.generator.db.DbColumn;
import com.mawujun.repository.mybatis.interceptor.MetaObjectUtil;
import com.mawujun.utils.DateUtils;

/**
 * @author mwj
 */
public class MySqlDialect extends AbstractDialect {
    @Override
    public Object processPageParameter1(MappedStatement ms, Map<String, Object> paramMap, RowBounds rowBounds, BoundSql boundSql, CacheKey pageKey) {
        paramMap.put(PAGEPARAMETER_FIRST, rowBounds.getOffset());
        paramMap.put(PAGEPARAMETER_SECOND, rowBounds.getLimit());
//        //处理pageKey
//        pageKey.update(page.getStartRow());
//        pageKey.update(page.getPageSize());
        //处理参数配置
        if (boundSql.getParameterMappings() != null) {
            List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>(boundSql.getParameterMappings());
//            if (page.getStartRow() == 0) {
//                newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_SECOND, Integer.class).build());
//            } else {
                newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_FIRST, Integer.class).build());
                newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), PAGEPARAMETER_SECOND, Integer.class).build());
//            }
            MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
            metaObject.setValue("parameterMappings", newParameterMappings);
        }
        return paramMap;
    }

    //https://www.cnblogs.com/bg2015-07-05/p/4991437.html
    //https://www.cnblogs.com/ggjucheng/p/3352280.html
    private  Map<String, String> date_pattern_map=new LinkedHashMap<String,String>(){{		
		this.put("yyyy-MM-dd","%Y-%m-%d");
		this.put("yyyy-MM-dd HH:mm:ss","%Y-%m-%d %H:%i:%s");
		this.put("HH:mm:ss","%H:%i:%s");
		this.put("yyyy-MM","%Y-%m");
		this.put("yyyy","%Y");
		this.put("yyyy-MM-dd HH:mm", "%Y-%m-%d %H:%i");
	}};

	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey pageKey) {
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
//        if (page.getStartRow() == 0) {
//            sqlBuilder.append(" LIMIT ? ");
//        } else {
            sqlBuilder.append(" LIMIT ?, ? ");
 //       }
        return sqlBuilder.toString();
	}


	@Override
	public String getDateFormatFunction() {
		// TODO Auto-generated method stub
		return "DATE_FORMAT";
	}


	@Override
	public String getDateFormatStr(String dateStr) {
		String date_pattern=DateUtils.resolverDateFormat(dateStr);
		String db_pattern=date_pattern_map.get(date_pattern);
		if(db_pattern==null) {
			throw new IllegalArgumentException("当前的日期格式不支持:"+dateStr+",需要新增的话，新建date.pattern.properties文件，按"+getAlias()+".yyyy-MM-dd=yyyy-MM-dd,同时添加regular.yyyy-MM-dd=^\\\\\\\\d{4}-\\\\\\\\d{1,2}-\\\\\\\\d{1,2}$模式编写");
		}
		return db_pattern;
	}
	
	@Override
	public DBAlias getAlias() {
		// TODO Auto-generated method stub
		return DBAlias.mysql;
	}

	@Override
	public void addDateFormatStr(String java_pattern, String db_pattern) {
		// TODO Auto-generated method stub
		date_pattern_map.put(java_pattern, db_pattern);
	}

	
	public DbColumn columnTypeToProertyType(String columnType) {
		DbColumn dbColumn=new DbColumn();
		String t = columnType.toLowerCase();
        if (t.contains("char")) {//varchar,char,text
        	int index=t.indexOf('(');
        	dbColumn.setColumnType(t.substring(0,index));
        	dbColumn.setJavaType(String.class);
        	dbColumn.setLength(Integer.parseInt(t.substring(index+1,t.length()-1)));
        	dbColumn.setScale(null);
            return dbColumn;
        } if (t.contains("text")) {//varchar,char,text
        	dbColumn.setColumnType(t);
        	dbColumn.setJavaType(String.class);
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
            return dbColumn;
        }  else if (t.contains("bigint")) {
        	int index=t.indexOf('(');
        	dbColumn.setColumnType(t.substring(0,index));
        	dbColumn.setJavaType(Long.class);
        	dbColumn.setLength(Integer.parseInt(t.substring(index+1,t.length()-1)));
        	dbColumn.setScale(null);
            return dbColumn;
        } else if (t.contains("int")) {
        	int index=t.indexOf('(');
        	dbColumn.setColumnType(t.substring(0,index));
        	dbColumn.setJavaType(Integer.class);
        	dbColumn.setLength(Integer.parseInt(t.substring(index+1,t.length()-1)));
        	dbColumn.setScale(null);
        } else if (t.contains("date")) {
        	dbColumn.setColumnType("date");
        	dbColumn.setJavaType(Date.class);//使用java.sql.Date类型
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
        }  else if (t.contains("time")) {
        	dbColumn.setColumnType("time");
        	dbColumn.setJavaType(Time.class);//使用java.sql.Date类型
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
            //return DbColumnType.DATE;
        } else if ( t.contains("year")) {
        	dbColumn.setColumnType("year");
        	dbColumn.setJavaType(Integer.class);//使用java.sql.Date类型
        	dbColumn.setLength(4);
        	dbColumn.setScale(null);
            //return DbColumnType.DATE;
        } else if (t.contains("timestamp")) {
        	dbColumn.setColumnType("timestamp");
        	dbColumn.setJavaType(java.util.Date.class);//使用java.sql.Date类型
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
        }  else if (t.contains("datetime")) {
        	dbColumn.setColumnType("datetime");
        	dbColumn.setJavaType(java.util.Date.class);//使用java.sql.Date类型
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
        }  else if (t.contains("bit")) {
        	dbColumn.setColumnType("bit");
        	dbColumn.setJavaType(Boolean.class);//使用java.sql.Date类型
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
        } else if (t.contains("decimal")) {
        	dbColumn.setColumnType("decimal");
        	dbColumn.setJavaType(BigDecimal.class);//使用java.sql.Date类型
        	int index=t.indexOf('(');
        	String[] aaa=t.substring(index+1,t.length()-1).split(",");
        	dbColumn.setPrecision(Integer.parseInt(aaa[0]));
        	dbColumn.setScale(Integer.parseInt(aaa[1]));
            //return DbColumnType.BIG_DECIMAL;
        } else if (t.contains("clob")) {
        	dbColumn.setColumnType("clob");
        	dbColumn.setJavaType(String.class);//使用java.sql.Date类型
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
        } else if (t.contains("blob")) {
        	dbColumn.setColumnType("blob");
        	dbColumn.setJavaType(byte[].class);//使用java.sql.Date类型
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
        } else if (t.contains("binary")) {
        	dbColumn.setColumnType("binary");
        	dbColumn.setJavaType(Serializable.class);//使用java.sql.Date类型
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
        } else if (t.contains("float")) {
        	dbColumn.setColumnType("float");
        	dbColumn.setJavaType(Float.class);//使用java.sql.Date类型
        	int index=t.indexOf('(');
        	if(index!=-1) {
        		String[] aaa=t.substring(index+1,t.length()-1).split(",");
            	dbColumn.setPrecision(Integer.parseInt(aaa[0]));
            	dbColumn.setScale(Integer.parseInt(aaa[1]));
        	}
        } else if (t.contains("double")) {
        	dbColumn.setColumnType("double");
        	dbColumn.setJavaType(Double.class);//使用java.sql.Date类型
        	int index=t.indexOf('(');
        	if(index!=-1) {
        		String[] aaa=t.substring(index+1,t.length()-1).split(",");
            	dbColumn.setPrecision(Integer.parseInt(aaa[0]));
            	dbColumn.setScale(Integer.parseInt(aaa[1]));
        	}
        } else if (t.contains("json") || t.contains("enum")) {
        	dbColumn.setColumnType(t);
        	dbColumn.setJavaType(String.class);//使用java.sql.Date类型
        	dbColumn.setLength(null);
        	dbColumn.setScale(null);
        } else {
        	throw new RuntimeException("暂时不支持该类型的代码生成："+columnType);
        }
        return dbColumn;
	}

}
