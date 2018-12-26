package com.mawujun.repository.mybatis.jpa;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.boot.internal.SessionFactoryOptionsBuilder;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

@org.springframework.context.annotation.Configuration
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
public class HibernateAutoConfiguration {
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@PostConstruct
	private void aaa() {
		LogicDeleteInterceptor bbb = new LogicDeleteInterceptor();

		EntityManager em = entityManagerFactory.createEntityManager();
		Session session = (Session) em.getDelegate();
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		sessionFactory.withOptions().interceptor(bbb);//.openSession();
		
		//
		if(sessionFactory.getSessionFactoryOptions() instanceof SessionFactoryOptionsBuilder) {
			((SessionFactoryOptionsBuilder)sessionFactory.getSessionFactoryOptions()).applyInterceptor(bbb);
		}
		//.getInterceptor();//
		//System.out.println("-----------------------");
		bbb.searchLogiceleteField(sessionFactory);
		
	}
}
