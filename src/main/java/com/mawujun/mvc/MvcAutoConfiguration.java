package com.mawujun.mvc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
//@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
public class MvcAutoConfiguration {

	@Bean
	private GlobalExceptionHandler globalExceptionHandler() {
		return new GlobalExceptionHandler();
	}
	
	 @ConditionalOnMissingBean(SpringContextUtils.class)
	 @Bean
	 public SpringContextUtils springContextUtils() {
		 return new SpringContextUtils();
	 }
}
