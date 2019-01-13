package com.mawujun.mvc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * spring context帮助类
 * @author mawujun 16064988
 *
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
	public static ApplicationContext applicationContext; 

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		if(applicationContext==null) {
			throw new NullPointerException("applicationContext还未初始化");
		}
		return applicationContext;
	}
	
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
	public static <T> T getBean( Class<T> requiredType) {
		return getApplicationContext().getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return getApplicationContext().getBean(name, requiredType);
	}

	public static boolean containsBean(String name) {
		return getApplicationContext().containsBean(name);
	}

	public static boolean isSingleton(String name) {
		return getApplicationContext().isSingleton(name);
	}

	public static Class<? extends Object> getType(String name) {
		return getApplicationContext().getType(name);
	}
	
	public static Environment getEnvironment() {
		return getApplicationContext().getEnvironment();
	}
	
	public static String getActiveProfile() {
		return getApplicationContext().getEnvironment().getActiveProfiles()[0];
	}

}