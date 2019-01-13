package com.mawujun.repository.mybatis.jpa;

import javax.annotation.PostConstruct;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.mawujun.mvc.SpringContextUtils;

//@org.springframework.context.annotation.Configuration
//@AutoConfigureBefore(MybatisAutoConfiguration.class)
public class TypeAliasesAutoConfiguration {
	@PostConstruct
	public void onApplicationEvent() {
		//MybatisProperties tp=springContextUtils().getBean(MybatisProperties.class);
		String aaaa = springContextUtils().getEnvironment().getProperty("mybatis.type-aliases-package");
		//System.out.println(aaaa);
		// System.out.println();
		if(aaaa!=null && !"".equals(aaaa)) {
			System.setProperty("mybatis.type-aliases-package", aaaa + ";com.mawujun.repository.mybatis.typeAliases");
		} else {
			System.setProperty("mybatis.type-aliases-package", "com.mawujun.repository.mybatis.typeAliases");
		}
		
		//System.out.println(springContextUtils().getEnvironment().getProperty("mybatis.type-aliases-package"));
		//System.out.println("===========================================================");
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
