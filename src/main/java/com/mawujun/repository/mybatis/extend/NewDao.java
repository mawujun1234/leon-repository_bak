package com.mawujun.repository.mybatis.extend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.mawujun.utils.ConvertUtils;

@Repository
public class NewDao  {
	@PersistenceContext
	private EntityManager em;
	
	public void create(Object t) {
		em.persist(t);
	}
	
	public void createBatch(List<Object> list) {
		for (int i = 0; i < list.size(); i++) {
			em.persist(list.get(i));
			if (i % 30 == 0) {
				em.flush();
				em.clear();
			}
		}
	}
	
	public void createBatch(Object... list) {
		for (int i = 0; i < list.length; i++) {
			em.persist(list[i]);
			if (i % 30 == 0) {
				em.flush();
				em.clear();
			}
		}
	}
	
	public Object getById(Class entityClass,Object id) {
		return em.find(entityClass, id);
	}
	
	protected
	
	public Object getByMap(Class entityClass,Map<String,Object> params) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery query = criteriaBuilder.createQuery(entityClass);
		Root itemRoot = query.from(entityClass);
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		for(Entry<String,Object> param:params.entrySet()) {   
			Class javatype=itemRoot.get(param.getKey()).getJavaType();
			predicatesList.add(criteriaBuilder.equal(itemRoot.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
		}
		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
        TypedQuery typedQuery = em.createQuery(query);
        //List resultList = typedQuery.getResultList();
        return typedQuery.getSingleResult();
		
	}
	
	public Object getByEntity(Class entityClass,Object params) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery query = criteriaBuilder.createQuery(entityClass);
		criteriaBuilder.a
	}
	

}
