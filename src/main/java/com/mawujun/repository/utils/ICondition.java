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
	 */
	ICondition put(String key, Object value);
	//	@Override
	//	public void putAll(Map<? extends String, ? extends Object> m) {
	//		// TODO Auto-generated method stub
	//		this.putAll(m);
	//	}

	//Map<String, Object> getParams();

	/**
	 *等于eq方法
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition add(String key, Object value);

	ICondition add(String key, OpEnum opEnum, Object value);

	ICondition eq(String key, Object value);

	/**
	 * 忽略大小写的比较
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition eq_i(String key, Object value);

	ICondition noteq(String key, Object value);

	/**
	 * 忽略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition noteq_i(String key, Object value);

	/**
	 * 大于
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition gt(String key, Object value);

	/**
	 * 大于等于
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition ge(String key, Object value);

	/**
	 * 小于
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition lt(String key, Object value);

	/**
	 * 小于等于
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition le(String key, Object value);

	ICondition between(String key, Object value1, Object value2);

	ICondition in(String key, Object... value);

	ICondition notin(String key, Object... value);

	ICondition isnull(String key);

	ICondition isnotnull(String key);

	/**
	 * 两端like,"%"+value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition like(String key, String value);

	/**
	 * 前端匹配，"%"+value
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition likeprefix(String key, String value);

	/**
	 * 后端匹配，value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition likesuffix(String key, String value);

	/**
	 * 两端like,"%"+value+"%",或略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition like_i(String key, String value);

	/**
	 * 前端匹配，"%"+value,或略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition likeprefix_i(String key, String value);

	/**
	 * 后端匹配，value+"%",或略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition likesuffix_i(String key, String value);

	/** 两端like,"%"+value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition notlike(String key, String value);

	/**
	 * 前端匹配，"%"+value
	 * @param key
	 * @param value
	 * @return
	 */
	ICondition notlikeprefix(String key, String value);

	/**
	 * 后端匹配，value+"%"
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