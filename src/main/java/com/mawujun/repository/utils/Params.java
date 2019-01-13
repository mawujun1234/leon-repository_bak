package com.mawujun.repository.utils;

import java.util.HashMap;
import java.util.Map;

public class Params extends HashMap<String,Object>  implements IParams{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4430685864786593882L;
	
	//private Map<String,Object> params=new HashMap<String,Object>();	
	
//	@Override
//	public int size() {
//		// TODO Auto-generated method stub
//		return super.size();
//	}
//
//	@Override
//	public boolean isEmpty() {
//		// TODO Auto-generated method stub
//		return super.isEmpty();
//	}
//
//	@Override
//	public boolean containsKey(Object key) {
//		// TODO Auto-generated method stub
//		return super.containsKey(key);
//	}
//
//	@Override
//	public boolean containsValue(Object value) {
//		// TODO Auto-generated method stub
//		return super.containsValue(value);
//	}
//
//	@Override
//	public Object get(Object key) {
//		// TODO Auto-generated method stub
//		return super.get(key);
//	}
//
//	
//
//	@Override
//	public Object remove(Object key) {
//		// TODO Auto-generated method stub
//		return super.remove(key);
//	}
//	
//	
//
//	@Override
//	public void clear() {
//		// TODO Auto-generated method stub
//		super.clear();
//		
//	}
//
//	@Override
//	public Set<String> keySet() {
//		// TODO Auto-generated method stub
//		return super.keySet();
//	}
//
//	@Override
//	public Collection<Object> values() {
//		// TODO Auto-generated method stub
//		return super.values();
//	}
//
//	@Override
//	public Set<Entry<String, Object>> entrySet() {
//		// TODO Auto-generated method stub
//		return super.entrySet();
//	}
	
	//存放操作符
	private Map<String,OpEnum> op=new HashMap<String,OpEnum>();
	
	/**
	 * 如果不存在，默认是eq
	 * @param key
	 * @return
	 */
	public OpEnum getOpEnum(String key) {
		OpEnum opEnum=op.get(key);
		return opEnum==null?OpEnum.eq:opEnum;
	}
	
	//==============================================
	/**
	 * 默认的操作符是“=”
	 */
	@Override
	public Params put(String key, Object value) {
		// TODO Auto-generated method stub
		super.put(key, value);
		op.put(key, OpEnum.eq);
		return this;
	}
//	@Override
//	public void putAll(Map<? extends String, ? extends Object> m) {
//		// TODO Auto-generated method stub
//		this.putAll(m);
//	}
	
	//=======================================================
	/**
	 * 创建一个新的Params对象
	 * @return
	 */
	public static Params of() {
		Params utils=new Params();
		return utils;
		
	}
	public static Params of(Map<String,Object> params) {
		Params utils=new Params();
		utils.putAll(params);
		return utils;
		
	}
	public static Params of(String key,Object value) {
		Params utils=new Params();
		utils.put(key, value);
		return utils;
		
	}
	
//	public Map<String,Object> getParams() {
//		return this;
//	}
	/**
	 *等于eq方法
	 * @param key
	 * @param value
	 * @return
	 */
	public Params add(String key,Object value) {
		this.put(key, value);
		op.put(key, OpEnum.eq);
		return this;
	}
	public Params add(String key,OpEnum opEnum,Object value) {
		this.put(key, value);
		op.put(key, opEnum);
		return this;
	}
	
	public Params eq(String key,Object value) {
		this.add(key,OpEnum.eq,value);
		return this;
	}
	/**
	 * 忽略大小写的比较
	 * @param key
	 * @param value
	 * @return
	 */
	public Params eq_i(String key,Object value) {
		this.add(key,OpEnum.eq_i,value);
		return this;
	}
	public Params noteq(String key,Object value) {
		this.add(key,OpEnum.noteq,value);
		return this;
	}
	/**
	 * 忽略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	public Params noteq_i(String key,Object value) {
		this.add(key,OpEnum.noteq_i,value);
		return this;
	}
	/**
	 * 大于
	 * @param key
	 * @param value
	 * @return
	 */
	public Params gt(String key,Object value) {
		this.add(key,OpEnum.gt,value);
		return this;
	}
	/**
	 * 大于等于
	 * @param key
	 * @param value
	 * @return
	 */
	public Params ge(String key,Object value) {
		this.add(key,OpEnum.ge,value);
		return this;
	}
	/**
	 * 小于
	 * @param key
	 * @param value
	 * @return
	 */
	public Params lt(String key,Object value) {
		this.add(key,OpEnum.lt,value);
		return this;
	}
	/**
	 * 小于等于
	 * @param key
	 * @param value
	 * @return
	 */
	public Params le(String key,Object value) {
		this.add(key,OpEnum.le,value);
		return this;
	}
	public Params between(String key,Object value1,Object value2) {
		this.add(key,OpEnum.between,new Object[] {value1,value2});
		return this;
	}
	public Params in(String key,Object... value) {
		this.add(key,OpEnum.in,value);
		return this;
	}
	public Params notin(String key,Object... value) {
		this.add(key,OpEnum.notin,value);
		return this;
	}
	public Params isnull(String key) {
		this.add(key,OpEnum.isnull,null);
		return this;
	}
	public Params isnotnull(String key) {
		this.add(key,OpEnum.isnotnull,null);
		return this;
	}
	/**
	 * 两端like,"%"+value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	public Params like(String key,String value) {
		this.add(key,OpEnum.like,"%"+value+"%");
		return this;
	}
	/**
	 * 前端匹配，"%"+value
	 * @param key
	 * @param value
	 * @return
	 */
	public Params likeprefix(String key,String value) {
		this.add(key,OpEnum.likeprefix,"%"+value);
		return this;
	}
	
	/**
	 * 后端匹配，value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	public Params likesuffix(String key,String value) {
		this.add(key,OpEnum.likesuffix,value+"%");
		return this;
	}
	
	/**
	 * 两端like,"%"+value+"%",或略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	public Params like_i(String key,String value) {
		this.add(key,OpEnum.like_i,"%"+value+"%");
		return this;
	}
	/**
	 * 前端匹配，"%"+value,或略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	public Params likeprefix_i(String key,String value) {
		this.add(key,OpEnum.likeprefix_i,"%"+value);
		return this;
	}
	
	/**
	 * 后端匹配，value+"%",或略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	public Params likesuffix_i(String key,String value) {
		this.add(key,OpEnum.likesuffix_i,value+"%");
		return this;
	}
	
	 /** 两端like,"%"+value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	public Params notlike(String key,String value) {
		this.add(key,OpEnum.notlike,"%"+value+"%");
		return this;
	}
	/**
	 * 前端匹配，"%"+value
	 * @param key
	 * @param value
	 * @return
	 */
	public Params notlikeprefix(String key,String value) {
		this.add(key,OpEnum.notlikeprefix,"%"+value);
		return this;
	}
	
	/**
	 * 后端匹配，value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	public Params notlikesuffix(String key,String value) {
		this.add(key,OpEnum.notlikesuffix,value+"%");
		return this;
	}
	
//	/**
//	 * 会把key相同的值组装成sql中in的形式,'a','b','c'
//	 * 同个key不能和其他方法混合调用，但是同个key可以多次调用addin()方法.addIn(....).addIn(...)
//	 * @param key
//	 * @param value
//	 * @return
//	 */
//	public Params addIn(String key,Object... values) {
//		if(values!=null && values.length>0) {
//			StringBuilder builder=new StringBuilder();
//			boolean hasfirstcomma=true;
//			if(this.containsKey(key)) {
//				builder.append(this.get(key));
//				hasfirstcomma=false;
//			} 
//
//			for(Object obj:values) {
//				if(obj==null) {
//					continue;
//				}
//				builder.append(",'");
//				builder.append(obj.toString());
//				builder.append("'");			
//			}
//			if(hasfirstcomma && builder.length()>1) {
//				this.put(key, builder.substring(1));
//			}
//		}
//		return this;
//	}
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
