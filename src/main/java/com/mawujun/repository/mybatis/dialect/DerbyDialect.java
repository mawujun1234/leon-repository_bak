package com.mawujun.repository.mybatis.dialect;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import com.mawujun.date.DateUtil;
import com.mawujun.generator.db.DbColumn;

/**
 * 
 * @author admin
 *
 */
public class DerbyDialect extends AbstractDialect{
	
	//http://db.apache.org/derby/docs/10.4/ref/
	public static  Map<String, String> date_pattern_map=new LinkedHashMap<String,String>(){{
		
		this.put("yyyy-MM-dd","DATE");//"yyyy-mm-dd"
		this.put("yyyy-MM-dd HH:mm:ss","TIMESTAMP");//"yyyy-mm-dd hh:mi:ss"
		this.put("HH:mm:ss","TIME");//"hh:mi:ss"
		this.put("yyyy-MM","yyyy-MM");//不能动，后面的7意思是23的样式,然后取7位
		//this.put("yyyy-MM-dd HH:mm", "16");//后面的16意思是120的样式,然后取16位
		this.put("yyyy","YEAR");
	}};

	@Override
	public DBAlias getAlias() {
		// TODO Auto-generated method stub
		return DBAlias.derby;
	}

	/**
	 * offset fetch next方式（SQL2012以上的版本才支持：推荐使用 ）
	 * 必须要有order by子句，在mapper.xml文件中编写sql的时候，必须加上order by 子句
	 */
	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
		String sql = boundSql.getSql();
		sql = sql.toLowerCase();
//		if(!super.existsEndOrderBy(sql)) {
//			throw new RuntimeException("sql的最后一定要加上order by子句，可以添加order by id");
//		}
		 
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

	/**
	 * 没有日期转换函数
	 */
	@Override
	public String getDateFormatFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDateFormatStr(String dateStr) {
		String date_pattern=DateUtil.resolverDateFormat(dateStr);
		String db_pattern=date_pattern_map.get(date_pattern);
		if(db_pattern==null) {
			throw new IllegalArgumentException("当前的日期格式不支持:"+dateStr+",需要新增的话，新建date.pattern.properties文件，按"+getAlias()+".yyyy-MM-dd=yyyy-MM-dd,同时添加regular.yyyy-MM-dd=^\\\\\\\\d{4}-\\\\\\\\d{1,2}-\\\\\\\\d{1,2}$模式编写");
		}
		//return new String[] {"varchar(100)",db_pattern};
		return db_pattern;
	}



	@Override
	public void addDateFormatStr(String java_pattern, String db_pattern) {
		// TODO Auto-generated method stub
		//date_pattern_map.put(java_pattern, db_pattern);
		throw new IllegalArgumentException("derby不允许添加新的日期格式，请参考官方文档，自行使用函数进行判断");
		
	}

	@Override
	public DbColumn columnTypeToProertyType(String columnType) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
