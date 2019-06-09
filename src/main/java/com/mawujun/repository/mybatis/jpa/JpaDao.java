package com.mawujun.repository.mybatis.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
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
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.mawujun.bean.BeanUtil;
import com.mawujun.convert.Convert;
import com.mawujun.repository.mybatis.dialect.AbstractDialect;
import com.mawujun.repository.mybatis.dialect.AutoDialect;
import com.mawujun.repository.mybatis.dialect.DBAlias;
import com.mawujun.repository.mybatis.expression.VarcharLiteralExpression;
import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.Cnd;
import com.mawujun.repository.utils.OpEnum;
import com.mawujun.repository.utils.Page;
import com.mawujun.utils.CollectionUtils;
import com.mawujun.utils.ReflectionUtils;
import com.mawujun.utils.string.StringUtils;

@Repository
//@Transactional(rollbackOn= {Exception.class})
public class JpaDao {
	private static final Logger logger = LoggerFactory.getLogger(JpaDao.class);
	@PersistenceContext
	private EntityManager em;

	private AutoDialect autoDialect = new AutoDialect();
	private volatile AbstractDialect dialect;

	private Map<Class, SimpleJpaRepository> repositoryCache = new HashMap<Class, SimpleJpaRepository>();
	private Map<Class, JpaEntityInformation> entityInformationCache = new HashMap<Class, JpaEntityInformation>();

//	public synchronized void initDialect(Connection connection)  {
//		// TODO Auto-generated method stub
//		//java.sql.Connection connection = em.unwrap(java.sql.Connection.class);
//		if(dialect==null) {
//			dialect=autoDialect.getDialect(connection);
//		}
//	}

	public SimpleJpaRepository getSimpleJpaRepository(Class entityClass) {
		if (repositoryCache.containsKey(entityClass)) {
			return repositoryCache.get(entityClass);
		}
		SimpleJpaRepository repository = new SimpleJpaRepository(entityClass, em);
		repositoryCache.put(entityClass, repository);

		return repository;
	}

	public JpaEntityInformation getEntityInformation(Class entityClass) {
		if (entityInformationCache.containsKey(entityClass)) {
			return entityInformationCache.get(entityClass);
		}
		JpaEntityInformation entityInformation = (JpaEntityInformation) ReflectionUtils
				.getFieldValue(getSimpleJpaRepository(entityClass), "entityInformation");
		
		entityInformationCache.put(entityClass, entityInformation);
		
		
		return entityInformation;

	}

	private Map<Class, Set<String>> attributeNameCache = new HashMap<Class, Set<String>>();

	public boolean hasAttribute(Class entityClass, String attr_name) {
		
		Set<String> attres = attributeNameCache.get(entityClass);
		if (attres == null) {
			synchronized (attributeNameCache) {
				if (attres == null) {
					attres = new HashSet<String>();

					Metamodel mm = em.getMetamodel();
					EntityType et = mm.entity(entityClass);
					// et.getAttribute(param.getKey());
					Set attributes = et.getAttributes();
					for (Object obj : attributes) {
						Attribute attr = (Attribute) obj;
						Field field = ReflectionUtils.getField(entityClass, attr.getName());
						if (field == null) {
							continue;
						}
						attres.add(attr.getName());
					}
				}
			}

		}
		return attres.contains(attr_name);
	}

	public void clear() {
		em.clear();
	}

	public Class getIdType(Class entityClass) {
		JpaEntityInformation entityInformation = getEntityInformation(entityClass);
		return entityInformation.getIdType();
	}

	public List<String> getIdAttributeNames(Class entityClass) {
		JpaEntityInformation entityInformation = getEntityInformation(entityClass);
		
		return (List<String>) entityInformation.getIdAttributeNames();
	}
	
	public boolean hasCompositeId(Class entityClass) {
		JpaEntityInformation entityInformation = getEntityInformation(entityClass);
		return entityInformation.hasCompositeId();
	}

	public String getIdAttributeNames2Str(Class entityClass) {
		// JpaEntityInformation entityInformation=getEntityInformation(entityClass);
		// return
		// StringUtils.collectionToDelimitedString(entityInformation.getIdAttributeNames().iterator());
		return StringUtils.collectionToCommaDelimitedString(getIdAttributeNames(entityClass));
	}

	public Object create(Class entityClass, Object entity) {
		// return getSimpleJpaRepository(entityClass).saveAndFlush(entity);
		em.persist(entity);
		em.flush();
		return entity;
	}

	public List createBatch(Class entityClass, List<Object> list) {
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

	public List createBatchByArray(Class entityClass, Object... list) {
//		SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
//		repository.saveAll(CollectionUtils.arrayToList(list));
//		repository.flush();
		List result = new ArrayList();
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
	
	public Object create(Class entityClass, Map<String,Object> map) {
		// return getSimpleJpaRepository(entityClass).saveAndFlush(entity);
		//insert into p_agentrole (agentid,roleid)  values(?,?)
//		String qlString="inser into "+entityClass.getCanonicalName()+"(xxxxxx) values(yyyyyyyyyy)";
//		String[] fields=new String[map.size()];
//		for(Entry<String,Object> entity:map.entrySet()) {
//			
//		}
		//Object entity=BeanUtils.mapToObject(map, entityClass);
		Object entity=BeanUtil.mapToBean(map, entityClass, false);
		return this.create(entityClass, entity);
	}

	public Object save(Class entityClass, Object entity) {
		return getSimpleJpaRepository(entityClass).saveAndFlush(entity);
		// em.persist(t);
	}

	public List saveBatch(Class entityClass, List<Object> list) {
		SimpleJpaRepository aaa = getSimpleJpaRepository(entityClass);
		List resylt = aaa.saveAll(list);
		aaa.flush();
		return resylt;
	}

	public List saveBatchByArray(Class entityClass, Object... list) {
		SimpleJpaRepository repository = getSimpleJpaRepository(entityClass);
		List resylt = repository.saveAll(CollectionUtils.arrayToList(list));
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

	public Object getById(Class entityClass, Object id) {
		SimpleJpaRepository repository = getSimpleJpaRepository(entityClass);

		Optional optional = repository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}

		// return em.find(entityClass, id);
	}

	public Object getByMap(Class entityClass, Map<String, Object> params) {
//		TypedQuery typedQuery=genTypedQuery(entityClass,params);
//        //List resultList = typedQuery.getResultList();
//        return typedQuery.getSingleResult();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery query = criteriaBuilder.createQuery(entityClass);
		Root itemRoot = query.from(entityClass);

		Predicate[] predicatesList = genPredicates(criteriaBuilder, itemRoot, params);
		query.where(predicatesList);
		TypedQuery typedQuery = em.createQuery(query);
//		// typedQuery.setFirstResult(startPosition)
//		// typedQuery.setMaxResults(maxResult)
//		try {
			Object obj= typedQuery.getSingleResult();
			return obj;
//		} catch(Exception e) {
//			return null;
//		}

	}

	public Object getByExample(Class entityClass, Object params) {
		SimpleJpaRepository repository = getSimpleJpaRepository(entityClass);
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher);
		Optional option = repository.findOne(example);
		if (option.isPresent()) {
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

	public List listByExample(Class entityClass, Object params) {
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher);
		return getSimpleJpaRepository(entityClass).findAll(example);
		// return findAll(example);
	}

	// private boolean bool =false;
	/**
	 * 获取某个日期值的日期格式
	 * 
	 * @param criteriaBuilder
	 * @param path
	 * @param value
	 * @return
	 */
	private Expression<String> getDateExpression(CriteriaBuilder criteriaBuilder, Path path, Object value) {
		// predicatesList.add(criteriaBuilder.equal(criteriaBuilder.substring(path.as(String.class),1,10),value));//这是可以使用的
		// Expression<String> timeStr = criteriaBuilder.function("FORMATDATETIME",
		// String.class, path, criteriaBuilder.parameter(String.class, "formatStr"));
		// Expression<String> timeStr = criteriaBuilder.function("FORMATDATETIME",
		// String.class, path,criteriaBuilder.literal("yyyy-MM-dd"));

		String format = dialect.getDateFormatStr((String) value);
		if (dialect.getAlias() == DBAlias.sqlserver || dialect.getAlias() == DBAlias.sqlserver2005
				|| dialect.getAlias() == DBAlias.sqlserver2012) {
			// 获取日期格式化函数
			String date_format = dialect.getDateFormatFunction();
			VarcharLiteralExpression varchar = new VarcharLiteralExpression((CriteriaBuilderImpl) criteriaBuilder, 50);

			if (format.indexOf(',') == -1) {
				Expression<String> timeStr = criteriaBuilder.function(date_format, String.class, varchar, path,
						criteriaBuilder.literal(Integer.parseInt(format)));
				return timeStr;
			} else {
				String[] formatArray = format.split(",");

//				//function的参数影响到了substring的参数
//				Expression<String> convert=criteriaBuilder.function(date_format, String.class,varchar,path,criteriaBuilder.literal(Integer.parseInt(formatArray[0])));
//				//加criteriaBuilder.lower()的原因是解决了criteriaBuilder.substring()后两个参数丢失的bug
//				Expression<String> timeStr = criteriaBuilder.substring(criteriaBuilder.lower(convert),1,Integer.parseInt(formatArray[1]));

//				Expression<String> convert=criteriaBuilder.function(date_format, String.class,varchar,path,criteriaBuilder.literal(formatArray[0]));
//				Expression<String> timeStr = criteriaBuilder.substring(convert,1,Integer.parseInt(formatArray[1]));

				// ConvertFunction convert=new
				// ConvertFunction((CriteriaBuilderImpl)criteriaBuilder,path,50,Integer.parseInt(formatArray[0]));
				Expression<String> convert = criteriaBuilder.function(date_format, String.class, varchar, path,
						criteriaBuilder.literal(Integer.parseInt(formatArray[0])));
				Expression<String> timeStr = criteriaBuilder.substring(criteriaBuilder.lower(convert), 1,
						Integer.parseInt(formatArray[1]));//
				return timeStr;
			}
		} else if (dialect.getAlias() == DBAlias.derby) {
			// String date_format=dialect.getDateFormatFunction();
			if ("yyyy-MM".equals(format)) {
				// DerbyFunctionYYYYMM YYYYMM=new
				// DerbyFunctionYYYYMM((CriteriaBuilderImpl)criteriaBuilder,path);
				// return criteriaBuilder.substring(YYYYMM, 1,7);
				Expression<String> timeStr = criteriaBuilder.function("varchar", String.class, path);
				return criteriaBuilder.substring(timeStr, 1, 7);
			} else if ("TIMESTAMP".equals(format)) {
				Expression<String> timeStr = criteriaBuilder.function("varchar", String.class, path);
				return criteriaBuilder.substring(timeStr, 1, 19);
			} else {
				Expression<String> timeStr = criteriaBuilder.function(format, String.class, path);
				return criteriaBuilder.function("varchar", String.class, timeStr);
			}

		} else {
			// 获取日期格式化函数
			String date_format = dialect.getDateFormatFunction();
			Expression<String> timeStr = criteriaBuilder.function(date_format, String.class, path,
					criteriaBuilder.literal(format));
			return timeStr;
		}

	}

	// private TypedQuery genTypedQuery(Class entityClass,Map<String,Object> params)
	// {
	private Predicate[] genPredicates(CriteriaBuilder criteriaBuilder, Root itemRoot, Map<String, Object> params) {
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

		boolean isParams = (params instanceof Cnd);
		// Predicate[] predicatesList=new Predicate[params.size()];
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		// int i=0;
		for (Entry<String, Object> param : params.entrySet()) {
			String param_key=param.getKey();
			if(!hasAttribute(itemRoot.getJavaType(),param_key)) {
				if(Cnd.limit_key.equals(param_key) || Cnd.page_key.equals(param_key) || Cnd.start_key.equals(param_key)) {
					continue;
				} else {
					logger.info("属性{}不在{}类中!,不能作为条件，已过滤掉了",param.getKey(),itemRoot.getJavaType());
					continue;
				}	
			}
			Path path = itemRoot.get(param.getKey());
			Class javatype = path.getJavaType();
			Object value = param.getValue();
			if (isParams) {
				OpEnum opEnum = ((Cnd) params).getOpEnum(param.getKey());
				switch (opEnum) {
				case eq:
					if (Date.class.isAssignableFrom(javatype)) {
						if (value instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, value);
							predicatesList.add(criteriaBuilder.equal(timeStr, value));
						} else if (value instanceof Date) {
							predicatesList.add(criteriaBuilder.equal(path, (Date) value));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}

					} else {
						predicatesList.add(criteriaBuilder.equal(path, Convert.convert(javatype,value)));
					}
					break;
				case eq_i:
					if (String.class.isAssignableFrom(javatype)) {
						predicatesList.add(
								criteriaBuilder.equal(criteriaBuilder.lower(path), value.toString().toLowerCase()));
					} else if (Date.class.isAssignableFrom(javatype)) {
						if (value instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, value);
							predicatesList.add(criteriaBuilder.equal(timeStr, value));
						} else if (value instanceof Date) {
							predicatesList.add(criteriaBuilder.equal(path, (Date) value));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}
					} else {
						predicatesList.add(criteriaBuilder.equal(path, Convert.convert(javatype,value)));
					}
					break;
				case noteq:
					// predicatesList.add(criteriaBuilder.notEqual(path,Convert.convert(value,
					// javatype)));
					if (Date.class.isAssignableFrom(javatype)) {
						if (value instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, value);
							predicatesList.add(criteriaBuilder.notEqual(timeStr, value));
						} else if (value instanceof Date) {
							predicatesList.add(criteriaBuilder.notEqual(path, (Date) value));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}

					} else {
						predicatesList.add(criteriaBuilder.notEqual(path, Convert.convert(javatype,value)));
					}
					break;
				case noteq_i:
					if (String.class.isAssignableFrom(javatype)) {
						predicatesList.add(
								criteriaBuilder.notEqual(criteriaBuilder.lower(path), value.toString().toLowerCase()));
					} else if (Date.class.isAssignableFrom(javatype)) {
						if (value instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, value);
							predicatesList.add(criteriaBuilder.notEqual(timeStr, value));
						} else if (value instanceof Date) {
							predicatesList.add(criteriaBuilder.notEqual(path, (Date) value));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}
					} else {
						predicatesList.add(criteriaBuilder.notEqual(path, Convert.convert(javatype,value)));
					}
					break;
				case gt:
					if (Number.class.isAssignableFrom(javatype) || ReflectionUtils.isPrimitiveNumber(value)) {
						predicatesList.add(criteriaBuilder.gt(path, (Number) Convert.convert(javatype,value)));
					} else if (Date.class.isAssignableFrom(javatype)) {
						// predicatesList.add(criteriaBuilder.greaterThan(path,(Date)Convert.convert(value,
						// javatype)));
						if (value instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, value);
							predicatesList.add(criteriaBuilder.greaterThan(timeStr, (String) value));
						} else if (value instanceof Date) {
							predicatesList.add(criteriaBuilder.greaterThan(path, (Date) value));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}
					} else {
						predicatesList.add(criteriaBuilder.greaterThan(path, param.getValue().toString()));
					}
					break;
				case ge:
					if (Number.class.isAssignableFrom(javatype) || ReflectionUtils.isPrimitiveNumber(value)) {
						predicatesList.add(criteriaBuilder.ge(path, (Number) Convert.convert(javatype,value)));
					} else if (Date.class.isAssignableFrom(javatype)) {
						// predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(path,(Date)Convert.convert(value,
						// javatype)));
						if (value instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, value);
							predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(timeStr, (String) value));
						} else if (value instanceof Date) {
							predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(path, (Date) value));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}
					} else {
						predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(path, param.getValue().toString()));
					}
					break;
				case lt:
					if (Number.class.isAssignableFrom(javatype) || ReflectionUtils.isPrimitiveNumber(value)) {
						predicatesList.add(criteriaBuilder.lt(path, (Number) Convert.convert(javatype,value)));
					} else if (Date.class.isAssignableFrom(javatype)) {
						// predicatesList.add(criteriaBuilder.lessThan(path,(Date)Convert.convert(value,
						// javatype)));
						if (value instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, value);
							predicatesList.add(criteriaBuilder.lessThan(timeStr, (String) value));
						} else if (value instanceof Date) {
							predicatesList.add(criteriaBuilder.lessThan(path, (Date) value));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}
					} else {
						predicatesList.add(criteriaBuilder.lessThan(path, param.getValue().toString()));
					}
					break;
				case le:
					if (Number.class.isAssignableFrom(javatype) || ReflectionUtils.isPrimitiveNumber(value)) {
						predicatesList.add(criteriaBuilder.le(path, (Number) Convert.convert(javatype,value)));
					} else if (Date.class.isAssignableFrom(javatype)) {
						// predicatesList.add(criteriaBuilder.lessThanOrEqualTo(path,(Date)Convert.convert(value,
						// javatype)));
						if (value instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, value);
							predicatesList.add(criteriaBuilder.lessThanOrEqualTo(timeStr, (String) value));
						} else if (value instanceof Date) {
							predicatesList.add(criteriaBuilder.lessThanOrEqualTo(path, (Date) value));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}
					} else {
						predicatesList.add(criteriaBuilder.lessThanOrEqualTo(path, param.getValue().toString()));
					}
					break;
				case between: {
					Object[] values = (Object[]) param.getValue();
					if (Number.class.isAssignableFrom(javatype) || ReflectionUtils.isPrimitiveNumber(value)) {
						predicatesList
								.add(criteriaBuilder.ge(path, (Number) Convert.convert(javatype,values[0])));
						predicatesList
								.add(criteriaBuilder.le(path, (Number) Convert.convert(javatype,values[1])));
					} else if (Date.class.isAssignableFrom(javatype)) {
						// predicatesList.add(criteriaBuilder.between(path,(Date)Convert.convert(values[0],
						// javatype),(Date)Convert.convert(values[1], javatype)));
						if (values[0] instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, values[0]);
							predicatesList
									.add(criteriaBuilder.between(timeStr, (String) values[0], (String) values[1]));
						} else if (values[0] instanceof Date) {
							predicatesList.add(criteriaBuilder.between(path, (Date) values[0], (Date) values[1]));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}
					} else {
						predicatesList.add(criteriaBuilder.between(path, values[0].toString(), values[1].toString()));
						// predicatesList.add(criteriaBuilder.greaterThanOrEqualTo(path,values[0].toString()));
						// predicatesList.add(criteriaBuilder.lessThanOrEqualTo(path,values[1].toString()));
					}
					break;
				}
				case in: {
					Object[] values = (Object[]) value;
					if (Date.class.isAssignableFrom(javatype)) {
						if (values[0] instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, values[0]);
							In in = criteriaBuilder.in(timeStr);
							for (Object obj : values) {
								in.value((String) obj);
							}
							predicatesList.add(in);
						} else if (values[0] instanceof Date) {
							In in = criteriaBuilder.in(path);
							for (Object obj : values) {
								in.value((Date) obj);
							}
							predicatesList.add(in);
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}
					} else {
						In in = criteriaBuilder.in(path);
						for (Object obj : values) {
							in.value(Convert.convert(javatype,obj));
						}
						predicatesList.add(in);
					}

					break;
				}
				case notin:
					Object[] values = (Object[]) value;
					if (Date.class.isAssignableFrom(javatype)) {
						if (values[0] instanceof String) {
							Expression<String> timeStr = getDateExpression(criteriaBuilder, path, values[0]);
							In in = criteriaBuilder.in(timeStr);
							for (Object obj : values) {
								in.value((String) obj);
							}
							predicatesList.add(criteriaBuilder.not(in));
						} else if (values[0] instanceof Date) {
							In in = criteriaBuilder.in(path);
							for (Object obj : values) {
								in.value((Date) obj);
							}
							predicatesList.add(criteriaBuilder.not(in));
						} else {
							throw new IllegalArgumentException(
									"参数" + param.getKey() + "类型不对,应该为日期字符串或Date类型!javatype=" + value);
						}
					} else {
						In in = criteriaBuilder.in(path);
						for (Object obj : values) {
							in.value(Convert.convert(javatype,obj));
						}
						predicatesList.add(criteriaBuilder.not(in));
					}
					break;
//		            	In in = criteriaBuilder.in(path);
//		            	Object[] values=(Object[])value;
//		            	for (Object obj:values) {
//		            		in.value(Convert.convert(obj, javatype));
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
					predicatesList
							.add(criteriaBuilder.like(criteriaBuilder.lower(path), value.toString().toLowerCase()));
					break;
				case likeprefix_i:
					predicatesList
							.add(criteriaBuilder.like(criteriaBuilder.lower(path), value.toString().toLowerCase()));
					break;
				case likesuffix_i:
					predicatesList
							.add(criteriaBuilder.like(criteriaBuilder.lower(path), value.toString().toLowerCase()));
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
				predicatesList.add(criteriaBuilder.equal(itemRoot.get(param.getKey()),
						Convert.convert(javatype,param.getValue())));
			}
			// i++;
			// predicatesList.add(criteriaBuilder.equal(itemRoot.get(param.getKey()),Convert.convert(param.getValue(),
			// javatype)));
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
//		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
//        TypedQuery typedQuery = em.createQuery(query);
//        return typedQuery;
	}

	public List listByMap(Class entityClass, Map<String, Object> params) {
		if (params == null) {
			return this.listAll(entityClass);
		}
		// TypedQuery typedQuery=genTypedQuery(entityClass,params);
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery query = criteriaBuilder.createQuery(entityClass);
		Root itemRoot = query.from(entityClass);

		Predicate[] predicatesList = genPredicates(criteriaBuilder, itemRoot, params);
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
//		Pageable pageable=PageRequest.of(pageinfo.getPage()-1, pageinfo.getLimit());
//		Page page=getSimpleJpaRepository(entityClass).findAll(example, pageable);
//		pageinfo.setTotal((int)page.getTotalElements());
//		pageinfo.setRoot(page.getContent());
//		return pageinfo;
//	}

	public Page listPageByExample(Class entityClass, Object params, int pageIndex, int limit) {
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher);
		Pageable pageable = PageRequest.of(pageIndex-1, limit);
		org.springframework.data.domain.Page page = getSimpleJpaRepository(entityClass).findAll(example, pageable);

		Page pageinfo = new Page();
		pageinfo.setPage(pageIndex);
		pageinfo.setLimit(limit);
		pageinfo.setTotal((int) page.getTotalElements());
		pageinfo.setRoot(page.getContent());
		//pageinfo.setParams(params);
		return pageinfo;
	}

	private class PageSpecification<T> implements Specification<T> {
		private Map<String, Object> params;

		public PageSpecification(Map<String, Object> params) {
			super();
			this.params = params;
		}

		@Override
		public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
			// List<Predicate> predicatesList = new ArrayList<Predicate>();
			Predicate[] predicatesList = new Predicate[0];
			if (params != null) {
//				predicatesList=new Predicate[params.size()];
//				int i=0;
//				for(Entry<String,Object> param:params.entrySet()) {   
//					Class javatype=root.get(param.getKey()).getJavaType();
//					predicatesList[i]=cb.equal(root.get(param.getKey()),Convert.convert(param.getValue(), javatype));
//					i++;
//					//predicatesList.add(cb.equal(root.get(param.getKey()),Convert.convert(param.getValue(), javatype)));
//				}
				predicatesList = genPredicates(cb, root, params);

			}
			Predicate p = cb.and(predicatesList);
			return p;

		}
	}

	public Page listPageByMap(Class entityClass, Map<String, Object> params, int pageIndex, int limit) {
		Pageable pageable = PageRequest.of(pageIndex-1, limit);

		PageSpecification spec = new PageSpecification(params);
		org.springframework.data.domain.Page page = getSimpleJpaRepository(entityClass).findAll(spec, pageable);

		Page pageinfo = new Page();
		pageinfo.setPage(pageIndex);
		pageinfo.setLimit(limit);
		pageinfo.setTotal((int) page.getTotalElements());
		pageinfo.setRoot(page.getContent());
		//pageinfo.setParams(params);

		return pageinfo;
	}

	public <T> Page<T> listPage(Class<T> entityClass, Cnd pageinfo) {
		Page<T> result=new Page<T>();
		result.setStart(pageinfo.getStart());
		result.setPage(pageinfo.getPage());
		result.setLimit(pageinfo.getLimit());
		Object params = pageinfo.getParams();
		Pageable pageable = PageRequest.of(pageinfo.getPage()-1, pageinfo.getLimit());
		if (params==null || params instanceof Map) {
			PageSpecification spec = new PageSpecification((Map) params);
			org.springframework.data.domain.Page page = getSimpleJpaRepository(entityClass).findAll(spec, pageable);
			result.setTotal((int) page.getTotalElements());
			result.setRoot(page.getContent());
		} else {
			ExampleMatcher matcher = ExampleMatcher.matching();
			Example example = Example.of(params, matcher);
			org.springframework.data.domain.Page page = getSimpleJpaRepository(entityClass).findAll(example, pageable);
			result.setTotal((int) page.getTotalElements());
			result.setRoot(page.getContent());
		}

		return result;
	}
//	public Page listPageByPage(Class entityClass, Page pageinfo) {
//		Object params = pageinfo.getParams();
//		Pageable pageable = PageRequest.of(pageinfo.getPage()-1, pageinfo.getLimit());
//		if (params==null || params instanceof Map) {
//			PageSpecification spec = new PageSpecification((Map) params);
//			org.springframework.data.domain.Page page = getSimpleJpaRepository(entityClass).findAll(spec, pageable);
//			pageinfo.setTotal((int) page.getTotalElements());
//			pageinfo.setRoot(page.getContent());
//		} else {
//			ExampleMatcher matcher = ExampleMatcher.matching();
//			Example example = Example.of(params, matcher);
//			org.springframework.data.domain.Page page = getSimpleJpaRepository(entityClass).findAll(example, pageable);
//			pageinfo.setTotal((int) page.getTotalElements());
//			pageinfo.setRoot(page.getContent());
//		}
//
//		return pageinfo;
//	}

	public Object update(Class entityClass, Object entity) {
		// SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		// return repository.u(entity);
		Object aaa = em.merge(entity);
		em.flush();
		return aaa;
	}

	public List updateBatch(Class entityClass, List list) {
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

	public List updateBatchByArray(Class entityClass, Object... list) {
		List result = new ArrayList();
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

	public int updateByMap(Class entityClass, Map<String, Object> sets, Map<String, Object> params) {
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
//			predicatesList[i]=cb.equal(root.get(param.getKey()),Convert.convert(param.getValue(), javatype));
//			i++;
//			//predicatesList.add(cb.equal(root.get(param.getKey()),Convert.convert(param.getValue(), javatype)));
//		}

		Predicate[] predicatesList = genPredicates(cb, root, params);
		update.where(predicatesList);

		for (Entry<String, Object> set : sets.entrySet()) {
			Class javatype = root.get(set.getKey()).getJavaType();
			update.set(root.get(set.getKey()), Convert.convert(javatype,set.getValue()));
		}

		Query query = em.createQuery(update);
		int rowCount = query.executeUpdate();
		em.clear();
		return rowCount;
		// repository.flush();
	}

	private Predicate[] getIdPredicates(Class entityClass, CriteriaBuilder cb, Root root, Object id) {
		JpaEntityInformation entityInformation = getEntityInformation(entityClass);
		Class id_javatype = entityInformation.getIdType();
		Iterable<String> idAttributeNames = entityInformation.getIdAttributeNames();
		if (!entityInformation.hasCompositeId()) {
			Predicate[] predicatesList = new Predicate[1];
			predicatesList[0] = cb.equal(root.get(idAttributeNames.iterator().next()),
					Convert.convert(id_javatype,id));
			return predicatesList;

		} else {
			Predicate[] predicatesList = new Predicate[((Collection) idAttributeNames).size()];
			int i = 0;
			for (String idAttributeName : idAttributeNames) {
				Object idAttributeValue = entityInformation.getCompositeIdAttributeValue(id, idAttributeName);

				// 当复合id是以一个单独的类的形式存在的时候
//				boolean complexIdParameterValueDiscovered = idAttributeValue != null&& !query.getParameter(idAttributeName).getParameterType().isAssignableFrom(idAttributeValue.getClass());
//				if (complexIdParameterValueDiscovered) {
//					update.where(cb.equal(root.get(idAttributeName),Convert.convert(id, id_javatype)));
//					break;
//				} 
				predicatesList[i] = cb.equal(root.get(idAttributeName),
						Convert.convert(id_javatype,idAttributeValue));
				i++;
			}
			return predicatesList;
		}
	}

	/**
	 * 已经支持复合主键
	 * 
	 * @param entityClass
	 * @param sets
	 * @param id
	 * @return
	 */
	public int updateById(Class entityClass, Map<String, Object> sets, Object id) {
		// JpaEntityInformation entityInformation=getEntityInformation(entityClass);

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate update = cb.createCriteriaUpdate(entityClass);

		Root root = update.from(entityClass);
		update.where(getIdPredicates(entityClass, cb, root, id));

//		Class id_javatype=entityInformation.getIdType();
//		Iterable<String> idAttributeNames = entityInformation.getIdAttributeNames();
//		if (!entityInformation.hasCompositeId()) {
//			update.where(cb.equal(root.get(idAttributeNames.iterator().next()),Convert.convert(id, id_javatype)));
//		} else {
//			Predicate[] predicatesList=new Predicate[((Collection)idAttributeNames).size()];
//			int i=0;
//			for (String idAttributeName : idAttributeNames) {
//				Object idAttributeValue = entityInformation.getCompositeIdAttributeValue(id, idAttributeName);
//
//				//当复合id是以一个单独的类的形式存在的时候
////				boolean complexIdParameterValueDiscovered = idAttributeValue != null&& !query.getParameter(idAttributeName).getParameterType().isAssignableFrom(idAttributeValue.getClass());
////				if (complexIdParameterValueDiscovered) {
////					update.where(cb.equal(root.get(idAttributeName),Convert.convert(id, id_javatype)));
////					break;
////				} 
//				predicatesList[i]=cb.equal(root.get(idAttributeName),Convert.convert(idAttributeValue, id_javatype));
//				i++;
//			}
//			update.where(predicatesList);	
//		}

		for (Entry<String, Object> set : sets.entrySet()) {
			Class javatype = root.get(set.getKey()).getJavaType();
			update.set(root.get(set.getKey()), Convert.convert(javatype,set.getValue()));
		}

		Query query = em.createQuery(update);
		int rowCount = query.executeUpdate();
		em.clear();
		return rowCount;

	}


	
	public static final String logicdelete_hql="update %s set %s =?0 where %s=?1";
	public static final String logicdeleteall_hql="update %s set %s =?0 ";
	public static final String delete_byid_hql="delete from %s where %s=?0 ";
	
	public int remove(Class entityClass, Object entity) {
		try {
			Loginc loginc = LogicDeleteInterceptor.getLoginc(entityClass);
			//逻辑删除
			if(loginc!=null) {
				JpaEntityInformation entityInformation = getEntityInformation(entityClass);
				//复合主键删除
				if(hasCompositeId(entityClass)) {
					List<String> ides=this.getIdAttributeNames(entityClass);
					StringBuilder hql=new StringBuilder(String.format("update %s set %s =?0 where ", entityClass.getName(),loginc.getName()));
					
					Object id_objs=em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
					int i=0;
					Object[] id_params=new Object[ides.size()];
					for(String id_str:ides) {
						if(i!=0) {
							hql.append(" and ");
						}
						hql.append(id_str+"=?"+(i+1));
						id_params[i]=entityInformation.getCompositeIdAttributeValue(id_objs, id_str);
					}
					Query query =em.createQuery(hql.toString());
					query.setParameter(0, loginc.getDeleteValue());
					for(int j=0;j<id_params.length;i++) {
						query.setParameter(j+1, id_params[j]);
					}
					int result= query.executeUpdate();
					if(Persistence.getPersistenceUtil().isLoaded(entity)) {
						em.refresh(entity);
					}
					return result;
					
				} else {
					//单主键删除
					Query query =em.createQuery(String.format(logicdelete_hql, entityClass.getName(),loginc.getName(),entityInformation.getIdAttribute().getName()));
					query.setParameter(0, loginc.getDeleteValue());
					
					Object id=em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
					query.setParameter(1, id);
					int result= query.executeUpdate();
					if(Persistence.getPersistenceUtil().isLoaded(entity)) {
						em.refresh(entity);
					}
					return result;
				}
			} else {
				//非逻辑删除
				SimpleJpaRepository repository = getSimpleJpaRepository(entityClass);
				repository.delete(entity);
				repository.flush();
			}
			
		} catch (Exception e) {
			logger.error("remove失败,entity=" + entity, e);
			return 0;
		}
		return 1;
	}
	public int removeBatch(Class entityClass, List list) {
		int result=0;
		for (int i = 0; i < list.size(); i++) {
			result+=this.remove(entityClass,list.get(i));
		}
		return result;
	}

	public int removeBatchByArray(Class entityClass, Object... list) {
		int result=0;
		for (int i = 0; i < list.length; i++) {
			result+=this.remove(entityClass,list[i]);
		}
		return result;
	}

	public int removeForce(Class entityClass, Object entity) {
		if(em.contains(entity)) {
			em.remove(entity);
			return 1;
		} else {
			//使用hql进行删除
			Object id=em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
			//防止爆，对象不存在的异常
			return removeForceById(entityClass,(Serializable)id);
			
		}
//		try {
//			//em.remove(em.contains(entity) ? entity : em.merge(entity));
//			//em.remove(entity);
//			SimpleJpaRepository repository = getSimpleJpaRepository(entityClass);
//			repository.delete(entity);
//			repository.flush();
//			return 1;
//		} catch (Exception e) {
//			logger.error("对象不存在",e);
//			return 0;
//		}
		
	}
	
	public int removeForceBatchByArray(Class entityClass, Object... list) {
		int result=0;
		for (int i = 0; i < list.length; i++) {
			result+=this.removeForce(entityClass,list[i]);
		}
		return result;
	}
	public int removeForceBatch(Class entityClass, List list) {
		int result=0;
		for (int i = 0; i < list.size(); i++) {
			result+=this.removeForce(entityClass,list.get(i));
		}
		return result;
	}
	
	public int removeForceById(Class entityClass, Serializable id) {
		int result=0;
//		Object entity=getById(entityClass,id);
//		if(entity!=null) {
//			em.remove(entity);
//			return 1;
//		}
		SimpleJpaRepository repository = getSimpleJpaRepository(entityClass);

		Optional optional = repository.findById(id);
		if (optional.isPresent()) {
			//return optional.get();
			em.remove(optional.get());
			return 1;
		} 

		JpaEntityInformation entityInformation = getEntityInformation(entityClass);
		if(hasCompositeId(entityClass)) {
			List<String> ides=this.getIdAttributeNames(entityClass);
			StringBuilder hql=new StringBuilder(String.format("delete from %s where ", entityClass.getName()));
	
			int i=0;
			Object[] id_params=new Object[ides.size()];
			for(String id_str:ides) {
				if(i!=0) {
					hql.append(" and ");
				}
				hql.append(id_str+"=?"+(i+1));
				id_params[i]=entityInformation.getCompositeIdAttributeValue(id, id_str);
			}
			Query query =em.createQuery(hql.toString());
			//query.setParameter(0, loginc.getDeleteValue());
			for(int j=0;j<id_params.length;i++) {
				query.setParameter(j+1, id_params[j]);
			}
			result= query.executeUpdate();

		} else {	
			Query query =em.createQuery(String.format(delete_byid_hql, entityClass.getName(),entityInformation.getIdAttribute().getName()));
			query.setParameter(0, id);
			result= query.executeUpdate();
		}
		
		return result;
	}
	public int removeById(Class entityClass, Serializable id) {
		int result=0;
		Loginc loginc = LogicDeleteInterceptor.getLoginc(entityClass);
		
		if(loginc!=null) {
			JpaEntityInformation entityInformation = getEntityInformation(entityClass);
			Query query =em.createQuery(String.format(logicdelete_hql, entityClass.getName(),loginc.getName(),entityInformation.getIdAttribute().getName()));
			query.setParameter(0, loginc.getDeleteValue());
			//Object id=em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
			query.setParameter(1, id);
			result= query.executeUpdate();

		} else {
			result=removeForceById(entityClass,id);
		}
		SimpleJpaRepository repository = getSimpleJpaRepository(entityClass);
		Optional optional = repository.findById(id);
		if (optional.isPresent()) {
			em.refresh(optional.get());
		} 
//		Object entity=em.getReference(entityClass, id);
//		if ( !(entity instanceof HibernateProxy) ) {
//			if(Persistence.getPersistenceUtil().isLoaded(entity)) {
//				em.refresh(entity);
//			}
//		} 
		return result;


	}

	public int removeAll(Class entityClass) {
		Loginc loginc = LogicDeleteInterceptor.getLoginc(entityClass);
		if(loginc!=null) {
			Query query =em.createQuery(String.format(logicdeleteall_hql, entityClass.getName(),loginc.getName()));
			query.setParameter(0, loginc.getDeleteValue());
			int result= query.executeUpdate();
			em.clear();
			return result;
		} else {
			return removeForceAll(entityClass);
		}
		//getSimpleJpaRepository(entityClass).deleteAllInBatch();// .deleteAll();
		//return true;
	}
	
	public int removeForceAll(Class entityClass) {
		int result= em.createQuery( String.format(QueryUtils.DELETE_ALL_QUERY_STRING, entityClass.getName())).executeUpdate();
		em.clear();
		return result;

	}

	public int removeByMap(Class entityClass, Map<String, Object> params) {
		Loginc loginc = LogicDeleteInterceptor.getLoginc(entityClass);
		//逻辑删除
		if(loginc!=null) {
			Map<String, Object> sets=new HashMap<String,Object>();
			sets.put(loginc.getName(), loginc.getDeleteValue());
			return this.updateByMap(entityClass, sets, params);
		} else {
			return removeForceByMap(entityClass,params);
		}

	}
	
	public int removeForceByMap(Class entityClass, Map<String, Object> params) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete update = cb.createCriteriaDelete(entityClass);
		Root root = update.from(entityClass);

		Predicate[] predicatesList = genPredicates(cb, root, params);
		update.where(predicatesList);

		Query query = em.createQuery(update);
		int rowCount = query.executeUpdate();
		em.clear();
		return rowCount;
	}



	public int removeByIds(Class entityClass, Serializable[] ids) {
		int result = 0;
		for (Serializable id : ids) {
			result += removeById(entityClass, id);
		}
		return result;
		
//		int result = 0;
//		SimpleJpaRepository repository = getSimpleJpaRepository(entityClass);
//		for (Serializable id : ids) {
//			result += removeById(entityClass, id);
//		}
//		return result;
	}
	public int removeByIds(Class entityClass, List<Serializable> ids) {
		int result = 0;
		for (Serializable id : ids) {
			result += removeById(entityClass, id);
		}
		return result;
		
//		int result = 0;
//		SimpleJpaRepository repository = getSimpleJpaRepository(entityClass);
//		for (Serializable id : ids) {
//			result += removeById(entityClass, id);
//		}
//		return result;
	}

	public long count(Class entityClass) {
		return getSimpleJpaRepository(entityClass).count();
	}

	public long countByExample(Class entityClass, Object params) {
		if (params == null) {
			return getSimpleJpaRepository(entityClass).count();
		}
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher);
		return getSimpleJpaRepository(entityClass).count(example);
		// return count(example);
	}

	public long countByMap(Class entityClass, Map<String, Object> params) {
		// SimpleJpaRepository repository=getSimpleJpaRepository(entityClass);
		// repository.count(spec)
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
		Root root = criteriaQuery.from(entityClass);
		JpaEntityInformation entityInformation = getEntityInformation(entityClass);

		Expression<Long> selection = cb.count(root.get(entityInformation.getIdAttribute()));
		criteriaQuery.select(selection);

		int i = 0;
		if (params != null && params.size() > 0) {
//			Predicate[] predicatesList = new Predicate[params.size()];
//
//			for (Entry<String, Object> param : params.entrySet()) {
//				Class javatype = root.get(param.getKey()).getJavaType();
//				predicatesList[i] = cb.equal(root.get(param.getKey()),
//						Convert.convert(param.getValue(), javatype));
//				i++;
//			}
			Predicate[] predicatesList = genPredicates(cb, root, params);
			criteriaQuery.where(predicatesList);
		}

		TypedQuery<Long> query = em.createQuery(criteriaQuery);

		return query.getSingleResult();

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
////					predicatesList[i]=cb.equal(root.get(param.getKey()),Convert.convert(param.getValue(), javatype));
////					i++;
////					//predicatesList.add(cb.equal(root.get(param.getKey()),Convert.convert(param.getValue(), javatype)));
////				}
////				
////			} 
////			Predicate p =cb.and(predicatesList);
////			return p;
//		}
//	}

	public boolean existsById(Class entityClass, Object id) {
		return getSimpleJpaRepository(entityClass).existsById(id);
		// return exists(example);
	}

	public boolean existsByExample(Class entityClass, Object params) {
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example example = Example.of(params, matcher);
		return getSimpleJpaRepository(entityClass).exists(example);
		// return exists(example);
	}

	public boolean existsByMap(Class entityClass, Map<String, Object> params) {
		long count = countByMap(entityClass, params);
		return count >= 1;
	}

	private BeanMap tuple2BeanMap(Tuple tuple, String[] fields) {
		BeanMap map = new BeanMap();
		for (int i = 0; i < fields.length; i++) {
			map.put(fields[i], tuple.get(i));
		}
		return map;
	}

	private BeanMap tuple2BeanMap(Object[] tuple, String[] fields) {
		BeanMap map = new BeanMap();
		for (int i = 0; i < fields.length; i++) {
			map.put(fields[i], tuple[i]);
		}
		return map;
	}

	public BeanMap getMapById(Class entityClass, Object id, String[] fields) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = cb.createQuery(Object[].class);
		Root root = criteriaQuery.from(entityClass);

		Selection[] selections = new Selection[fields.length];
		for (int i = 0; i < fields.length; i++) {
			selections[i] = root.get(fields[i]);
		}

		criteriaQuery.multiselect(selections).where(getIdPredicates(entityClass, cb, root, id));
		TypedQuery<Object[]> q = em.createQuery(criteriaQuery);

		return tuple2BeanMap(q.getSingleResult(), fields);

//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
//		Root root = criteriaQuery.from(entityClass);
//		
//		Selection[] selections=new Selection[fields.length];
//		for(int i=0;i<fields.length;i++) {
//			selections[i]=root.get(fields[i]).alias(fields[i]);
//		}
//		
//		criteriaQuery.multiselect(selections).where(cb.equal(root.get("id"), id));
//		TypedQuery<Tuple> q=em.createQuery(criteriaQuery);
//		
//		return tuple2BeanMap(q.getSingleResult(),fields); 
	}

	public BeanMap getMapByMap(Class entityClass, Map<String, Object> params, String... fields) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = cb.createQuery(Object[].class);
		Root root = criteriaQuery.from(entityClass);

		Selection[] selections = new Selection[fields.length];
		for (int i = 0; i < fields.length; i++) {
			selections[i] = root.get(fields[i]);
		}
		criteriaQuery.multiselect(selections);

		Predicate[] predicatesList = genPredicates(cb, root, params);
		criteriaQuery.where(predicatesList);
		TypedQuery<Object[]> typedQuery = em.createQuery(criteriaQuery);

		return tuple2BeanMap(typedQuery.getSingleResult(), fields);
	}

	public List<BeanMap> listMapByMap(Class entityClass, Map<String, Object> params, String... fields) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = cb.createQuery(Object[].class);
		Root root = criteriaQuery.from(entityClass);

		Selection[] selections = new Selection[fields.length];
		for (int i = 0; i < fields.length; i++) {
			selections[i] = root.get(fields[i]);
		}
		criteriaQuery.multiselect(selections);

		Predicate[] predicatesList = genPredicates(cb, root, params);
		criteriaQuery.where(predicatesList);
		TypedQuery<Object[]> typedQuery = em.createQuery(criteriaQuery);

		List<Object[]> list = typedQuery.getResultList();
		List<BeanMap> result = new ArrayList<BeanMap>(list.size());
		for (Object[] objArry : list) {
			result.add(tuple2BeanMap(objArry, fields));
		}
		return result;
	}

//	public Long sumAsLong(Class entityClass,String field,Map<String,Object> params) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
//		Root root = criteriaQuery.from(entityClass);
//		
//		criteriaQuery.select(cb.sumAsLong(root.get(field)));
//		
//		Predicate[] predicatesList=genPredicates(cb,root,params);
//		criteriaQuery.where(predicatesList);
//		
//		TypedQuery typedQuery = em.createQuery(criteriaQuery);
//		return (Long)typedQuery.getSingleResult();
//		
//	}
//	public Double sumAsDouble(Class entityClass,String field,Map<String,Object> params) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Double> criteriaQuery = cb.createQuery(Double.class);
//		Root root = criteriaQuery.from(entityClass);
//		
//		criteriaQuery.select(cb.sumAsDouble(root.get(field)));
//		
//		Predicate[] predicatesList=genPredicates(cb,root,params);
//		criteriaQuery.where(predicatesList);
//		
//		TypedQuery typedQuery = em.createQuery(criteriaQuery);
//		return (Double)typedQuery.getSingleResult();
//		
//	}
}
