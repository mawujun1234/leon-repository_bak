package com.mawujun.repository.mybatis.jpa;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.session.SqlSession;

import com.mawujun.exception.BizException;
import com.mawujun.mvc.SpringContextUtils;
import com.mawujun.repository.utils.Condition;
import com.mawujun.repository.utils.Page;
import com.mawujun.repository.utils.PageMethodCache;
import com.mawujun.utils.ReflectionUtils;

/**
 * z这个类也可以自己扩展，例如要把某些逻辑写在持久层里的时候，可以为某些类专门指定使用哪个Proxy
 * @author mawujun 16064988@qq.com  
 *
 * @param <T>
 */
public class JpaMapperProxy<T> extends MapperProxy<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private JpaDao newdao;
	protected Class<T> entityClass;
	private final SqlSession sqlSession_;
	
	/**
	 * 之初始化一次
	 * @param sqlSession
	 * @param mapperInterface
	 * @param methodCache
	 */
	public JpaMapperProxy(SqlSession sqlSession, Class<T> mapperInterface,
			Map<Method, MapperMethod> methodCache) {
		super(sqlSession, mapperInterface, methodCache);
		sqlSession_=sqlSession;
		
		//this.newdao=SpringContextUtils.getBean(JpaDao.class);
		this.entityClass = ReflectionUtils.getGenericInterfaces(mapperInterface,0);
	}
	public JpaDao getJpaDao() {
		if(this.newdao==null) {
			this.newdao=SpringContextUtils.getBean(JpaDao.class);
		}
		return this.newdao;
	}

	//存储已经判断过的方法,为了提高性能
	private Set<String> exist_look=new HashSet<String>();
	/**
	 * 在这里进行拦截，如果是指定的方法就
	 * 额外的处理可以做成处理类，例如建一个InvokeHandler处理类，根据各种规则把处理内容发送到这个类里面进行处理，
	 * 这样的话MyMapperProxy就不需要依赖SessionFactory，MybatisRepository这些类了，直接在外面配置就可以了
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		JpaMethod jpaMethod=method.getAnnotation(JpaMethod.class);
		if(jpaMethod==null) {
			//是mybatis的时候，同时判断该方法是否是分页方法，供后面PageInfoInterceptor使用
			Class return_clazz=method.getReturnType();
            //只要返回值是Page，就表示进行分页查询
			String key=method.getDeclaringClass().getCanonicalName()+"."+method.getName();	
			if(!exist_look.contains(key)) {
				exist_look.add(key);
	            if( Page.class.isAssignableFrom(return_clazz)) {
	//            	System.out.println(method.getDeclaringClass().getCanonicalName());
	//            	System.out.println(method.getClass());	
	            	PageMethodCache.add(key);
	            }
			}
            
			
			//如果是调用mybatis中的方法，就直接返回
			return super.invoke(proxy, method, args);
		}
		if(method.getName().equals("getById")) {
			sqlSession_.clearCache();
			return getJpaDao().getById(entityClass, args[0]);
		} 
		else if(method.getName().equals("create")) {
			if(args[0]==null) {
				throw new BizException("create参数不能为null");
			} if(entityClass.isInstance(args[0])) {
				return getJpaDao().create(entityClass,args[0]);
			} if(args[0] instanceof Map) {
				return getJpaDao().create(entityClass,(Map<String,Object>)args[0]);
			} else if(args[0] instanceof List) {
				return getJpaDao().createBatch(entityClass,(List)args[0]);
			} else {
				return getJpaDao().createBatchByArray(entityClass,(Object[])args[0]);
			}
			 
		} 
		else if(method.getName().equals("save")) {
			if(args[0]==null) {
				throw new BizException("create参数不能为null");
			} if(entityClass.isInstance(args[0])) {
				return getJpaDao().save(entityClass,args[0]);
			} else if(args[0] instanceof List) {
				return getJpaDao().saveBatch(entityClass,(List)args[0]);
			} else {
				return getJpaDao().saveBatchByArray(entityClass,(Object[])args[0]);
			}	 
		} 
//		else if(method.getName().equals("create")) {
//		 return getJpaDao().create(entityClass,args[0]);
//	} 
//		else if(method.getName().equals("createBatch")) {
//			 return getJpaDao().createBatch(entityClass,(List)args[0]);
//		} 
//		else if(method.getName().equals("createBatchByArray")) {
//			 return getJpaDao().createBatchByArray(entityClass,(Object[])args[0]);
//		}
//		else if(method.getName().equals("save")) {
//			 return getJpaDao().save(entityClass,args[0]);
//		} else if(method.getName().equals("saveBatch")) {
//			 return getJpaDao().saveBatch(entityClass,(List)args[0]);
//		} else if(method.getName().equals("saveBatchByArray")) {
//			 return getJpaDao().saveBatchByArray(entityClass,(Object[])args[0]);
//		}
		
//		else if(method.getName().equals("getByMap"))  {
//			return getJpaDao().getByMap(entityClass, (Map<String,Object>)args[0]);
//		} else if(method.getName().equals("getByExample"))  {
//			return getJpaDao().getByExample(entityClass, args[0]);
//		} 
		else if(method.getName().equals("get"))  {
			if(args[0]==null || args[0] instanceof Map) {
				return getJpaDao().getByMap(entityClass, (Map<String,Object>)args[0]);
			} else {
				return getJpaDao().getByExample(entityClass, args[0]);
			}
			
		} 
//		else if(method.getName().equals("listByExample"))  {
//			return getJpaDao().listByExample(entityClass, args[0]);
//		}  else if(method.getName().equals("listByMap"))  {
//			return getJpaDao().listByMap(entityClass, (Map<String,Object>)args[0]);
//		} 
		else if(method.getName().equals("list"))  {
			if(args==null) {
				return getJpaDao().listAll(entityClass);
			} else if(args[0]==null || args[0] instanceof Map) {
				return getJpaDao().listByMap(entityClass, (Map<String,Object>)args[0]);
			} else {
				return getJpaDao().listByExample(entityClass, args[0]);
			}
			
		}  else if(method.getName().equals("listByMap"))  {
			
		} 
//		else if(method.getName().equals("listAll"))  {
//			return getJpaDao().listAll(entityClass);
//		} 
//		else if(method.getName().equals("listPageByExample"))  {
//			return getJpaDao().listPageByExample(entityClass, args[0],(int)args[1],(int)args[2]);
//		}
//		else if(method.getName().equals("listPageByMap"))  {
//			return getJpaDao().listPageByMap(entityClass, (Map<String,Object>)args[0],(int)args[1],(int)args[2]);
//		} 
		else if(method.getName().equals("page"))  {
			if(args.length==3) {
				if(args[0]==null || args[0] instanceof Map) {
					return getJpaDao().listPageByMap(entityClass, (Map<String,Object>)args[0],(int)args[1],(int)args[2]);
				} else {
					return getJpaDao().listPageByExample(entityClass, args[0],(int)args[1],(int)args[2]);
				}
				
			}
			if(args[0] instanceof Condition) {
				return getJpaDao().listPage(entityClass, (Condition)args[0]);
			}
			
		} 
		
//		else if(method.getName().equals("update"))  {
//			return getJpaDao().update(entityClass, args[0]);
//		} 
//		else if(method.getName().equals("updateBatch"))  {
//			return getJpaDao().updateBatch(entityClass, (List)args[0]);
//		}  else if(method.getName().equals("updateBatchByArray"))  {//数组
//			return getJpaDao().updateBatchByArray(entityClass, (Object[])args[0]);
//		}  else if(method.getName().equals("updateByMap"))  {
//		return getJpaDao().updateByMap(entityClass,(Map<String,Object>)args[0],(Map<String,Object>)args[1]);
//	} 
		else if(method.getName().equals("update"))  {
			if(args[0]==null) {
				throw new BizException(method.getName()+"参数不能为null");
			} else if(args.length==2) {
				return getJpaDao().updateByMap(entityClass,(Map<String,Object>)args[0],(Map<String,Object>)args[1]);
			} else if(args.length==1 && args[0] instanceof Condition) {
				Condition cnd=(Condition)args[0];
				if(cnd.getUpdatefields()==null || cnd.getUpdatefields().size()==0) {
					throw new BizException("请调用Condition.update()方法添加更新字段");
				}
				Object obj=cnd.getParams();
				if(obj==null || !(obj instanceof Map) ||((Map)obj).size()==0) {
					throw new BizException("请调用Condition的方法添加更新条件");
				}
				return getJpaDao().updateByMap(entityClass,cnd.getUpdatefields(),cnd);
			} else if (entityClass.isInstance(args[0])) {
				return getJpaDao().update(entityClass, args[0]);
			} else if(args[0] instanceof List) {
				return getJpaDao().updateBatch(entityClass,(List)args[0]);
			} else {
				return getJpaDao().updateBatchByArray(entityClass,(Object[])args[0]);
			}	 
		}  
		else if(method.getName().equals("updateById"))  {
			return getJpaDao().updateById(entityClass,(Map<String,Object>)args[0],args[1]);
		}  
		
//		else if(method.getName().equals("remove"))  {
//			 return getJpaDao().remove(entityClass, args[0]);
//		} else if(method.getName().equals("removeByMap"))  {
//			return getJpaDao().removeByMap(entityClass, (Map<String,Object>)args[0]);
//		} 
		else if(method.getName().equals("remove"))  {
			if(args[0]==null) {
				throw new BizException("删除的时候参数不能为null!");
			} else if(entityClass.isInstance(args[0])) {
				return getJpaDao().remove(entityClass, args[0]);
			} else if(args[0] instanceof List) {
				return getJpaDao().removeBatch(entityClass,(List)args[0]);
			} else if(args[0] instanceof Map) {
				return getJpaDao().removeByMap(entityClass, (Map<String,Object>)args[0]);
			} else {
				return getJpaDao().removeBatchByArray(entityClass,(Object[])args[0]);
			} 
			 
		} 
		if(method.getName().equals("removeAll"))  {
			 return getJpaDao().removeAll(entityClass);
		} else if(method.getName().equals("removeById"))  {
			return getJpaDao().removeById(entityClass, (Serializable)args[0]);
		} else if(method.getName().equals("removeByIds"))  {
			if(args[0] instanceof List) {
				return getJpaDao().removeByIds(entityClass, (List<Serializable>)args[0]);
			} else {
				return getJpaDao().removeByIds(entityClass, (Serializable[])args[0]);
			}
			
		} 
//		else if(method.getName().equals("removeForce"))  {
//			return getJpaDao().removeForce(entityClass, args[0]);
//		}  else if(method.getName().equals("removeForceByMap"))  {
//			return getJpaDao().removeForceByMap(entityClass, (Map<String,Object>)args[0]);
//		} 
		else if(method.getName().equals("removeForce"))  {
			if(args[0]==null) {
				throw new BizException("removeForce删除的时候参数不能为null!");
			}else if(args[0] instanceof Map) {
				return getJpaDao().removeForceByMap(entityClass, (Map<String,Object>)args[0]);
			} else if(entityClass.isInstance(args[0])) {
				return getJpaDao().removeForce(entityClass, args[0]);
			} else if(args[0] instanceof List) {
				return getJpaDao().removeForceBatch(entityClass,(List)args[0]);
			} else {
				return getJpaDao().removeForceBatchByArray(entityClass,(Object[])args[0]);
			}
			
		} 
		else if(method.getName().equals("removeForceById"))  {
			return getJpaDao().removeForceById(entityClass, (Serializable)args[0]);
		} else if(method.getName().equals("removeForceAll"))  {
			return getJpaDao().removeForceAll(entityClass);
		}  
		
		
		else if(method.getName().equals("count"))  {
			if(args==null) {
				return getJpaDao().count(entityClass);
			} else if(args[0]==null || args[0] instanceof Map) {
				return getJpaDao().countByMap(entityClass, (Map<String,Object>)args[0]);
			} else {
				return getJpaDao().countByExample(entityClass, args[0]);
			}
			
		} 
//		 else if(method.getName().equals("count"))  {
//				return getJpaDao().count(entityClass);
//		}	else if(method.getName().equals("countByExample"))  {
//			return getJpaDao().countByExample(entityClass, args[0]);
//		} else if(method.getName().equals("countByMap"))  {
//			return getJpaDao().countByMap(entityClass, (Map<String,Object>)args[0]);
//		} 
		
		else if(method.getName().equals("existsById"))  {
			return getJpaDao().existsById(entityClass, args[0]);
		} else if(method.getName().equals("exists"))  {
			if(args[0]==null || args[0] instanceof Map) {
				return getJpaDao().existsByMap(entityClass, (Map<String,Object>)args[0]);
			} else {
				return getJpaDao().existsByExample(entityClass, args[0]);
			}		
		}
//		else if(method.getName().equals("existsByExample"))  {
//			return getJpaDao().existsByExample(entityClass, args[0]);
//		} else if(method.getName().equals("existsByMap"))  {
//			return getJpaDao().existsByMap(entityClass, (Map<String,Object>)args[0]);
//		} 
		
		else if(method.getName().equals("getIdType"))  {
			return getJpaDao().getIdType(entityClass);
		} else if(method.getName().equals("getIdAttributeNames"))  {
			return getJpaDao().getIdAttributeNames(entityClass);
		} else if(method.getName().equals("getIdAttributeNames2Str"))  {
			return getJpaDao().getIdAttributeNames2Str(entityClass);
		} 
		
		else if (method.getName().equals("getMapById")){
			return getJpaDao().getMapById(entityClass,args[0],(String[])args[1]);
		} else if (method.getName().equals("getMap")){
			return getJpaDao().getMapByMap(entityClass,(Map<String,Object>)args[0],(String[])args[1]);
		}else if (method.getName().equals("listMap")){
			return getJpaDao().listMapByMap(entityClass,(Map<String,Object>)args[0],(String[])args[1]);
		}
		
//		else if (method.getName().equals("sumAsLong")){
//			return getJpaDao().sumAsLong(entityClass,(String)args[0],(Map<String,Object>)args[2]);
//		} else if (method.getName().equals("sumAsDouble")){
//			return getJpaDao().sumAsDouble(entityClass,(String)args[0],(Map<String,Object>)args[2]);
//		}
		
		else if (method.getName().equals("clear")){
			getJpaDao().clear();
			return true;
		}
		
		
		return super.invoke(proxy, method, args);
//		//继承自IHibernateRepository的方法就在hibernate中执行，如果在子接口中重载了的话，就不会进入这里了
//		if(method.getDeclaringClass()==IHibernateDao.class) {
//			return invokeByHibernate(proxy, method, args);
//		} else {
//			Class[] paramTypes=method.getParameterTypes();
//			//如果是分页的话
//			if(paramTypes.length==1 && paramTypes[0]==Page.class && method.getReturnType()==Page.class){
//				return mybatisRepository.selectPage(namespace+"."+method.getName(), (Page)args[0]);
//			} else {
//				 return super.invoke(proxy, method, args);
//			}
//			
//		}
	   
	}
	
	
//	private static final Map<String, Method> hibernateMethodCache=new ConcurrentHashMap<String, Method>();
//	public Object invokeByHibernate(Object proxy, Method method, Object[] args) throws Throwable {
//		//method.i
//		//Method[] methods=HibernateInvoke.class.getMethods();
//		
//		
//		Method hMethod=getMethod(method.getName(),args);
//		return hMethod.invoke(hibernateDao, args);
//		
//	}
//	
//	private String getKey(String name, Class[] paramTypes){
//		StringBuilder key=new StringBuilder(name);
//		for(Class paramstype:paramTypes){
//			key.append(paramstype.getName());
//		}
//		return key.toString();
//	}
//	/**
//	 * 如果没有和参数什么完全匹配的方法，就返回最后一个名称和参数个数匹配的方法
//	 * @author mawujun email:160649888@163.com qq:16064988
//	 * @param name
//	 * @param paramTypes
//	 * @return
//	 * @throws SecurityException 
//	 * @throws NoSuchMethodException 
//	 */
//	public  Method getMethod(String name, Object[] args) throws NoSuchMethodException, SecurityException {
//		//AssertUtils.notNull(clazz, "Class must not be null");
//		AssertUtils.notNull(name, "Method name must not be null");
//		Class searchType = HibernateDao.class;
//		//Class searchType = HibernateInvoke.class;
//		//没有参数的方法
//		if(args==null){
//			Method temp=hibernateMethodCache.get(name);
//			if(temp==null){
//				temp=searchType.getMethod(name);
//			}
//			hibernateMethodCache.put(name,temp);
//			return temp;
//		}
//		
//		Class[] paramTypes=new Class[args.length];
//		for(int i=0;i<args.length;i++){
//			Object o=args[i];
//			paramTypes[i]=o.getClass();
//		}
//		
//		
//		String key=getKey(name,paramTypes);
//		Method temp=hibernateMethodCache.get(key);
//		if(temp!=null){
//			return temp;
//		}
//		
//		
//		List<Method> temps=new ArrayList<Method>();
//		while (!Object.class.equals(searchType) && searchType != null) {
//			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
//			for (int i = 0; i < methods.length; i++) {
//				Method method = methods[i];
//				if (name.equals(method.getName()) ) {
//					if((paramTypes == null || paramTypes.length==0) && method.getParameterTypes().length==0){
//						return method;
//					} else {
//						int isSame= isSameParames(paramTypes,method.getParameterTypes());
//						if(isSame==1){
//							//temp=method;
//							temps.add(method);
//						} else if(isSame==2){
//							hibernateMethodCache.put(key,method);
//							return method;
//						}
//					}
//				}
//			}
//			searchType = searchType.getSuperclass();
//		}
//		
//		//当有多个方法都匹配的时候，获取继承结构中 最接近参数的那层的方法
//		if(temps.size()==1){
//			temp=temps.get(0);
//		} else {
//			Method standerMethod =temps.get(0);	
//			for(int i=1;i<temps.size();i++){
//				Class[] standerMethodParamTypes=standerMethod.getParameterTypes();
//				Class[] targetMethodParamTypes=temps.get(i).getParameterTypes();
//				
//				//判断参数是否一致
//				for(int j=0;j<standerMethodParamTypes.length;j++){	
//					if(standerMethodParamTypes[j]==targetMethodParamTypes[j]){	
//						continue;
//					} else if(standerMethodParamTypes[j].isAssignableFrom(targetMethodParamTypes[j])){
//						standerMethod=temps.get(i);
//						break;
//					} else if (standerMethodParamTypes[j].isArray() && targetMethodParamTypes[j].isArray()) {
//						if (standerMethodParamTypes[j].getComponentType() ==targetMethodParamTypes[j].getComponentType()) {
//							continue;
//						} else if (standerMethodParamTypes[j].getComponentType().isAssignableFrom(targetMethodParamTypes[j].getComponentType())) {
//							standerMethod=temps.get(i);
//							break;
//						} 
//					}
//				}
//			}
//			temp=standerMethod;
//		}	
//		hibernateMethodCache.put(key,temp);
//		return temp;
//	}
//	
//	/***
//	 * 0表示不匹配
//	 * 1：表示是Object的匹配，那要把这个方法先缓存
//	 * 2：表示是完全匹配，直接可以返回该方法
//	 * 
//	 * 考虑多个参数
//	 * @author mawujun email:160649888@163.com qq:16064988
//	 * @param paramTypes
//	 * @param methodParamTypes
//	 * @return
//	 */
//	public int isSameParames(Class[] paramTypes,Class[] methodParamTypes){
//		if(paramTypes.length!=methodParamTypes.length){
//			return 0;
//		}
//		int isSame=2;
//		for(int i=0;i<paramTypes.length;i++){
//			//methodParamTypes[i]==Serializable.class用于判断id的时候
//			//if(methodParamTypes[i]==Object.class || methodParamTypes[i]==Serializable.class){
//			if(methodParamTypes[i]==paramTypes[i]){	
//				continue;
//			} else if(methodParamTypes[i].isAssignableFrom(paramTypes[i])){
//				isSame=1;
//			} else if (methodParamTypes[i].isArray() && paramTypes[i].isArray()) {
//				if (methodParamTypes[i].getComponentType() == paramTypes[i].getComponentType()) {
//					continue;
//				//} else if (paramTypes[i].getComponentType() == Object.class|| methodParamTypes[i].getComponentType() == Serializable.class) {
//				} else if (methodParamTypes[i].getComponentType().isAssignableFrom(paramTypes[i].getComponentType())) {
//					isSame = 1;
//				} else {
//					isSame=0;
//					return isSame;
//				}
//			} else {
//				return 0;
//			}
//		}
//		return isSame;
//	}

}
