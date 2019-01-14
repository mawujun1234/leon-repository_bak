package com.mawujun.repository.mybatis.interceptor;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.mawujun.repository.mybatis.jpa.JpaMybatisAutoConfiguration;

@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ PageInfoInterceptor.class })
@AutoConfigureBefore(JpaMybatisAutoConfiguration.class)
public class PageConfiguration {
	@ConditionalOnMissingBean(PageInfoInterceptor.class)
	@Bean
	public PageInfoInterceptor pageInfoInterceptor() {
		return new PageInfoInterceptor();
	}

}
