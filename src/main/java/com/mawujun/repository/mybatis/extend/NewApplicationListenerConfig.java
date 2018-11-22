package com.mawujun.repository.mybatis.extend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class NewApplicationListenerConfig {
	@Bean
    public NewApplicationListener newApplicationListener(){
        return new NewApplicationListener();
    }
}
