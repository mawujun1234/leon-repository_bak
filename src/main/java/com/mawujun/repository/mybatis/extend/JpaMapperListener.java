package com.mawujun.repository.mybatis.extend;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.mawujun.utils.ReflectUtils;
//@Component
public class JpaMapperListener {//implements ApplicationListener<ContextRefreshedEvent> {

	
	public ApplicationContext context;

//	@Override
//	public void onApplicationEvent(ContextRefreshedEvent event) {
//		
//		//ConfigurableApplicationContext context =event.getApplicationContext();
//		context=event.getApplicationContext();
//		
////		// TODO Auto-generated method stub
////		SqlSessionFactory  sqlSessionFactory=context.getBean(SqlSessionFactory.class);
////		if(sqlSessionFactory==null) {
////			return;
////		}
////		//首先获取Configuration，等初始化完后，然后反射
////		//替换MapperProxyFactory
////		//然后再替换MapperProxy
////		Configuration conf=sqlSessionFactory.getConfiguration();
////		//Collection<Class<?>> list=conf.getMapperRegistry().getMappers();
////		//System.out.println(list);
////		//MetaObject.forObject(object, objectFactory, objectWrapperFactory, reflectorFactory)
////		Map<Class<?>, MapperProxyFactory<?>> knownMappers=(Map<Class<?>, MapperProxyFactory<?>>)ReflectUtils.getFieldValue(conf.getMapperRegistry(), "knownMappers");
////		
////		for(Entry<Class<?>,MapperProxyFactory<?>> entry:knownMappers.entrySet()) {
////			knownMappers.put(entry.getKey(), new JpaMapperProxyFactory(entry.getValue().getMapperInterface()));
////		}
//
//
//	}

}
