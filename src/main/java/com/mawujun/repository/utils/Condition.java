package com.mawujun.repository.utils;

import java.util.HashMap;
import java.util.Map;

import com.mawujun.utils.string.StringUtils;

public class Condition extends HashMap<String,Object>  implements ICondition{
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
	public Condition put(String key, Object value) {
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
	public static Condition of() {
		Condition utils=new Condition();
		return utils;
		
	}
	public static Condition of(Map<String,Object> params) {
		Condition utils=new Condition();
		
		for(Entry<String,Object> entry:params.entrySet()) {
			utils.add(entry.getKey(), entry.getValue());
		}

		return utils;
		
	}
	public static Condition of(String key,Object value) {
		Condition utils=new Condition();
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
	public Condition add(String key,Object value) {
		if(!StringUtils.hasText(key) || !StringUtils.isNotEmpty(value)) {
			return this;
		}
		this.put(key, value);
		op.put(key, OpEnum.eq);
		return this;
	}
	public Condition add(String key,OpEnum opEnum,Object value) {
		if(!StringUtils.hasText(key) || !StringUtils.isNotEmpty(value)) {
			return this;
		}
		this.put(key, value);
		op.put(key, opEnum);
		return this;
	}
	
	public Condition eq(String key,Object value) {
		this.add(key,OpEnum.eq,value);
		return this;
	}
	/**
	 * 忽略大小写的比较
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition eq_i(String key,Object value) {
		this.add(key,OpEnum.eq_i,value);
		return this;
	}
	public Condition noteq(String key,Object value) {
		this.add(key,OpEnum.noteq,value);
		return this;
	}
	/**
	 * 忽略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition noteq_i(String key,Object value) {
		this.add(key,OpEnum.noteq_i,value);
		return this;
	}
	/**
	 * 大于
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition gt(String key,Object value) {
		this.add(key,OpEnum.gt,value);
		return this;
	}
	/**
	 * 大于等于
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition ge(String key,Object value) {
		this.add(key,OpEnum.ge,value);
		return this;
	}
	/**
	 * 小于
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition lt(String key,Object value) {
		this.add(key,OpEnum.lt,value);
		return this;
	}
	/**
	 * 小于等于
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition le(String key,Object value) {
		this.add(key,OpEnum.le,value);
		return this;
	}
	public Condition between(String key,Object value1,Object value2) {
		this.add(key,OpEnum.between,new Object[] {value1,value2});
		return this;
	}
	public Condition in(String key,Object... value) {
		this.add(key,OpEnum.in,value);
		return this;
	}
	public Condition notin(String key,Object... value) {
		this.add(key,OpEnum.notin,value);
		return this;
	}
	public Condition isnull(String key) {
		//this.add(key,OpEnum.isnull,null);
		this.put(key, null);
		op.put(key, OpEnum.isnull);
		return this;
	}
	public Condition isnotnull(String key) {
		//this.add(key,OpEnum.isnotnull,null);
		this.put(key, null);
		op.put(key, OpEnum.isnotnull);
		return this;
	}
	/**
	 * 两端like,"%"+value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition like(String key,String value) {
		if(!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
			return this;
		}
		this.add(key,OpEnum.like,"%"+value+"%");
		return this;
	}
	/**
	 * 前端匹配，"%"+value
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition likeprefix(String key,String value) {
		if(!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
			return this;
		}
		this.add(key,OpEnum.likeprefix,"%"+value);
		return this;
	}
	
	/**
	 * 后端匹配，value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition likesuffix(String key,String value) {
		if(!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
			return this;
		}
		this.add(key,OpEnum.likesuffix,value+"%");
		return this;
	}
	
	/**
	 * 两端like,"%"+value+"%",或略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition like_i(String key,String value) {
		if(!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
			return this;
		}
		this.add(key,OpEnum.like_i,"%"+value+"%");
		return this;
	}
	/**
	 * 前端匹配，"%"+value,或略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition likeprefix_i(String key,String value) {
		if(!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
			return this;
		}
		this.add(key,OpEnum.likeprefix_i,"%"+value);
		return this;
	}
	
	/**
	 * 后端匹配，value+"%",或略大小写
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition likesuffix_i(String key,String value) {
		if(!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
			return this;
		}
		this.add(key,OpEnum.likesuffix_i,value+"%");
		return this;
	}
	
	 /** 两端like,"%"+value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition notlike(String key,String value) {
		if(!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
			return this;
		}
		this.add(key,OpEnum.notlike,"%"+value+"%");
		return this;
	}
	/**
	 * 前端匹配，"%"+value
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition notlikeprefix(String key,String value) {
		if(!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
			return this;
		}
		this.add(key,OpEnum.notlikeprefix,"%"+value);
		return this;
	}
	
	/**
	 * 后端匹配，value+"%"
	 * @param key
	 * @param value
	 * @return
	 */
	public Condition notlikesuffix(String key,String value) {
		if(!StringUtils.hasText(key) || !StringUtils.hasText(value)) {
			return this;
		}
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
