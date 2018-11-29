package com.mawujun.repository.mybatis.extend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class JpaMapperListenerConfig {
	@Bean
    public JpaMapperListener newApplicationListener(){
        return new JpaMapperListener();
    }
}
