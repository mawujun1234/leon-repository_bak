package com.mawujun.mvc;

import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
//@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
public class MvcAutoConfiguration {

	@Bean
	private GlobalExceptionHandler globalExceptionHandler() {
		return new GlobalExceptionHandler();
	}
}
