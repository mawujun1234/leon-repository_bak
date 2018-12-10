package com.mawujun.repository.mybatis.extend;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;

import com.mawujun.utils.ReflectUtils;

@org.springframework.context.annotation.Configuration
//@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
//@ConditionalOnBean(DataSource.class)
//@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MybatisAutoConfiguration {
	
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
		//Collection<Class<?>> list=conf.getMapperRegistry().getMappers();
		//System.out.println(list);
		//MetaObject.forObject(object, objectFactory, objectWrapperFactory, reflectorFactory)
		Map<Class<?>, MapperProxyFactory<?>> knownMappers=(Map<Class<?>, MapperProxyFactory<?>>)ReflectUtils.getFieldValue(conf.getMapperRegistry(), "knownMappers");
		
		for(Entry<Class<?>,MapperProxyFactory<?>> entry:knownMappers.entrySet()) {
			knownMappers.put(entry.getKey(), new JpaMapperProxyFactory(entry.getValue().getMapperInterface()));
		}

//	
//		CityMapper cityMapper=context.getBean(CityMapper.class);
//		System.out.println(cityMapper.getById("111"));
	}
	
	

}
