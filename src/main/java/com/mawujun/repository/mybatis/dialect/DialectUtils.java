package com.mawujun.repository.mybatis.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.mawujun.repository.mybatis.interceptor.PageException;
import com.mawujun.utils.string.StringUtils;

public class DialectUtils {
    public static  DBAlias getDialect_name(Connection conn) {
    	//String dialect_name=null;
    	DBAlias dbAlias_result=null;
    	try {
			String jdbcUrl=conn.getMetaData().getURL();
	        if (StringUtils.isEmpty(jdbcUrl)) {
	          throw new PageException("无法自动获取jdbcUrl，请在分页插件中配置leon.mybatis.dialect参数!");
	        }
			
//	        for (String dialect : dialectAliasMap.keySet()) {
//	            if (jdbcUrl.indexOf(":" + dialect + ":") != -1) {
//	            	dialect_name= dialect;
//	            }
//	        }
	        for(DBAlias dbAlias:DBAlias.values()) {
	        	 if (jdbcUrl.indexOf(":" + dbAlias.toString().replace("_", "-") + ":") != -1) {
		            	//dialect_name= dbAlias.name();;
	        		 	dbAlias_result=dbAlias;
		            }
	        }
	        
	        if(DBAlias.sqlserver==dbAlias_result) {
					//Connection dbConn =dataSource.getConnection();
					DatabaseMetaData dmd = conn.getMetaData();
					//https://www.cnblogs.com/SameZhao/p/6184924.html sql server的版本号
					int marjorVersion= dmd.getDatabaseMajorVersion();
					//11为 SQL SERVER 2012
					if(marjorVersion>=11) {
						//dialect_name= "sqlserver2012";
						dbAlias_result=DBAlias.sqlserver2012;
					} else if(marjorVersion>=9 && marjorVersion<11) {
						//dialect_name= "sqlserver2005";
						dbAlias_result=DBAlias.sqlserver2005;
					} else if(marjorVersion<9){
						//dialect_name= "sqlserver";
						dbAlias_result=DBAlias.sqlserver;
					}			
//					System.out.println("当前数据库是：" + dmd.getDatabaseProductName());
//					System.out.println("当前主版本：" + dmd.getDatabaseMajorVersion());
//					System.out.println("当前次要版本：" + dmd.getDatabaseMinorVersion());
//					System.out.println("当前数据库版本：" + dmd.getDatabaseProductVersion());
//					System.out.println("当前数据库驱动：" + dmd.getDriverVersion());
//					System.out.println("当前数据库URL：" + dmd.getURL());
//					System.out.println("当前数据库是否是只读模式？：" + dmd.isReadOnly());
//					System.out.println("当前数据库是否支持批量更新？：" + dmd.supportsBatchUpdates());
//					System.out.println("当前数据库是否支持结果集的双向移动（数据库数据变动不在ResultSet体现）？：" + dmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
//					System.out.println("当前数据库是否支持结果集的双向移动（数据库数据变动会影响到ResultSet的内容）？：" + dmd.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));
//					System.out.println("========================================");
	        
	        } 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
       if (dbAlias_result == null) {
	      throw new PageException("无法自动获取数据库类型，请通过leon.mybatis.dialect 参数指定!");
	   }
       return dbAlias_result;
    	
    }
}
