package com.mawujun.repository.mybatis.jpa;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.mawujun.mvc.SpringContextUtils;
import com.mawujun.utils.ReflectionUtils;

@org.springframework.context.annotation.Configuration
//@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
//@ConditionalOnBean(DataSource.class)
//@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class JpaMybatisAutoConfiguration {
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	//@Bean
	 @PostConstruct
	public void onApplicationEvent() {
		
//		//ConfigurableApplicationContext context =event.getApplicationContext();
//		context=event.getApplicationContext();
//		
//		// TODO Auto-generated method stub
//		SqlSessionFactory  sqlSessionFactory=context.getBean(SqlSessionFactory.class);
//		if(sqlSessionFactory==null) {
//			return;
//		}
		//首先获取Configuration，等初始化完后，然后反射
		//替换MapperProxyFactory
		//然后再替换MapperProxy
		Configuration conf=sqlSessionFactory.getConfiguration();
		MapperRegistry mapperRegistry=conf.getMapperRegistry();
		//Map<Class<?>, MapperProxyFactory<?>> knownMappers=(Map<Class<?>, MapperProxyFactory<?>>)ReflectUtils.getFieldValue(mapperRegistry, "knownMappers");
		Map<Class<?>, MapperProxyFactory<?>> knownMappers_ =(Map<Class<?>, MapperProxyFactory<?>>)ReflectionUtils.getFieldValue(mapperRegistry, "knownMappers");
				
		
		JpaMapperRegistry jpaMapperRegistry=new JpaMapperRegistry(conf);
		jpaMapperRegistry.setKnownMappers_(knownMappers_);
		ReflectionUtils.setFieldValue(jpaMapperRegistry, "knownMappers", knownMappers_);
		//BeanUtils.copyProperties(mapperRegistry, jpaMapperRegistry);
		
		ReflectionUtils.setFieldValue(conf, "mapperRegistry", jpaMapperRegistry);
		//Collection<Class<?>> list=conf.getMapperRegistry().getMappers();
		//System.out.println(list);
		//MetaObject.forObject(object, objectFactory, objectWrapperFactory, reflectorFactory)
		
		
		for(Entry<Class<?>,MapperProxyFactory<?>> entry:knownMappers_.entrySet()) {
			knownMappers_.put(entry.getKey(), new JpaMapperProxyFactory(entry.getValue().getMapperInterface()));
		}

	}
	 
	 @ConditionalOnMissingBean(JpaDao.class)
	 @Bean
	 public JpaDao jpaDao() {
		 return new JpaDao();
	 }
	 
	 @ConditionalOnMissingBean(SpringContextUtils.class)
	 @Bean
	 public SpringContextUtils springContextUtils() {
		 return new SpringContextUtils();
	 }
	

}
