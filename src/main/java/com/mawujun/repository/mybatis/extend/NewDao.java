package com.mawujun.repository.mybatis.extend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.mawujun.exception.BusinessException;
import com.mawujun.repository.utils.PageInfo;
import com.mawujun.utils.CollectionUtils;
import com.mawujun.utils.ConvertUtils;

@Repository
public class NewDao  {
	private static final Logger logger=LoggerFactory.getLogger(NewDao.class);
	@PersistenceContext
	private EntityManager em;
	
	private Map<Class,SimpleJpaRepository> repositoryCache=new HashMap<Class,SimpleJpaRepository>();
	
	public SimpleJpaRepository getSimpleJpaRepository(Class entityClass) {
		SimpleJpaRepository repository=new SimpleJpaRepository(entityClass,em);
		repositoryCache.put(entityClass, repository);
		return repository;
	}
	
	public Object create(Class entityClass,Object entity) {
		//1:判断对象是否创建成功，通过判断对象的状态，是持久态，还是有态来进行判断，然后返回是0还是1
		//2:批量更新是使用jpql还是条件查询
		
		return getSimpleJpaRepository(entityClass).saveAndFlush(entity);
		//em.persist(t);
	}
	
	public void createBatch(Class entityClass,List<Object> list) {
		SimpleJpaRepository aaa=getSimpleJpaRepository(entityClass);
		aaa.saveAll(list);
		aaa.flush();
//		for (int i = 0; i < list.size(); i++) {
//			em.persist(list.get(i));
//			if (i % 30 == 0) {
//				em.flush();
//				em.clear();
//			}
//		}
	}
	
	public void createBatch(Class entityClass,Object... list) {
		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		repository.saveAll(CollectionUtils.arrayToList(list));
		repository.flush();
//		for (int i = 0; i < list.length; i++) {
//			em.persist(list[i]);
//			if (i % 30 == 0) {
//				em.flush();
//				em.clear();
//			}
//		}
	}
	
	public Object getById(Class entityClass,Object id) {
		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		Optional optional= repository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}
		
		//return em.find(entityClass, id);
	}
	
	
	
	public Object getByMap(Class entityClass,Map<String,Object> params) {
//		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//		CriteriaQuery query = criteriaBuilder.createQuery(entityClass);
//		Root itemRoot = query.from(entityClass);
//		List<Predicate> predicatesList = new ArrayList<Predicate>();
//		for(Entry<String,Object> param:params.entrySet()) {   
//			Class javatype=itemRoot.get(param.getKey()).getJavaType();
//			predicatesList.add(criteriaBuilder.equal(itemRoot.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
//		}
//		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
//        TypedQuery typedQuery = em.createQuery(query);
		TypedQuery typedQuery=genTypedQuery(entityClass,params);
        //List resultList = typedQuery.getResultList();
        return typedQuery.getSingleResult();
		
	}
	
	
	public Object getByExample(Class entityClass,Object params) {
		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher); 	
		Optional option=repository.findOne(example);
		if(option.isPresent()) {
			return option.get();
		} else {
			return null;
		}
		
//		ExampleMatcher matcher = ExampleMatcher.matching();
//		Example example = Example.of(params, matcher); 		
//		Optional option=findOne(example);
//		if(option.isPresent()) {
//			return option.get();
//		} else {
//			return null;
//		}
	}
	public Object existsByExample(Class entityClass,Object params) {
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher); 	
		return getSimpleJpaRepository(entityClass).exists(example);
		//return exists(example);
	}
	public long countByExample(Class entityClass,Object params) {
		if(params==null) {
			return getSimpleJpaRepository(entityClass).count();
		}
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher); 	
		return getSimpleJpaRepository(entityClass).count(example);
		//return count(example);
	}
	
	public List listByExample(Class entityClass,Object params) {
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher); 	
		return getSimpleJpaRepository(entityClass).findAll(example);
		//return findAll(example);
	}
	
//	public PageInfo listPageByExample(Class entityClass,Object params, PageInfo pageinfo) {
//		ExampleMatcher matcher = ExampleMatcher.matching();
//		Example example = Example.of(params, matcher); 	
//		Pageable pageable=PageRequest.of(pageinfo.getPage(), pageinfo.getLimit());
//		Page page=getSimpleJpaRepository(entityClass).findAll(example, pageable);
//		pageinfo.setTotal((int)page.getTotalElements());
//		pageinfo.setRoot(page.getContent());
//		return pageinfo;
//	}
	public PageInfo listPageByExample(Class entityClass,Object params, int pageIndex,int limit) {
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher); 	
		Pageable pageable=PageRequest.of(pageIndex, limit);
		Page page=getSimpleJpaRepository(entityClass).findAll(example, pageable);
		
		PageInfo pageinfo=new PageInfo();
		pageinfo.setPage(pageIndex);
		pageinfo.setLimit(limit);
		pageinfo.setTotal((int)page.getTotalElements());
		pageinfo.setRoot(page.getContent());
		pageinfo.setParams(params);
		return pageinfo;
	}
	
	
	private TypedQuery genTypedQuery(Class entityClass,Map<String,Object> params) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery query = criteriaBuilder.createQuery(entityClass);
		Root itemRoot = query.from(entityClass);
		//List<Predicate> predicatesList = new ArrayList<Predicate>();
		Predicate[] predicatesList=new Predicate[params.size()];
		int i=0;
		for(Entry<String,Object> param:params.entrySet()) {   
			Class javatype=itemRoot.get(param.getKey()).getJavaType();
			predicatesList[i]=criteriaBuilder.equal(itemRoot.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype));
			i++;
			//predicatesList.add(criteriaBuilder.equal(itemRoot.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
		}
		query.where(predicatesList);
        TypedQuery typedQuery = em.createQuery(query);
        return typedQuery;
	}
	public List listByMap(Class entityClass,Map<String,Object> params) {	
		if(params==null) {
			return this.listAll(entityClass);
		}
		TypedQuery typedQuery=genTypedQuery(entityClass,params);
        //List resultList = typedQuery.getResultList();
        return typedQuery.getResultList();
		
	}
	
	public List listAll(Class entityClass) {
		return getSimpleJpaRepository(entityClass).findAll();
	}
	
//	public PageInfo listPageByMap(Class entityClass,Map<String,Object> params, PageInfo pageinfo) {
//		ExampleMatcher matcher = ExampleMatcher.matching();
//		Example example = Example.of(params, matcher); 	
//		Pageable pageable=PageRequest.of(pageinfo.getPage(), pageinfo.getLimit());
//		Page page=getSimpleJpaRepository(entityClass).findAll(example, pageable);
//		pageinfo.setTotal((int)page.getTotalElements());
//		pageinfo.setRoot(page.getContent());
//		return pageinfo;
//	}
	
	private static class PageSpecification<T> implements Specification<T> {
		private  Map<String,Object> params;
		
		public PageSpecification(Map<String, Object> params) {
			super();
			this.params = params;
		}

		@Override
		public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
			//List<Predicate> predicatesList = new ArrayList<Predicate>();
			Predicate[] predicatesList=new Predicate[0];
			if(params!=null) {
				predicatesList=new Predicate[params.size()];
				int i=0;
				for(Entry<String,Object> param:params.entrySet()) {   
					Class javatype=root.get(param.getKey()).getJavaType();
					predicatesList[i]=cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype));
					i++;
					//predicatesList.add(cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
				}
				
			} 
			Predicate p =cb.and(predicatesList);
			return p;

		}
	}
	public PageInfo listPageByMap(Class entityClass,Map<String,Object> params, int pageIndex,int limit) {
		Pageable pageable = PageRequest.of(pageIndex, limit);
//		Specification spec = new Specification() { // 查询条件构造
//			@Override
//			public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
//				//List<Predicate> predicatesList = new ArrayList<Predicate>();
//				Predicate[] predicatesList=new Predicate[0];
//				if(params!=null) {
//					predicatesList=new Predicate[params.size()];
//					int i=0;
//					for(Entry<String,Object> param:params.entrySet()) {   
//						Class javatype=root.get(param.getKey()).getJavaType();
//						predicatesList[i]=cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype));
//						i++;
//						//predicatesList.add(cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
//					}
//					
//				} 
//				Predicate p =cb.and(predicatesList);
//				return p;
//			}
//
//		};   
		PageSpecification spec=new PageSpecification(params);
		Page page=getSimpleJpaRepository(entityClass).findAll(spec, pageable);
		
		PageInfo pageinfo=new PageInfo();
		pageinfo.setPage(pageIndex);
		pageinfo.setLimit(limit);
		pageinfo.setTotal((int)page.getTotalElements());
		pageinfo.setRoot(page.getContent());
		pageinfo.setParams(params);
		
		return pageinfo;
	}
	
	
	public Object update(Class entityClass,Object entity) {
		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		return repository.saveAndFlush(entity);
	}
	
	public void updateByMap(Class entityClass,Map<String,Object> params) {
		if(params.get("id")==null || "".equals(params.get("id"))) {
			throw new BusinessException("id不能为空!");
		}
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate update = cb.createCriteriaUpdate(entityClass);
		Root root = update.from(entityClass);
		//List<Predicate> predicatesList = new ArrayList<Predicate>();
		Predicate[] predicatesList=new Predicate[params.size()];
		int i=0;
		for(Entry<String,Object> param:params.entrySet()) {   
			Class javatype=root.get(param.getKey()).getJavaType();
			predicatesList[i]=cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype));
			i++;
			//predicatesList.add(cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
		}
		update.where(predicatesList);
		//repository.flush();
	}
	
	public int remove(Class entityClass,Object entity) {
		try {
		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		repository.delete(entity);
		repository.flush();
		} catch(Exception e) {
			logger.error("remove失败,entity="+entity,e);
			return 0;
		}
		return 1;
	}
	
	public void removeByMap(Class entityClass,Map<String,Object> params) {
		//这里返回影响的函数，还没设置
		throw new BusinessException("还未开发");
		//SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		//repository.flush();
	}
	
	public int removeById(Class entityClass,Object id) {
		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		try {
			repository.deleteById(id);
			repository.flush();
		} catch(Exception e) {
			logger.error("removeById失败,id="+id,e);
			return 0;
		}
		return 1;
		
	}
	
	public int removeByIds(Class entityClass,Object[] ids) {
		int result=0;
		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		for(Object id:ids) {
			result+=removeById(entityClass,id);
		}
		return result;
	}
	
//	//============================example相关的
//	public  List findAll(Example example) {
//		return getQuery(new ExampleSpecification(example), example.getProbeType(), Sort.unsorted()).getResultList();
//	}
//	
//	public  long count(Example example) {
//		return executeCountQuery(getCountQuery(new ExampleSpecification(example), example.getProbeType()));
//	}
//	private static long executeCountQuery(TypedQuery<Long> query) {
//
//		Assert.notNull(query, "TypedQuery must not be null!");
//
//		List<Long> totals = query.getResultList();
//		long total = 0L;
//
//		for (Long element : totals) {
//			total += element == null ? 0 : element;
//		}
//
//		return total;
//	}
//	protected  TypedQuery<Long> getCountQuery(@Nullable Specification spec, Class domainClass) {
//
//		CriteriaBuilder builder = em.getCriteriaBuilder();
//		CriteriaQuery<Long> query = builder.createQuery(Long.class);
//
//		Root root = applySpecificationToCriteria(spec, domainClass, query);
//
//		if (query.isDistinct()) {
//			query.select(builder.countDistinct(root));
//		} else {
//			query.select(builder.count(root));
//		}
//
//		// Remove all Orders the Specifications might have applied
//		query.orderBy(Collections.<Order> emptyList());
//
//		return em.createQuery(query);
//	}
//	
//	public  boolean exists(Example example) {
//		return !getQuery(new ExampleSpecification(example), example.getProbeType(), Sort.unsorted()).getResultList()
//				.isEmpty();
//	}
//	
//	public  Optional findOne(Example example) {
//
//		try {
//			return Optional.of(getQuery(new ExampleSpecification(example), example.getProbeType(), Sort.unsorted()).getSingleResult());
//		} catch (NoResultException e) {
//			return Optional.empty();
//		}
//	}
//	protected  TypedQuery getQuery(@Nullable Specification spec, Class domainClass, Sort sort) {
//
//		CriteriaBuilder builder = em.getCriteriaBuilder();
//		CriteriaQuery query = builder.createQuery(domainClass);
//
//		Root root = applySpecificationToCriteria(spec, domainClass, query);
//		query.select(root);
//
//		if (sort.isSorted()) {
//			query.orderBy(toOrders(sort, root, builder));
//		}
//		return em.createQuery(query);
//		//return applyRepositoryMethodMetadata(em.createQuery(query));
//	}
//	private  Root applySpecificationToCriteria(@Nullable Specification spec, Class domainClass,
//			CriteriaQuery query) {
//
//		Assert.notNull(domainClass, "Domain class must not be null!");
//		Assert.notNull(query, "CriteriaQuery must not be null!");
//
//		Root root = query.from(domainClass);
//
//		if (spec == null) {
//			return root;
//		}
//
//		CriteriaBuilder builder = em.getCriteriaBuilder();
//		Predicate predicate = spec.toPredicate(root, query, builder);
//
//		if (predicate != null) {
//			query.where(predicate);
//		}
//
//		return root;
//	}
////	private  TypedQuery applyRepositoryMethodMetadata(TypedQuery query) {
////
////		if (metadata == null) {
////			return query;
////		}
////
////		LockModeType type = metadata.getLockModeType();//LockModeType.READ;//
////		TypedQuery toReturn = type == null ? query : query.setLockMode(type);
////
////		//applyQueryHints(toReturn);
////
////		return toReturn;
////	}
//
//	
//	private static class ExampleSpecification<T> implements Specification<T> {
//
//		private final Example<T> example;
//
//		/**
//		 * Creates new {@link ExampleSpecification}.
//		 *
//		 * @param example
//		 */
//		ExampleSpecification(Example<T> example) {
//
//			Assert.notNull(example, "Example must not be null!");
//			this.example = example;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
//		 */
//		@Override
//		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//			return QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
//		}
//	}
//	//================================example相关的

}
