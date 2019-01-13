package com.mawujun.mvc;

import java.util.HashMap;
import java.util.Map;

import com.mawujun.repository.utils.Page;

/**
 * spring mvc 的返回数据格式
 * 使用方法:R.ok().putData(result);
 * @author mawujun 16064988
 *
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 0);
		put("success",true);
		put("msg", "success");
	}
	
	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(500, msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code); 
		r.put("msg", msg);
		r.put("success", false);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}
	/**
	 * 直接放进去data数据
	 * @param data
	 * @return
	 */
	public static R ok(Object data) {
		R r= new R();
		r.setData(data);
		return r;
	}

	public R put(String key, Object value) {
		
		if(value instanceof Page) {
			putPage(key,(Page)value);
		} else {
			super.put(key, value);
		}
		return this;
	}
	private void putPage(String key, Page page) {
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("page", page.getPage());
		result.put("limit", page.getLimit());
		result.put("total", page.getTotal());
		result.put("start", page.getStart());
		result.put("totalPages", page.getTotalPages());
		result.put("root", page.getRoot());
		result.put("rootSize", page.getRootSize());
		
		super.put(key, result);
		
	}
	
	public R data(Object value) {
		super.put("data", value);
		return this;
	}
	public R putData(Object value) {
		return this.data(value);
	}
	public R setData(Object value) {
		return this.data(value);
	}
	
	public R putCode(Object value) {
		super.put("code", value);
		return this;
	}
	public R setCode(Object value) {
		return this.putCode(value);
	}
	
	public R putMsg(Object value) {
		super.put("msg", value);
		return this;
	}
	public R setMsg(Object value) {
		return this.putMsg(value);
	}
	
	
}
