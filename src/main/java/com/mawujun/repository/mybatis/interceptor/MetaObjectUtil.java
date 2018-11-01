package com.mawujun.repository.mybatis.interceptor;

import java.lang.reflect.Method;

import org.apache.ibatis.reflection.MetaObject;

public class MetaObjectUtil {
	 public static Method method;

	    static {
	        try {
	            // 高版本中的 MetaObject.forObject 有 4 个参数，低版本是 1 个
	            // 下面这个 MetaObjectWithReflectCache 带反射的缓存信息
	            Class<?> metaClass = MetaObjectWithReflectCache.class;
	            method = metaClass.getDeclaredMethod("forObject", Object.class);
	        } catch (Throwable e1) {
	            try {
	                Class<?> metaClass = MetaObject.class;//Class.forName("org.apache.ibatis.reflection.MetaObject");
	                method = metaClass.getDeclaredMethod("forObject", Object.class);
	            } catch (Exception e2) {
	                throw new PageException(e2);
	            }
	        }

	    }

	    public static MetaObject forObject(Object object) {
	        try {
	            return (MetaObject) method.invoke(null, object);
	        } catch (Exception e) {
	            throw new PageException(e);
	        }
	    }
}
