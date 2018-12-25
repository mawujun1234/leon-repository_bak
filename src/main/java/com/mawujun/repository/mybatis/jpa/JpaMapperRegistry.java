package com.mawujun.repository.mybatis.jpa;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.session.Configuration;

public class JpaMapperRegistry extends MapperRegistry {
	private  Map<Class<?>, MapperProxyFactory<?>> knownMappers_ = new HashMap<Class<?>, MapperProxyFactory<?>>();
	public Map<Class<?>, MapperProxyFactory<?>> getKnownMappers_() {
		return knownMappers_;
	}


	public void setKnownMappers_(Map<Class<?>, MapperProxyFactory<?>> knownMappers_) {
		this.knownMappers_ = knownMappers_;
	}


	private  Configuration config_;

	public JpaMapperRegistry(Configuration config) {
		super(config);
		this.config_=config;
		// TODO Auto-generated constructor stub
	}
	
	
	public <T> void addMapper(Class<T> type) {
		if (type.isInterface()) {
			if (hasMapper(type)) {
				throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
			}
			boolean loadCompleted = false;
			try {
				knownMappers_.put(type, new JpaMapperProxyFactory<T>(type));
				// It's important that the type is added before the parser is run
				// otherwise the binding may automatically be attempted by the
				// mapper parser. If the type is already known, it won't try.
				MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config_, type);
				parser.parse();
				loadCompleted = true;
			} finally {
				if (!loadCompleted) {
					knownMappers_.remove(type);
				}
			}
		}
	}

}
