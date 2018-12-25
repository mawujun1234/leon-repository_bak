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

/**
 * 长整形的id生成器
 * https://blog.csdn.net/u014042146/article/details/52595624 生成的时候，还可以传递参数
 * @author admin
 *
 */
public class LongIdGenerator implements IdentifierGenerator,Configurable{
   
    

    public static LongIdUtils idWorker;
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		if(idWorker==null) {
    		idWorker = new LongIdUtils();
    	}
    	  
    	 return  idWorker.nextId();
	}
	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		// TODO Auto-generated method stub
		
	}
}
