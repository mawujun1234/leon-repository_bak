package com.mawujun.repository.mybatis.interceptor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.mawujun.mvc.SpringContextUtils;
import com.mawujun.repository.mybatis.dialect.AutoDialect;
import com.mawujun.repository.mybatis.dialect.Dialect;
import com.mawujun.repository.utils.PageInfo;
import com.mawujun.utils.string.StringUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class PageInfoInterceptor implements Interceptor {
    private volatile Dialect dialect;
    //private String countSuffix = MSUtils.countSuffix;
    //protected Cache<String, MappedStatement> msCountMap = null;
    //private String default_dialect_class = "com.github.pagehelper.PageHelper";
    
    private AutoDialect autoDialect=new AutoDialect();
    protected static Map<String, MappedStatement> msCountMap = new HashMap<String, MappedStatement>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
    	
        //try {
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            if(dialect==null) {	
            	String dialect_classname=SpringContextUtils.getEnvironment().getProperty("leon.mybatis.dialect");
            	if(StringUtils.hasText(dialect_classname)) {
					try {
						Class<?> aClass = Class.forName(dialect_classname);
						dialect = (Dialect) aClass.newInstance();
					} catch (Exception e) {
						throw new PageException(e);
					}
            	} else {
            		dialect=autoDialect.getDialect(ms);
            	}
//            	//dialect=new SqlServer2012Dialect();

        	}
            Object parameter = args[1];
            
            //判断是否分页
            boolean isPageInfo=false;
            PageInfo pageinfo=null;
            if(parameter instanceof Map) {
            	Collection collections=((Map)parameter).values();
            	Iterator iterator=collections.iterator();
            	while(iterator.hasNext()) {
            		Object o=iterator.next();
            		if(o instanceof PageInfo) {
            			isPageInfo=true;
            			pageinfo=(PageInfo)o;
            			args[1]=pageinfo.getParams();
            			parameter=args[1];
            			break;
            		}
            	}
            }

            //如果不是分页就直接返回
            if(!isPageInfo) {
            	return invocation.proceed();
            }

            //RowBounds rowBounds = (RowBounds) args[2];
            RowBounds rowBounds = new RowBounds(pageinfo.getStart(),pageinfo.getLimit());
            args[2]=rowBounds;
            ResultHandler resultHandler = (ResultHandler) args[3];
            Executor executor = (Executor) invocation.getTarget();
            CacheKey cacheKey;
            BoundSql boundSql;
            //由于逻辑关系，只会进入一次
            if (args.length == 4) {
                //4 个参数时
                boundSql = ms.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else {
                //6 个参数时
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }
           
            //查询总数,必须放在前面
            Long count = count(executor, ms, args[1], rowBounds, resultHandler, boundSql,pageinfo);
            pageinfo.setTotal(count.intValue());
            if(count==0) {
            	return pageinfo;
            }
            
            //List list=(List)invocation.proceed();
            List list = ExecutorUtil.pageQuery(dialect, executor,
                    ms, parameter, rowBounds, resultHandler, boundSql, cacheKey);
            pageinfo.setRoot(list);
            
            return pageinfo;
    }



    private Long count(Executor executor, MappedStatement ms, Object parameter,
            RowBounds rowBounds, ResultHandler resultHandler,
            BoundSql boundSql,PageInfo pageinfo) throws SQLException {
		String countMsId = ms.getId() + MSUtils.countSuffix;
		Long count;
		// 先判断是否存在手写的 count 查询
		MappedStatement countMs = ExecutorUtil.getExistedMappedStatement(ms.getConfiguration(), countMsId);
		if (countMs != null) {
			count = ExecutorUtil.executeManualCount(executor, countMs, parameter, boundSql, resultHandler);
		} else {
			countMs = msCountMap.get(countMsId);
			// 自动创建
			if (countMs == null) {
				// 根据当前的 ms 创建一个返回值为 Long 类型的 ms
				countMs = MSUtils.newCountMappedStatement(ms, countMsId);
				msCountMap.put(countMsId, countMs);
			}
			count = ExecutorUtil.executeAutoCount(dialect, executor, countMs, parameter, boundSql,pageinfo, rowBounds, resultHandler);
		}
		return count;
	}

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
       
    }

}