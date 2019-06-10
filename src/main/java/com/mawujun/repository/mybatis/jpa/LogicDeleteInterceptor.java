package com.mawujun.repository.mybatis.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;

import org.hibernate.EmptyInterceptor;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mawujun.convert.Convert;
import com.mawujun.generator.annotation.LogicDelete;
import com.mawujun.util.ReflectUtil;

public class LogicDeleteInterceptor extends EmptyInterceptor {
	private static Logger logger=LoggerFactory.getLogger(LogicDeleteInterceptor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private static Map<Class,Stirng> 

	@Override
	public boolean onSave(
			Object entity, 
			Serializable id, 
			Object[] state, 
			String[] propertyNames, 
			Type[] types) {
		
		
		
		
		return setLogicDelete(entity,state,propertyNames);
	}
	
	@Override
	public boolean onFlushDirty(
			Object entity, 
			Serializable id, 
			Object[] currentState, 
			Object[] previousState, 
			String[] propertyNames, 
			Type[] types) {
//		System.out.println("===================================");
//		System.out.println("===================================");
//		System.out.println("===================================");
//		System.out.println("===================================");
//		System.out.println("===================================");
		return setLogicDelete(entity,currentState,propertyNames);
	}
	
	private boolean setLogicDelete(Object entity,Object[] state, String[] propertyNames ) {
		if(notcontainLoginDeletefield.contains(entity.getClass())) {
			return false;
		} else if(containLoginDeletefield.containsKey(entity.getClass())) {
			Loginc loginc=containLoginDeletefield.get(entity.getClass());
			for (int index=0;index<propertyNames.length;index++) {
                if (loginc.getName().equals(propertyNames[index]))
                {
                	if(state[index]==null) {
                		logger.info("设置{}逻辑删除的默认值为0",entity.getClass());
                		state[index] = loginc.getDefaultValue();
                		return true;
                	}
                }
            }
		}
		return false;
	}
	
	private static Set<Class> notcontainLoginDeletefield=new HashSet<Class>();
	private static Map<Class,Loginc> containLoginDeletefield=new HashMap<Class,Loginc>();
	
	public void searchLogiceleteField(SessionFactoryImpl sessionFactory) {
		logger.info("开始判断哪些实体类是逻辑删除的");
		Set<EntityType<?>> entitytypes=sessionFactory.getMetamodel().getEntities();
		//System.out.println(entitytypes);
		boolean conttain=false;
		for(EntityType<?> entityType:entitytypes) {
			Class claz=entityType.getJavaType();
			conttain=false;
			
			Set<?> attributes=entityType.getAttributes();//entityType.getSingularAttributes();//(Set<Attribute<Object,Object>>)entityType.getAttributes();
			for(Object obj:attributes) {
				Attribute attr=(Attribute)obj;
					Field field=ReflectUtil.getField(claz,attr.getName());
					if(field==null) {
						continue;
					}
					LogicDelete logicDelete=field.getAnnotation(LogicDelete.class);
					if(logicDelete!=null) {
						conttain=true;
						Loginc loginc=new Loginc();
						loginc.setName(attr.getName());
						loginc.setDefaultValue(Convert.convert(attr.getJavaType(),logicDelete.defaultValue()));
						loginc.setDeleteValue(Convert.convert(attr.getJavaType(),logicDelete.deleteValue()));
						containLoginDeletefield.put(claz,loginc);
						break;
					}
//				
				
			}
			if(!conttain) {
				notcontainLoginDeletefield.add(claz);
			}
			
		}
		
	}
	/**
	 * 获取该实体类 是否是逻辑删除的
	 * @param clazz
	 * @return
	 */
	public static Loginc getLoginc(Class clazz) {
		return containLoginDeletefield.get(clazz);
	}

}
