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
 * @author mwj
 */
public class OracleDialect extends AbstractDialect{

	//https://www.cnblogs.com/henuyuxiang/p/4062558.html
	/**
     * 主要用于sql查询的时候，根据查询条件自动对数据库的日志转换成对应的格式进行比较
     */
	private  Map<String, String> date_pattern_map=new LinkedHashMap<String,String>(){{
		
		this.put("yyyy-MM-dd","yyyy-MM-dd");
		this.put("yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH24:mi:ss");
		this.put("HH:mm:ss","HH24:mi:ss");
		this.put("yyyy-MM","yyyy-MM");
		this.put("yyyy","yyyy");
		this.put("yyyy-MM-dd HH:mm", "yyyy-MM-dd HH24:mi");
	}};
	
	@Override
	public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds,
			CacheKey pageKey) {
		String sql = boundSql.getSql();
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("SELECT * FROM ( ");
        sqlBuilder.append(" SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) TMP_PAGE)");
        sqlBuilder.append(" WHERE ROW_ID <= ? AND ROW_ID > ?");
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

	@Override
	public String getDateFormatFunction() {
		// TODO Auto-generated method stub
		return "to_char";
	}

	@Override
	public String getDateFormatStr(String dateStr) {
		String date_pattern= DateUtil.resolverDateFormat(dateStr);
		String db_pattern=date_pattern_map.get(date_pattern);
		if(db_pattern==null) {
			throw new IllegalArgumentException("当前的日期格式不支持:"+dateStr+",需要新增的话，新建date.pattern.properties文件，按"+getAlias()+".yyyy-MM-dd=yyyy-MM-dd,同时添加regular.yyyy-MM-dd=^\\\\\\\\d{4}-\\\\\\\\d{1,2}-\\\\\\\\d{1,2}$模式编写");
		}
		return db_pattern;
	}
	
	@Override
	public DBAlias getAlias() {
		// TODO Auto-generated method stub
		return DBAlias.oracle;
	}

	@Override
	public void addDateFormatStr(String java_pattern, String db_pattern) {
		// TODO Auto-generated method stub
		date_pattern_map.put(java_pattern, db_pattern);
	}

	@Override
	public DbColumn columnTypeToProertyType(String columnType) {
		// TODO Auto-generated method stub
		return null;
	}

}
