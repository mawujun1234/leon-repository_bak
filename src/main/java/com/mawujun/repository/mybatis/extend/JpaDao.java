package com.mawujun.repository.mybatis.extend;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.mawujun.repository.mybatis.dialect.AbstractDialect;
import com.mawujun.repository.mybatis.dialect.AutoDialect;
import com.mawujun.repository.mybatis.expression.VarcharLiteralExpression;
import com.mawujun.repository.utils.OpEnum;
import com.mawujun.repository.utils.PageInfo;
import com.mawujun.repository.utils.Params;
import com.mawujun.utils.CollectionUtils;
import com.mawujun.utils.ConvertUtils;
import com.mawujun.utils.ReflectUtils;
import com.mawujun.utils.StringUtils;

@Repository
public class JpaDao {
	private static final Logger logger=LoggerFactory.getLogger(JpaDao.class);
	@PersistenceContext
	private EntityManager em;
	
	private AutoDialect autoDialect=new AutoDialect();
	private volatile AbstractDialect dialect;
	
	private Map<Class,SimpleJpaRepository> repositoryCache=new HashMap<Class,SimpleJpaRepository>();
	private Map<Class,JpaEntityInformation> entityInformationCache=new HashMap<Class,JpaEntityInformation>();
	
//	public synchronized void initDialect(Connection connection)  {
//		// TODO Auto-generated method stub
//		//java.sql.Connection connection = em.unwrap(java.sql.Connection.class);
//		if(dialect==null) {
//			dialect=autoDialect.getDialect(connection);
//		}
//	}
	
	public SimpleJpaRepository getSimpleJpaRepository(Class entityClass) {
		if(repositoryCache.containsKey(entityClass)) {
			return repositoryCache.get(entityClass);
		}
		SimpleJpaRepository repository=new SimpleJpaRepository(entityClass,em);
		repositoryCache.put(entityClass, repository);
		
		
		return repository;
	}
	
	public JpaEntityInformation getEntityInformation(Class entityClass) {
		if(entityInformationCache.containsKey(entityClass)) {
			return entityInformationCache.get(entityClass);
		}
		JpaEntityInformation  entityInformation=(JpaEntityInformation)ReflectUtils.getFieldValue( getSimpleJpaRepository(entityClass), "entityInformation");
		entityInformationCache.put(entityClass, entityInformation);
		return entityInformation;
		
	}
	
	public void clear() {
		em.clear();
	}
	
	public Class getIdType(Class entityClass) {
		JpaEntityInformation entityInformation=getEntityInformation(entityClass);
		return entityInformation.getIdType();
	}
	public List<String> getIdAttributeNames(Class entityClass) {
		JpaEntityInformation entityInformation=getEntityInformation(entityClass);	
		return (List<String>)entityInformation.getIdAttributeNames();
	}
	public String getIdAttributeNames2Str(Class entityClass) {
		//JpaEntityInformation entityInformation=getEntityInformation(entityClass);	
		//return StringUtils.collectionToDelimitedString(entityInformation.getIdAttributeNames().iterator());
		return StringUtils.collectionToCommaDelimitedString(getIdAttributeNames(entityClass));
	}
	
	public Object create(Class entityClass,Object entity) {
		//return getSimpleJpaRepository(entityClass).saveAndFlush(entity);
		em.persist(entity);
		em.flush();
		return entity;
	}
	
	public List createBatch(Class entityClass,List<Object> list) {
//		SimpleJpaRepository aaa=getSimpleJpaRepository(entityClass);
//		aaa.saveAll(list);
//		aaa.flush();
		for (int i = 0; i < list.size(); i++) {
			em.persist(list.get(i));
			if (i % 30 == 0) {
				em.flush();
				em.clear();
			}
		}
		em.flush();
		return list;
	}
	
	public List createBatchByArray(Class entityClass,Object... list) {
//		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
//		repository.saveAll(CollectionUtils.arrayToList(list));
//		repository.flush();
		List result=new ArrayList();
		for (int i = 0; i < list.length; i++) {
			em.persist(list[i]);
			result.add(list[i]);
			if (i % 30 == 0) {
				em.flush();
				em.clear();
			}
		}
		em.flush();
		return result;
	}
	
	public Object save(Class entityClass,Object entity) {
		return getSimpleJpaRepository(entityClass).saveAndFlush(entity);
		//em.persist(t);
	}
	
	public List saveBatch(Class entityClass,List<Object> list) {
		SimpleJpaRepository aaa=getSimpleJpaRepository(entityClass);
		List resylt=aaa.saveAll(list);
		aaa.flush();
		return resylt;
	}
	
	public List saveBatchByArray(Class entityClass,Object... list) {
		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		List resylt=repository.saveAll(CollectionUtils.arrayToList(list));
		repository.flush();
		return resylt;
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
//		TypedQuery typedQuery=genTypedQuery(entityClass,params);
//        //List resultList = typedQuery.getResultList();
//        return typedQuery.getSingleResult();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery query = criteriaBuilder.createQuery(entityClass);
		Root itemRoot = query.from(entityClass);
		
		Predicate[] predicatesList=genPredicates(criteriaBuilder,itemRoot,params);
		query.where(predicatesList);
        TypedQuery typedQuery = em.createQuery(query);
        //typedQuery.setFirstResult(startPosition)
        //typedQuery.setMaxResults(maxResult)
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


	
	public List listByExample(Class entityClass,Object params) {
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher); 	
		return getSimpleJpaRepository(entityClass).findAll(example);
		//return findAll(example);
	}
	

	//private boolean bool =false;
	/**
	 * 获取某个日期值的日期格式
	 * @param criteriaBuilder
	 * @param path
	 * @param value
	 * @return
	 */
	private Expression<String> getDateExpression(CriteriaBuilder criteriaBuilder,Path path,Object value){
		//predicatesList.add(criteriaBuilder.equal(criteriaBuilder.substring(path.as(String.class),1,10),value));//这是可以使用的		
		//Expression<String> timeStr = criteriaBuilder.function("FORMATDATETIME", String.class, path, criteriaBuilder.parameter(String.class, "formatStr"));
		//Expression<String> timeStr = criteriaBuilder.function("FORMATDATETIME", String.class, path,criteriaBuilder.literal("yyyy-MM-dd"));
		
		String format=dialect.getDateFormatStr((String)value);
		if(!dialect.isSqlServer()) {
			//获取日期格式化函数
			String date_format=dialect.getDateFormatFunction();
			Expression<String> timeStr = criteriaBuilder.function(date_format, String.class, path,criteriaBuilder.literal(format));
			return timeStr;
		} else {
			//获取日期格式化函数
			String date_format=dialect.getDateFormatFunction();
			VarcharLiteralExpression varchar=new VarcharLiteralExpression((CriteriaBuilderImpl)criteriaBuilder,50);
			
			if(format.indexOf(',')==-1) {
				Expression<String> timeStr = criteriaBuilder.function(date_format, String.class,varchar,path,criteriaBuilder.literal(Integer.parseInt(format)));
				return timeStr;
			} else {
				String[] formatArray=format.split(",");

//				//function的参数影响到了substring的参数
//				Expression<String> convert=criteriaBuilder.function(date_format, String.class,varchar,path,criteriaBuilder.literal(Integer.parseInt(formatArray[0])));
//				//加criteriaBuilder.lower()的原因是解决了criteriaBuilder.substring()后两个参数丢失的bug
//				Expression<String> timeStr = criteriaBuilder.substring(criteriaBuilder.lower(convert),1,Integer.parseInt(formatArray[1]));
				
				Expression<String> convert=criteriaBuilder.function(date_format, String.class,varchar,path,criteriaBuilder.literal(formatArray[0]));
				Expression<String> timeStr = criteriaBuilder.substring(convert,1,Integer.parseInt(formatArray[1]));
				
				//ConvertFunction convert=new ConvertFunction((CriteriaBuilderImpl)criteriaBuilder,path,50,Integer.parseInt(formatArray[0]));
				//Expression<String> timeStr = criteriaBuilder.substring(criteriaBuilder.lower(convert),1,Integer.parseInt(formatArray[1]));//
				return timeStr;
			}	
		}
		
	}
	
	//private TypedQuery genTypedQuery(Class entityClass,Map<String,Object> params) {
	private Predicate[] genPredicates(CriteriaBuilder criteriaBuilder,Root itemRoot,Map<String,Object> params) {
//		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//		CriteriaQuery query = criteriaBuilder.createQuery(entityClass);
//		Root itemRoot = query.from(entityClass);
	
		if (dialect == null) {
			synchronized (autoDialect) {
				if (dialect == null) {
					// Connection connection = em.unwrap(java.sql.Connection.class);
					SessionImplementor session = em.unwrap(SessionImplementor.class);
					Connection connection = session.connection();
					dialect = autoDialect.getDialect(connection);
				}
			}
		}

		boolean isParams=(params instanceof Params);
		//Predicate[] predicatesList=new Predicate[params.size()];
		List<Predicate> predicatesList=new ArrayList<Predicate>();
		//int i=0;
		for(Entry<String,Object> param:params.entrySet()) {   
			Path path=itemRoot.get(param.getKey());
			Class javatype=path.getJavaType();
			Object value=param.getValue();
			if(isParams) {
				OpEnum opEnum=((Params)params).getOpEnum(param.getKey());
				switch (opEnum)
		        {
		            case eq:
		            	if(Date.class.isAssignableFrom(javatype)) {
		            		if(value instanceof String) {
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,value);
			            		predicatesList.add(criteriaBuilder.equal(timeStr,value));
		            		} else if(value instanceof Date) {
		            			predicatesList.add(criteriaBuilder.equal(path,(Date)value));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            		
		            	} else {
			            	predicatesList.add(criteriaBuilder.equal(path,ConvertUtils.convert(value, javatype)));
		            	}	
		                break;
		            case eq_i:
		            	if(String.class.isAssignableFrom(javatype)) {
			            	predicatesList.add(criteriaBuilder.equal(criteriaBuilder.lower(path),value.toString().toLowerCase()));
		            	} else if(Date.class.isAssignableFrom(javatype)) {
		            		if(value instanceof String) {
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,value);
			            		predicatesList.add(criteriaBuilder.equal(timeStr,value));
		            		} else if(value instanceof Date) {
		            			predicatesList.add(criteriaBuilder.equal(path,(Date)value));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            	} else {
			            	predicatesList.add(criteriaBuilder.equal(path,ConvertUtils.convert(value, javatype)));
		            	}            	
		                break;
		            case noteq:
		            	//predicatesList.add(criteriaBuilder.notEqual(path,ConvertUtils.convert(value, javatype)));
		            	if(Date.class.isAssignableFrom(javatype)) {
		            		if(value instanceof String) {
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,value);
			            		predicatesList.add(criteriaBuilder.notEqual(timeStr,value));
		            		} else if(value instanceof Date) {
		            			predicatesList.add(criteriaBuilder.notEqual(path,(Date)value));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            		
		            	} else {
			            	predicatesList.add(criteriaBuilder.notEqual(path,ConvertUtils.convert(value, javatype)));
		            	}	
		                break;
		            case noteq_i:
		            	if(String.class.isAssignableFrom(javatype)) {
			            	predicatesList.add(criteriaBuilder.notEqual(criteriaBuilder.lower(path),value.toString().toLowerCase()));
		            	} else if(Date.class.isAssignableFrom(javatype)) {
		            		if(value instanceof String) {
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,value);
			            		predicatesList.add(criteriaBuilder.notEqual(timeStr,value));
		            		} else if(value instanceof Date) {
		            			predicatesList.add(criteriaBuilder.notEqual(path,(Date)value));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            	}  else {
			            	predicatesList.add(criteriaBuilder.notEqual(path,ConvertUtils.convert(value, javatype)));
		            	}  
		                break;
		            case gt:	
		            	if(Number.class.isAssignableFrom(javatype) || ReflectUtils.isPrimitiveNumber(value)) {
		            		predicatesList.add(criteriaBuilder.gt(path,(Number)ConvertUtils.convert(value, javatype)));
		            	} else if(Date.class.isAssignableFrom(javatype)) {
		            		//predicatesList.add(criteriaBuilder.greaterThan(path,(Date)ConvertUtils.convert(value, javatype)));
		            		if(value instanceof String) {
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,value);
			            		predicatesList.add(criteriaBuilder.greaterThan(timeStr,(String)value));
		            		} else if(value instanceof Date) {
		            			predicatesList.add(criteriaBuilder.greaterThan(path,(Date)value));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            	} else {
		            		predicatesList.add(criteriaBuilder.greaterThan(path,param.getValue().toString()));
		            	}
		                break;
		            case ge:
		            	if(Number.class.isAssignableFrom(javatype) || ReflectUtils.isPrimitiveNumber(value)) {
		            		predicatesList.add(criteriaBuilder.ge(path,(Number)ConvertUtils.convert(value, javatype)));
		            	} else if(Date.class.isAssignableFrom(javatype)) {
		            		//predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(path,(Date)ConvertUtils.convert(value, javatype)));
		            		if(value instanceof String) {
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,value);
			            		predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(timeStr,(String)value));
		            		} else if(value instanceof Date) {
		            			predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(path,(Date)value));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            	} else {
		            		predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(path,param.getValue().toString()));
		            	}
		                break;
		            case lt:
		            	if(Number.class.isAssignableFrom(javatype) || ReflectUtils.isPrimitiveNumber(value)) {
		            		predicatesList.add(criteriaBuilder.lt(path,(Number)ConvertUtils.convert(value, javatype)));
		            	} else if(Date.class.isAssignableFrom(javatype)) {
		            		//predicatesList.add(criteriaBuilder.lessThan(path,(Date)ConvertUtils.convert(value, javatype)));
		            		if(value instanceof String) {
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,value);
			            		predicatesList.add(criteriaBuilder.lessThan(timeStr,(String)value));
		            		} else if(value instanceof Date) {
		            			predicatesList.add(criteriaBuilder.lessThan(path,(Date)value));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            	} else {
		            		predicatesList.add(criteriaBuilder.lessThan(path,param.getValue().toString()));
		            	}
		                break;
		            case le:
		            	if(Number.class.isAssignableFrom(javatype) || ReflectUtils.isPrimitiveNumber(value)) {
		            		predicatesList.add(criteriaBuilder.le(path,(Number)ConvertUtils.convert(value, javatype)));
		            	} else if(Date.class.isAssignableFrom(javatype)) {
		            		//predicatesList.add(criteriaBuilder.lessThanOrEqualTo(path,(Date)ConvertUtils.convert(value, javatype)));
		            		if(value instanceof String) {
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,value);
			            		predicatesList.add(criteriaBuilder.lessThanOrEqualTo(timeStr,(String)value));
		            		} else if(value instanceof Date) {
		            			predicatesList.add(criteriaBuilder.lessThanOrEqualTo(path,(Date)value));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            	} else {
		            		predicatesList.add(criteriaBuilder.lessThanOrEqualTo(path,param.getValue().toString()));
		            	}
		                break;
		            case between:{
		            	Object[] values=(Object[])param.getValue();
		            	if(Number.class.isAssignableFrom(javatype) || ReflectUtils.isPrimitiveNumber(value)) {
		            		predicatesList.add(criteriaBuilder.ge(path,(Number)ConvertUtils.convert(values[0], javatype)));
		            		predicatesList.add(criteriaBuilder.le(path,(Number)ConvertUtils.convert(values[1], javatype)));
		            	} else if(Date.class.isAssignableFrom(javatype)) {
		            		//predicatesList.add(criteriaBuilder.between(path,(Date)ConvertUtils.convert(values[0], javatype),(Date)ConvertUtils.convert(values[1], javatype)));
		            		if(values[0] instanceof String) {
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,values[0]);
		            			predicatesList.add(criteriaBuilder.between(timeStr,(String)values[0],(String)values[1]));
		            		} else if(values[0] instanceof Date) {
		            			predicatesList.add(criteriaBuilder.between(path,(Date)values[0],(Date)values[1]));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            	} else {
		            		predicatesList.add(criteriaBuilder.between(path,values[0].toString(),values[1].toString()));
		            		//predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(path,values[0].toString()));
		            		//predicatesList.add(criteriaBuilder.lessThanOrEqualTo(path,values[1].toString()));
		            	}
		                break;}
		            case in:{	            	
		            	Object[] values=(Object[])value;
		            	if(Date.class.isAssignableFrom(javatype)) {
		            		if(values[0] instanceof String) {	
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,values[0]);
		            			In in = criteriaBuilder.in(timeStr);
		            			for (Object obj:values) {
				            		in.value((String)obj);
				            	}
		            			predicatesList.add(in);
		            		} else if(values[0] instanceof Date) {
		            			In in = criteriaBuilder.in(path);
			            		for (Object obj:values) {
				            		in.value((Date)obj);
				            	}
			            		predicatesList.add(in);
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            	} else {
		            		In in = criteriaBuilder.in(path);
		            		for (Object obj:values) {
			            		in.value(ConvertUtils.convert(obj, javatype));
			            	}
		            		predicatesList.add(in);
		            	}
		            	
		        		
		                break;
		            }
		            case notin:
		            	Object[] values=(Object[])value;
		            	if(Date.class.isAssignableFrom(javatype)) {
		            		if(values[0] instanceof String) {	
		            			Expression<String> timeStr=getDateExpression(criteriaBuilder,path,values[0]);
		            			In in = criteriaBuilder.in(timeStr);
		            			for (Object obj:values) {
				            		in.value((String)obj);
				            	}
		            			predicatesList.add(criteriaBuilder.not(in));
		            		} else if(values[0] instanceof Date) {
		            			In in = criteriaBuilder.in(path);
			            		for (Object obj:values) {
				            		in.value((Date)obj);
				            	}
			            		predicatesList.add(criteriaBuilder.not(in));
		            		} else {
		            			throw new IllegalArgumentException("参数"+param.getKey()+"类型不对,应该为日期字符串或Date类型!javatype="+value);
		            		}
		            	} else {
		            		In in = criteriaBuilder.in(path);
		            		for (Object obj:values) {
			            		in.value(ConvertUtils.convert(obj, javatype));
			            	}
		            		predicatesList.add(criteriaBuilder.not(in));
		            	}
		            	break;
//		            	In in = criteriaBuilder.in(path);
//		            	Object[] values=(Object[])value;
//		            	for (Object obj:values) {
//		            		in.value(ConvertUtils.convert(obj, javatype));
//		            	}
//		            	predicatesList.add(criteriaBuilder.not(in));
//		                break;
		            case like:
		            	predicatesList.add(criteriaBuilder.like(path, value.toString()));
		                break;
		            case likeprefix:
		            	predicatesList.add(criteriaBuilder.like(path, value.toString()));
		                break;
		            case likesuffix:
		            	predicatesList.add(criteriaBuilder.like(path, value.toString()));
		                break;
		            case like_i:
		            	predicatesList.add(criteriaBuilder.like(criteriaBuilder.lower(path), value.toString().toLowerCase()));
		                break;
		            case likeprefix_i:
		            	predicatesList.add(criteriaBuilder.like(criteriaBuilder.lower(path), value.toString().toLowerCase()));
		                break;
		            case likesuffix_i:
		            	predicatesList.add(criteriaBuilder.like(criteriaBuilder.lower(path), value.toString().toLowerCase()));
		                break;
		            case notlike:
		            	predicatesList.add(criteriaBuilder.notLike(path, value.toString()));
		                break;
		            case notlikeprefix:
		            	predicatesList.add(criteriaBuilder.notLike(path, value.toString()));
		                break;
		            case notlikesuffix:
		            	predicatesList.add(criteriaBuilder.notLike(path, value.toString()));
		                break;
		            case isnull:
		            	predicatesList.add(criteriaBuilder.isNull(path));
		                break;
		            case isnotnull:
		            	predicatesList.add(criteriaBuilder.isNotNull(path));
		                break;
		            default:
		                break;
		        }
			} else {
				predicatesList.add(criteriaBuilder.equal(itemRoot.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
			}
			//i++;
			//predicatesList.add(criteriaBuilder.equal(itemRoot.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
//		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
//        TypedQuery typedQuery = em.createQuery(query);
//        return typedQuery;
	}
	public List listByMap(Class entityClass,Map<String,Object> params) {	
		if(params==null) {
			return this.listAll(entityClass);
		}
		//TypedQuery typedQuery=genTypedQuery(entityClass,params);
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery query = criteriaBuilder.createQuery(entityClass);
		Root itemRoot = query.from(entityClass);
		
		Predicate[] predicatesList=genPredicates(criteriaBuilder,itemRoot,params);
		query.where(predicatesList);
        TypedQuery typedQuery = em.createQuery(query);
       // typedQuery.setParameter("formatStr", "yyyy-MM-dd");

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
	
	private  class PageSpecification<T> implements Specification<T> {
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
//				predicatesList=new Predicate[params.size()];
//				int i=0;
//				for(Entry<String,Object> param:params.entrySet()) {   
//					Class javatype=root.get(param.getKey()).getJavaType();
//					predicatesList[i]=cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype));
//					i++;
//					//predicatesList.add(cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
//				}
				predicatesList=genPredicates(cb,root,params);
				
			} 
			Predicate p =cb.and(predicatesList);
			return p;

		}
	}
	
	public PageInfo listPageByMap(Class entityClass,Map<String,Object> params, int pageIndex,int limit) {
		Pageable pageable = PageRequest.of(pageIndex, limit);
   
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
	
	public PageInfo listPageByPageInfo(Class entityClass,PageInfo pageinfo) {
		Object params=pageinfo.getParams();
		Pageable pageable = PageRequest.of(pageinfo.getPage(), pageinfo.getLimit());
		if(params instanceof Map) {
			PageSpecification spec=new PageSpecification((Map)params);
			Page page=getSimpleJpaRepository(entityClass).findAll(spec, pageable);
			pageinfo.setTotal((int)page.getTotalElements());
			pageinfo.setRoot(page.getContent());
		} else {
			ExampleMatcher matcher = ExampleMatcher.matching();
			Example example = Example.of(params, matcher); 	
			Page page=getSimpleJpaRepository(entityClass).findAll(example, pageable);
			pageinfo.setTotal((int)page.getTotalElements());
			pageinfo.setRoot(page.getContent());
		}

		return pageinfo;
	}
	
	
	public Object update(Class entityClass,Object entity) {
		//SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		//return repository.u(entity);
		Object aaa=em.merge(entity);
		em.flush();
		return aaa;
	}
	public List updateBatch(Class entityClass,List list) {
		for (int i = 0; i < list.size(); i++) {
			em.merge(list.get(i));
			if (i % 30 == 0) {
				em.flush();
				em.clear();
			}
		}
		em.flush();
		return list;
	}
	public List updateBatchByArray(Class entityClass,Object... list) {
		List result=new ArrayList();
		for (int i = 0; i < list.length; i++) {
			em.merge(list[i]);
			result.add(list[i]);
			if (i % 30 == 0) {
				em.flush();
				em.clear();
			}
		}
		em.flush();
		return result;
	}
	
	public int updateByMap(Class entityClass,Map<String,Object> sets,Map<String,Object> params) {
//		if(params.get("id")==null || "".equals(params.get("id"))) {
//			throw new BusinessException("id不能为空!");
//		}
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate update = cb.createCriteriaUpdate(entityClass);
		Root root = update.from(entityClass);
//		//List<Predicate> predicatesList = new ArrayList<Predicate>();
//		Predicate[] predicatesList=new Predicate[params.size()];
//		int i=0;
//		for(Entry<String,Object> param:params.entrySet()) {   
//			Class javatype=root.get(param.getKey()).getJavaType();
//			predicatesList[i]=cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype));
//			i++;
//			//predicatesList.add(cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
//		}
		
		Predicate[] predicatesList=genPredicates(cb,root,params);
		update.where(predicatesList);
		
		for(Entry<String,Object> set:sets.entrySet()) { 
			Class javatype=root.get(set.getKey()).getJavaType();
			update.set(root.get(set.getKey()),ConvertUtils.convert(set.getValue(), javatype));
		}
		
		Query query = em.createQuery(update);
        int rowCount = query.executeUpdate();
        em.clear();
        return rowCount;
		//repository.flush();
	}
	

	/**
	 * 已经支持复合主键
	 * @param entityClass
	 * @param sets
	 * @param id
	 * @return
	 */
	public int updateById(Class entityClass,Map<String,Object> sets,Object id) {
		JpaEntityInformation entityInformation=getEntityInformation(entityClass);
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate update = cb.createCriteriaUpdate(entityClass);
		
		Root root = update.from(entityClass);
		Class id_javatype=entityInformation.getIdType();
		Iterable<String> idAttributeNames = entityInformation.getIdAttributeNames();
		if (!entityInformation.hasCompositeId()) {
			update.where(cb.equal(root.get(idAttributeNames.iterator().next()),ConvertUtils.convert(id, id_javatype)));
		} else {
			Predicate[] predicatesList=new Predicate[((Collection)idAttributeNames).size()];
			int i=0;
			for (String idAttributeName : idAttributeNames) {
				Object idAttributeValue = entityInformation.getCompositeIdAttributeValue(id, idAttributeName);

				//当复合id是以一个单独的类的形式存在的时候
//				boolean complexIdParameterValueDiscovered = idAttributeValue != null&& !query.getParameter(idAttributeName).getParameterType().isAssignableFrom(idAttributeValue.getClass());
//				if (complexIdParameterValueDiscovered) {
//					update.where(cb.equal(root.get(idAttributeName),ConvertUtils.convert(id, id_javatype)));
//					break;
//				} 
				predicatesList[i]=cb.equal(root.get(idAttributeName),ConvertUtils.convert(idAttributeValue, id_javatype));
				i++;
			}
			update.where(predicatesList);	
		}

		
		for(Entry<String,Object> set:sets.entrySet()) { 
			Class javatype=root.get(set.getKey()).getJavaType();
			update.set(root.get(set.getKey()),ConvertUtils.convert(set.getValue(), javatype));
		}
		
		Query query = em.createQuery(update);
        int rowCount = query.executeUpdate();
        em.clear();
        return rowCount;
		
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
	
	public boolean removeAll(Class entityClass) {
		getSimpleJpaRepository(entityClass).deleteAll();
		return true;
	}
	
	public int removeByMap(Class entityClass,Map<String,Object> params) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete update = cb.createCriteriaDelete(entityClass);
		Root root = update.from(entityClass);
//		//List<Predicate> predicatesList = new ArrayList<Predicate>();
//		Predicate[] predicatesList=new Predicate[params.size()];
//		int i=0;
//		for(Entry<String,Object> param:params.entrySet()) {   
//			Class javatype=root.get(param.getKey()).getJavaType();
//			predicatesList[i]=cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype));
//			i++;
//			//predicatesList.add(cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
//		}
//		update.where(predicatesList);
		
		Predicate[] predicatesList=genPredicates(cb,root,params);
		update.where(predicatesList);
		
		Query query = em.createQuery(update);
        int rowCount = query.executeUpdate();
        em.clear();
        return rowCount;
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
	
	public long count(Class entityClass ) {
		return getSimpleJpaRepository(entityClass).count();
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
	
	public long countByMap(Class entityClass,Map<String,Object> params) {
		//SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		//repository.count(spec)
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
		Root root = criteriaQuery.from(entityClass);
		JpaEntityInformation entityInformation=getEntityInformation(entityClass);
		
		Expression<Long> selection=cb.count(root.get(entityInformation.getIdAttribute()));
		criteriaQuery.select(selection);
		
		int i=0;
		if (params != null && params.size() > 0) {
//			Predicate[] predicatesList = new Predicate[params.size()];
//
//			for (Entry<String, Object> param : params.entrySet()) {
//				Class javatype = root.get(param.getKey()).getJavaType();
//				predicatesList[i] = cb.equal(root.get(param.getKey()),
//						ConvertUtils.convert(param.getValue(), javatype));
//				i++;
//			}
			Predicate[] predicatesList=genPredicates(cb,root,params);
			criteriaQuery.where(predicatesList);
		}
		
		TypedQuery<Long> query= em.createQuery(criteriaQuery);

		return query.getSingleResult();
		
//		Class id_javatype=entityInformation.getIdType();
//		Iterable<String> idAttributeNames = entityInformation.getIdAttributeNames();
//		if (!entityInformation.hasCompositeId()) {
//			//criteriaQuery.where(cb.equal(root.get(idAttributeNames.iterator().next()),ConvertUtils.convert(id, id_javatype)));
//			Expression<Long> selection=cb.count(root.get(idAttributeNames.iterator().next()));
//			criteriaQuery.select(selection);
//		} else {
//			root.get(entityInformation.getIdAttribute())
//			EntityTypeExpression exp=new EntityTypeExpression(cb,id_javatype);
////			Predicate[] predicatesList=new Predicate[((Collection)idAttributeNames).size()];
////			int i=0;
////			for (String idAttributeName : idAttributeNames) {
////				//Object idAttributeValue = entityInformation.getCompositeIdAttributeValue(id, idAttributeName);
////				//predicatesList[i]=cb.equal(root.get(idAttributeName),ConvertUtils.convert(id, id_javatype));
////				i++;
////			}
////			update.where(predicatesList);	
//		}

	}
//	private static final class CountByMapSpecification<T> implements Specification<T> {
//		private final JpaEntityInformation<T, ?> entityInformation;
//		Map<String,Object> params;
//
//		CountByMapSpecification(JpaEntityInformation<T, ?> entityInformation) {
//			this.entityInformation = entityInformation;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
//		 */
//		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//			query.select(cb.count(root.get));
//
////			Predicate[] predicatesList=new Predicate[0];
////			if(params!=null) {
////				predicatesList=new Predicate[params.size()];
////				int i=0;
////				for(Entry<String,Object> param:params.entrySet()) {   
////					Class javatype=root.get(param.getKey()).getJavaType();
////					predicatesList[i]=cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype));
////					i++;
////					//predicatesList.add(cb.equal(root.get(param.getKey()),ConvertUtils.convert(param.getValue(), javatype)));
////				}
////				
////			} 
////			Predicate p =cb.and(predicatesList);
////			return p;
//		}
//	}
	
	public boolean existsById(Class entityClass,Object id) {
		return getSimpleJpaRepository(entityClass).existsById(id);
		//return exists(example);
	}
	public boolean existsByExample(Class entityClass,Object params) {
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher); 	
		return getSimpleJpaRepository(entityClass).exists(example);
		//return exists(example);
	}
	public boolean existsByMap(Class entityClass,Map<String,Object> params) {
		long count=countByMap(entityClass,params);
		return count >= 1;
		
//		JpaEntityInformation entityInformation=getEntityInformation(entityClass).get(entityClass);
//		
//		String placeholder ="";// provider.getCountQueryPlaceholder();
//		String entityName = entityInformation.getEntityName();
//		Iterable<String> idAttributeNames = entityInformation.getIdAttributeNames();
//		if(idAttributeNames!=null) {
//			Iterator<String> iterator=idAttributeNames.iterator();
//			while(iterator.hasNext()) {
//				String str=iterator.next();
//				placeholder=placeholder+","+str;
//			}
//		}
//		if(placeholder!=null) {
//			placeholder=placeholder.substring(1, placeholder.length());
//		} else {
//			placeholder="*";
//		}
//		
//		String existsQuery = QueryUtils.getExistsQueryString(entityName, placeholder, idAttributeNames);
//		
//		String COUNT_QUERY_STRING = "select count(%s) from %s x";
//		
//		String aa=String.format(COUNT_QUERY_STRING, placeholder,entityName);
//		System.out.println(aa);
//		
//		params.str
//		return false;
		
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
