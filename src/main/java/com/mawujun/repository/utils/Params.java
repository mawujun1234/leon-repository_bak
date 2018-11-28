package com.mawujun.repository.utils;

import java.util.HashMap;
import java.util.Map;

public class Params extends HashMap<String,Object>{
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
	
	
	//==============================================
	@Override
	public Params put(String key, Object value) {
		// TODO Auto-generated method stub
		super.put(key, value);
		return this;
	}
	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		// TODO Auto-generated method stub
		this.putAll(m);
	}
	
	//=======================================================
	/**
	 * 创建一个新的Params对象
	 * @return
	 */
	public static Params of() {
		Params utils=new Params();
		return utils;
		
	}
	public static Params of(String key,Object value) {
		Params utils=new Params();
		utils.put(key, value);
		return utils;
		
	}
	
	public Map<String,Object> getParams() {
		return this;
	}
	
	public Params add(String key,Object value) {
		this.put(key, value);
		return this;
	}
	/**
	 * 会把key相同的值组装成sql中in的形式,'a','b','c'
	 * 同个key不能和其他方法混合调用，但是同个key可以多次调用addin()方法.addIn(....).addIn(...)
	 * @param key
	 * @param value
	 * @return
	 */
	public Params addIn(String key,Object... values) {
		if(values!=null && values.length>0) {
			StringBuilder builder=new StringBuilder();
			boolean hasfirstcomma=true;
			if(this.containsKey(key)) {
				builder.append(this.get(key));
				hasfirstcomma=false;
			} 

			for(Object obj:values) {
				if(obj==null) {
					continue;
				}
				builder.append(",'");
				builder.append(obj.toString());
				builder.append("'");			
			}
			if(hasfirstcomma && builder.length()>1) {
				this.put(key, builder.substring(1));
			}
		}
		return this;
	}
	/**
	 * 会再value的两边都加上%号
	 * @param key
	 * @param value
	 * @return
	 */
	public Params addLike(String key,Object value) {
		if(value!=null) {
			this.add(key, "%"+value.toString()+"%");
		}
		return this;
	}

}
