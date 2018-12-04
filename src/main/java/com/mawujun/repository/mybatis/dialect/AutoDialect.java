/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.mawujun.repository.mybatis.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.MappedStatement;

import com.mawujun.repository.mybatis.interceptor.PageException;
import com.mawujun.utils.string.StringUtils;

/**
 * 自动选择数据库适配器
 *
 * @author mwj
 */
public class AutoDialect {

    private static Map<String, Class<? extends Dialect>> dialectAliasMap = new HashMap<String, Class<? extends Dialect>>();

    public static void registerDialectAlias(String alias, Class<? extends Dialect> dialectClass){
        dialectAliasMap.put(alias, dialectClass);
    }

    static {
        //注册别名
        registerDialectAlias("hsqldb", HsqlDialect.class);
        registerDialectAlias("h2", H2Dialect.class);
        registerDialectAlias("postgresql", HsqlDialect.class);
        registerDialectAlias("phoenix", HsqlDialect.class);

        registerDialectAlias("mysql", MySqlDialect.class);
        registerDialectAlias("mariadb", MySqlDialect.class);
        registerDialectAlias("sqlite", MySqlDialect.class);

        registerDialectAlias("oracle", OracleDialect.class);
        registerDialectAlias("db2", Db2Dialect.class);
        registerDialectAlias("informix", InformixDialect.class);
        //解决 informix-sqli #129，仍然保留上面的
        registerDialectAlias("informix-sqli", InformixDialect.class);

        registerDialectAlias("sqlserver", SqlServerDialect.class);
        registerDialectAlias("sqlserver2005", SqlServer2005Dialect.class);
        registerDialectAlias("sqlserver2012", SqlServer2012Dialect.class);

        registerDialectAlias("derby", SqlServer2012Dialect.class);
        //达梦数据库,https://github.com/mybatis-book/book/issues/43
        registerDialectAlias("dm", OracleDialect.class);
        //阿里云PPAS数据库,https://github.com/pagehelper/Mybatis-PageHelper/issues/281
        registerDialectAlias("edb", OracleDialect.class);
    }

    //自动获取dialect,如果没有setProperties或setSqlUtilConfig，也可以正常进行
    private boolean autoDialect = true;
    //多数据源时，获取jdbcurl后是否关闭数据源
    private boolean closeConn = true;
    //属性配置
    private Properties properties;
    //缓存
    private Map<String, AbstractDialect> urlDialectMap = new ConcurrentHashMap<String, AbstractDialect>();
    private ReentrantLock lock = new ReentrantLock();
    private AbstractDialect delegate;
//    private ThreadLocal<AbstractDialect> dialectThreadLocal = new ThreadLocal<AbstractDialect>();
//
//    //多数据动态获取时，每次需要初始化
//    public void initDelegateDialect(MappedStatement ms) {
//        if (delegate == null) {
//            if (autoDialect) {
//                this.delegate = getDialect(ms);
//            } else {
//                dialectThreadLocal.set(getDialect(ms));
//            }
//        }
//    }
//
//    //获取当前的代理对象
//    public AbstractDialect getDelegate() {
//        if (delegate != null) {
//            return delegate;
//        }
//        return dialectThreadLocal.get();
//    }
//
//    //移除代理对象
//    public void clearDelegate() {
//        dialectThreadLocal.remove();
//    }

    private String fromJdbcUrl(String jdbcUrl,DataSource dataSource) {
    	String dialect_name=null;
        for (String dialect : dialectAliasMap.keySet()) {
            if (jdbcUrl.indexOf(":" + dialect + ":") != -1) {
            	dialect_name= dialect;
            }
        }
        
        if("sqlserver".equals(dialect_name)) {
        	try {
				Connection dbConn =dataSource.getConnection();
				DatabaseMetaData dmd = dbConn.getMetaData();
				//https://www.cnblogs.com/SameZhao/p/6184924.html sql server的版本号
				int marjorVersion= dmd.getDatabaseMajorVersion();
				//11为 SQL SERVER 2012
				if(marjorVersion>=11) {
					return "sqlserver2012";
				} else if(marjorVersion>=9 && marjorVersion<11) {
					return "sqlserver2005";
				} else if(marjorVersion<9){
					return "sqlserver";
				}
				
				
//				System.out.println("当前数据库是：" + dmd.getDatabaseProductName());
//				System.out.println("当前主版本：" + dmd.getDatabaseMajorVersion());
//				System.out.println("当前次要版本：" + dmd.getDatabaseMinorVersion());
//				System.out.println("当前数据库版本：" + dmd.getDatabaseProductVersion());
//				System.out.println("当前数据库驱动：" + dmd.getDriverVersion());
//				System.out.println("当前数据库URL：" + dmd.getURL());
//				System.out.println("当前数据库是否是只读模式？：" + dmd.isReadOnly());
//				System.out.println("当前数据库是否支持批量更新？：" + dmd.supportsBatchUpdates());
//				System.out.println("当前数据库是否支持结果集的双向移动（数据库数据变动不在ResultSet体现）？：" + dmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
//				System.out.println("当前数据库是否支持结果集的双向移动（数据库数据变动会影响到ResultSet的内容）？：" + dmd.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));
//				System.out.println("========================================");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        return dialect_name;
    }

    /**
     * 反射类
     *
     * @param className
     * @return
     * @throws Exception
     */
    private Class resloveDialectClass(String className) throws Exception {
        if (dialectAliasMap.containsKey(className.toLowerCase())) {
            return dialectAliasMap.get(className.toLowerCase());
        } else {
            return Class.forName(className);
        }
    }

    /**
     * 初始化 helper
     *
     * @param dialectClass
     * @param properties
     */
    private AbstractDialect initDialect(String dialectClass, Properties properties) {
        AbstractDialect dialect;
        if (StringUtils.isEmpty(dialectClass)) {
            throw new PageException("使用分页插件时，必须设置 helper 属性");
        }
        try {
            Class sqlDialectClass = resloveDialectClass(dialectClass);
            if (AbstractDialect.class.isAssignableFrom(sqlDialectClass)) {
                dialect = (AbstractDialect) sqlDialectClass.newInstance();
            } else {
                throw new PageException("使用分页插件 时，方言必须是实现 " + AbstractDialect.class.getCanonicalName() + " 接口的实现类!");
            }
        } catch (Exception e) {
            throw new PageException("初始化 helper [" + dialectClass + "]时出错:" + e.getMessage(), e);
        }
        //dialect.setProperties(properties);
        return dialect;
    }

    /**
     * 获取url
     *
     * @param dataSource
     * @return
     */
    private String getUrl(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return conn.getMetaData().getURL();
        } catch (SQLException e) {
            throw new PageException(e);
        } finally {
            if (conn != null) {
                try {
                    if (closeConn) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    //ignore
                }
            }
        }
    }

    /**
     * 根据 jdbcUrl 获取数据库方言
     *
     * @param ms
     * @return
     */
    public AbstractDialect getDialect(MappedStatement ms) {
        //改为对dataSource做缓存
        DataSource dataSource = ms.getConfiguration().getEnvironment().getDataSource();
        String url = getUrl(dataSource);
        if (urlDialectMap.containsKey(url)) {
            return urlDialectMap.get(url);
        }
        try {
            lock.lock();
            if (urlDialectMap.containsKey(url)) {
                return urlDialectMap.get(url);
            }
            if (StringUtils.isEmpty(url)) {
                throw new PageException("无法自动获取jdbcUrl，请在分页插件中配置dialect参数!");
            }
            String dialectStr = fromJdbcUrl(url,dataSource);
            if (dialectStr == null) {
                throw new PageException("无法自动获取数据库类型，请通过leon.mybatis.dialect 参数指定!");
            }
            AbstractDialect dialect = initDialect(dialectStr, properties);
            urlDialectMap.put(url, dialect);
            return dialect;
        } finally {
            lock.unlock();
        }
    }

//    public void setProperties(Properties properties) {
//        //多数据源时，获取 jdbcurl 后是否关闭数据源
//        String closeConn = properties.getProperty("closeConn");
//        if (StringUtils.isNotEmpty(closeConn)) {
//            this.closeConn = Boolean.parseBoolean(closeConn);
//        }
//        //使用 sqlserver2012 作为默认分页方式，这种情况在动态数据源时方便使用
//        String useSqlserver2012 = properties.getProperty("useSqlserver2012");
//        if (StringUtils.isNotEmpty(useSqlserver2012) && Boolean.parseBoolean(useSqlserver2012)) {
//            registerDialectAlias("sqlserver", SqlServer2012Dialect.class);
//            registerDialectAlias("sqlserver2008", SqlServerDialect.class);
//        }
//        String dialectAlias = properties.getProperty("dialectAlias");
//        if (StringUtils.isNotEmpty(dialectAlias)) {
//            String[] alias = dialectAlias.split(";");
//            for (int i = 0; i < alias.length; i++) {
//                String[] kv = alias[i].split("=");
//                if(kv.length != 2){
//                    throw new IllegalArgumentException("dialectAlias 参数配置错误，" +
//                            "请按照 alias1=xx.dialectClass;alias2=dialectClass2 的形式进行配置!");
//                }
//                for (int j = 0; j < kv.length; j++) {
//                    try {
//                        Class<? extends Dialect> diallectClass = (Class<? extends Dialect>) Class.forName(kv[1]);
//                        //允许覆盖已有的实现
//                        registerDialectAlias(kv[0], diallectClass);
//                    } catch (ClassNotFoundException e) {
//                        throw new IllegalArgumentException("请确保 dialectAlias 配置的 Dialect 实现类存在!", e);
//                    }
//                }
//            }
//        }
//        //指定的 Helper 数据库方言，和  不同
//        String dialect = properties.getProperty("helperDialect");
//        //运行时获取数据源
//        String runtimeDialect = properties.getProperty("autoRuntimeDialect");
//        //1.动态多数据源
//        if (StringUtils.isNotEmpty(runtimeDialect) && "TRUE".equalsIgnoreCase(runtimeDialect)) {
//            this.autoDialect = false;
//            this.properties = properties;
//        }
//        //2.动态获取方言
//        else if (StringUtils.isEmpty(dialect)) {
//            autoDialect = true;
//            this.properties = properties;
//        }
//        //3.指定方言
//        else {
//            autoDialect = false;
//            this.delegate = initDialect(dialect, properties);
//        }
//    }
}
