package com.mawujun.repository.identity.generator;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class SnowFlakeGenerator implements IdentifierGenerator,Configurable{
   
    

    public static SnowFlakeUtils idWorker;
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		if(idWorker==null) {
    		idWorker = new SnowFlakeUtils(1, 1);
    	}
    	  
    	 return  idWorker.nextId();
	}
	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		// TODO Auto-generated method stub
		
	}
}
