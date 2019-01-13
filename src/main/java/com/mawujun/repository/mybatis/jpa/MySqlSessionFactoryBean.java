package com.mawujun.repository.mybatis.jpa;

import java.lang.reflect.Field;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

public class MySqlSessionFactoryBean extends SqlSessionFactoryBean {
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		SqlSessionFactory sqlSessionFactory=super.getObject();
		Configuration configuration=sqlSessionFactory.getConfiguration();
		
		JpaMapperRegistry mapperRegistry=new JpaMapperRegistry(configuration);
		Field field = configuration.getClass().getDeclaredField("mapperRegistry");
		field.setAccessible(true);
		field.set(configuration, mapperRegistry);
		
		
	  }
}
