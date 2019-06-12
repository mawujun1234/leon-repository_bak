package com.mawujun.repository.mybatis.interceptor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import com.mawujun.exception.BizException;
import com.mawujun.mvc.SpringContextUtils;
import com.mawujun.repository.mybatis.dialect.AutoDialect;
import com.mawujun.repository.mybatis.dialect.Dialect;
import com.mawujun.repository.utils.Cnd;
import com.mawujun.repository.utils.Page;
import com.mawujun.repository.utils.PageMethodCache;
import com.mawujun.util.StringUtil;

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
            	if(StringUtil.hasText(dialect_classname)) {
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
            
//            //这里返回的是Execute的返回值，而不是定义的接口的返回值。，所以不能再这里根据返回值来进行判断 是否分页
//            Class return_clazz=invocation.getMethod().getReturnType();
//            System.out.println(return_clazz+"==============================");
//            //只要返回值是Page，就表示进行分页查询
//            if(!Page.class.isAssignableFrom(return_clazz)) {
//            	return invocation.proceed();
//            }
            
            if(!PageMethodCache.exits(ms.getId())) {
            	//test.mawujun.jpa.JpaMybatisMapper.listPageByMybatis
            	return invocation.proceed();
            }
            
            
            
            Object parameter = args[1];
            
            //判断是否分页
            boolean isPageCondition=false;
            //因为现在只支持参数为Condition的分页方式
            Cnd condition=null;
            //分页的returntype必须是Page，就表示这个查询要使用分页查询
            
            
            if(parameter instanceof Map) {
            	//如果只有一个参数，并且参数是Condition
            	if(parameter instanceof Cnd) {
            		condition=(Cnd)parameter;
            		
            		
            		args[1]=condition.getParams();
        			//parameter=args[1];
            	} else {
            		//参数可能是随机参数，或者是Condition和其他参数的混合体
            		condition=Cnd.of((Map)parameter);
            		args[1]=condition.getParams();
        			//parameter=args[1];
            	}
            }


            //如果不是分页就直接返回
            isPageCondition=condition.isPageCondition();
            if(!isPageCondition) {
            	throw new BizException("分页查询必须具有start或page和limit名称的参数，用于分页");
            	//return invocation.proceed();
            }
            //因为参数可能是实体，如果是实体参数的话，其他指定参数能不能 用注意下
            parameter=condition.getParams();
            
            Page pageinfo=new Page();
            pageinfo.setStart(condition.getStart());
            pageinfo.setLimit(condition.getLimit());
            pageinfo.setPage(condition.getPage());
            
            //RowBounds rowBounds = (RowBounds) args[2];
            RowBounds rowBounds = new RowBounds(condition.getStart(),condition.getLimit());
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
            Long count = count(executor, ms, args[1], rowBounds, resultHandler, boundSql,condition);
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
            BoundSql boundSql,Cnd condition) throws SQLException {
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
			count = ExecutorUtil.executeAutoCount(dialect, executor, countMs, parameter, boundSql,condition, rowBounds, resultHandler);
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