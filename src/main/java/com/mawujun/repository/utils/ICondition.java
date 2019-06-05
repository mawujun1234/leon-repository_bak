package com.mawujun.repository.utils;

import java.util.Map;

/**
 * 参数接口
 * @author admin
 *
 */
public interface ICondition {

//	/**
//	 * 如果不存在，默认是eq
//	 * @param key
//	 * @return
//	 */
//	OpEnum getOpEnum(String key);

	//==============================================
	/**
	 * 默认的操作符是“=”
	 * 如果值为null，就不会添加这个条件
	 */
	ICondition put(String key, Object value);
	//	@Override
	//	public void putAll(Map<? extends String, ? extends Object> m) {
	//		// TODO Auto-generated method stub
	//		this.putAll(m);
	//	}

	//Map<String, Object> getParams();

	/**
	 *等于eq方法,
	 *如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition add(String key, Object value);

	/**
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param opEnum
	 * @param value
	 * @return
	 */
	ICondition add(String key, OpEnum opEnum, Object value);
	/**
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition eq(String key, Object value);

	/**
	 * 忽略大小写的比较,如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition eq_i(String key, Object value);
	/**
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition noteq(String key, Object value);

	/**
	 * 忽略大小写
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition noteq_i(String key, Object value);

	/**
	 * 大于
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition gt(String key, Object value);

	/**
	 * 大于等于
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition ge(String key, Object value);

	/**
	 * 小于
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition lt(String key, Object value);

	/**
	 * 小于等于
	 * .如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition le(String key, Object value);
	/**
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value1
	 * @param value2
	 * @return
	 */
	ICondition between(String key, Object value1, Object value2);
	/**
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition in(String key, Object... value);
	/**
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition notin(String key, Object... value);
	/**
	 * 
	 * @param key
	 * @return
	 */
	ICondition isnull(String key);
	/**
	 * 如果值为null，就添加条件is null，如果不为null，就等于eq
	 * @param key
	 * @return
	 */
	ICondition isnull(String key,Object value);
	/**
	 * 如果值为null，就添加条件is not null，如果不为null，就等于noteq
	 * @param key
	 * @return
	 */
	ICondition isnotnull(String key,Object value);
	/**
	 * 如果值为null，就添加条件is not null，如果不为null，就等于noteq
	 * @param key
	 * @return
	 */
	ICondition isnotnull(String key);

	/**
	 * 两端like,"%"+value+"%"
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition like(String key, String value);

	/**
	 * 前端匹配，"%"+value
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition likeprefix(String key, String value);

	/**
	 * 后端匹配，value+"%"
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition likesuffix(String key, String value);

	/**
	 * 两端like,"%"+value+"%",或略大小写
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition like_i(String key, String value);

	/**
	 * 前端匹配，"%"+value,或略大小写
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition likeprefix_i(String key, String value);

	/**
	 * 后端匹配，value+"%",或略大小写
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition likesuffix_i(String key, String value);

	/** 两端like,"%"+value+"%"
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition notlike(String key, String value);

	/**
	 * 前端匹配，"%"+value
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition notlikeprefix(String key, String value);

	/**
	 * 后端匹配，value+"%"
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition notlikesuffix(String key, String value);

//	/**
//		 * 会把key相同的值组装成sql中in的形式,'a','b','c'
//		 * 同个key不能和其他方法混合调用，但是同个key可以多次调用addin()方法.addIn(....).addIn(...)
//		 * @param key
//		 * @param value
//		 * @return
//		 */
//	IParams addIn(String key, Object... values);
	//	/**
	//	 * 会再value的两边都加上%号
	//	 * @param key
	//	 * @param value
	//	 * @return
	//	 */
	//	public Params addLike(String key,Object value) {
	//		if(value!=null) {
	//			this.add(key, "%"+value.toString()+"%");
	//		}
	//		return this;
	//	}

}