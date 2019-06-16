package com.mawujun.repository.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.mawujun.util.StringUtil;

public class Cnd extends HashMap<String,Object>  implements ICondition,IUpdate{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4430685864786593882L;
	
	//private Map<String,Object> params=new HashMap<String,Object>();	
	
	
	//存放操作符
	private Map<String,OpEnum> op=new HashMap<String,OpEnum>();
	
	protected int page = -1;//当前第几页，第一页默认是1
	protected int limit = 50;// 默认是每页50条
	protected int start = 0;//第一行默认是0
	
	public final static String start_key="start";
	public final static String limit_key="limit";
	public final static String page_key="page";
	protected boolean isPageCondition=false;//是否是分页的条件
	
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
	public Cnd put(String key, Object value) {
		// TODO Auto-generated method stub
		super.put(key, value);
		op.put(key, OpEnum.eq);
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
	public static Cnd of() {
		Cnd utils=new Cnd();
		return utils;
		
	}
	
	/**
	 * 
	 * @param page 第几页,第一页是1
	 * @param limit 每页多少
	 * @return
	 */
	public static Cnd ofPageLimit(int page,int limit){
		Cnd param=new Cnd();
		if(page<1) {
			throw new IllegalArgumentException("分页的页码是从1开始");
		}
		
		param.setLimit(limit);
		param.setPage(page);
		param.isPageCondition=true;
		//param.setStart((page-1)*limit);
		
		return param;
	}
	/**
	 * 
	 * @param start 哪一行开始
	 * @param limit 每页多少
	 * @return
	 */
	public static Cnd ofStartLimit(int start,int limit){
		Cnd param=new Cnd();
		param.setStart(start);
		param.setLimit(limit);
		param.setPage((start/limit)+1);
		param.isPageCondition=true;
		return param;
	}
	

	/**
	 * 分页的参数名称未page和limit或start和limit,如果map里面有上面这三个参数，就会自动放到里面去
	 * @param params
	 * @return
	 */
	public static Cnd of(Map<String,Object> params) {
		Cnd cnd=null;
		int limit=0;
		if(params.containsKey(limit_key)) {
			limit=Integer.parseInt(params.get(limit_key).toString());
		}
		if(params.containsKey(page_key)) {
			int page=Integer.parseInt(params.get(page_key).toString());
			cnd= Cnd.ofPageLimit(page, limit);
			cnd.isPageCondition=true;
		} else	if(params.containsKey(start_key)) {
			int start=Integer.parseInt(params.get(start_key).toString());
			cnd= Cnd.ofStartLimit(start, limit);
			cnd.isPageCondition=true;
		}
		if(cnd==null){
			cnd=new Cnd();
		} else {
			//params.remove(start_key);
			//params.remove(page_key);
			//params.remove(limit_key);
		}
		
		for(Entry<String,Object> entry:params.entrySet()) {
			cnd.add(entry.getKey(), entry.getValue());
		}

		return cnd;
		
	}
	public static Cnd of(String key,Object value) {
		Cnd utils=new Cnd();
		utils.put(key, value);
		return utils;
		
	}
	
	public static String example_key="example"; 
	private boolean isExampleCondition=false;
	/**
	 * 设置参数，一般是作为where条件的，可以是map，bean等各种类型
	 * 并且如果设置了bean，再调用put其他参数，会把这个bean参数以map覆盖掉，
	 * 如果你想把bean和其他参数混合并列存在的话，那就只调用put方法，一个一个放进去。
	 * 
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param params
	 */
	public Cnd setParams(Object params) {
		if(params==null) {
			return this;
		}
		if(params instanceof Cnd) {
			this.putAll((Map)params);
		} else if(params instanceof Map) {
			this.putAll((Map)params);
		} else {
			isExampleCondition=true;
			//再使用的的时候，会提取这个example_key的值作为参数的
			this.put(example_key, params);
		}
		
		return this;
	}
	/**
	 * 返回参数，如果设置的时候是实体对象，那就返回实体对象，否则就返回Map
	 * @return
	 */
	public Object getParams(){
		if(isExampleCondition){
			return this.get(example_key);
		} else {
			return this;
		}
	}
	//=======================================
	private String countColumn;
	/**
	 * 使用哪一列进行统计总数，默认是null,一般可以设置为id
	 * 只在mybatis中有效
	 * @param countColumn
	 */
	public Cnd setCountColumn(String countColumn) {
		this.countColumn = countColumn;
		return this;
	}
	public String getCountColumn() {
		return countColumn;
	}
	
//	public Map<String,Object> getParams() {
//		return this;
//	}
	/**
	 *等于eq方法,如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	public Cnd add(String key,Object value) {
		if(!StringUtil.hasText(key) || !StringUtil.isNotEmpty(value)) {
			return this;
		}
		this.put(key, value);
		op.put(key, OpEnum.eq);
		return this;
	}
	/**
	 * 如果值为null，就不会添加这个条件
	 */
	public Cnd add(String key,OpEnum opEnum,Object value) {
		if(!StringUtil.hasText(key) || !StringUtil.isNotEmpty(value)) {
			return this;
		}
		this.put(key, value);
		op.put(key, opEnum);
		return this;
	}
	/**
	 * 如果值为null，就不会添加这个条件
	 */
	public Cnd eq(String key,Object value) {
		this.add(key,OpEnum.eq,value);
		return this;
	}
	/**
	 * 忽略大小写的比较,如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	public Cnd eq_i(String key,Object value) {
		this.add(key,OpEnum.eq_i,value);
		return this;
	}
	/**
	 * 如果值为null，就不会添加这个条件
	 */
	public Cnd noteq(String key,Object value) {
		this.add(key,OpEnum.noteq,value);
		return this;
	}
	/**
	 * 忽略大小写,如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	public Cnd noteq_i(String key,Object value) {
		this.add(key,OpEnum.noteq_i,value);
		return this;
	}
	/**
	 * 大于
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	public Cnd gt(String key,Object value) {
		this.add(key,OpEnum.gt,value);
		return this;
	}
	/**
	 * 大于等于
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	public Cnd ge(String key,Object value) {
		this.add(key,OpEnum.ge,value);
		return this;
	}
	/**
	 * 小于
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	public Cnd lt(String key,Object value) {
		this.add(key,OpEnum.lt,value);
		return this;
	}
	/**
	 * 小于等于
	 * 如果值为null，就不会添加这个条件
	 * @param key
	 * @param value
	 * @return
	 */
	public Cnd le(String key,Object value) {
		this.add(key,OpEnum.le,value);
		return this;
	}
	/**
	 * 如果值为null，就不会添加这个条件
	 */
	public Cnd between(String key,Object value1,Object value2) {
		this.add(key,OpEnum.between,new Object[] {value1,value2});
		return this;
	}
	/**
	 * 如果值为null，就不会添加这个条件
	 */
	public Cnd in(String key,Object... value) {
		this.add(key,OpEnum.in,value);
		return this;
	}
	/**
	 * 如果值为null，就不会添加这个条件
	 */
	public Cnd notin(String key,Object... value) {
		this.add(key,OpEnum.notin,value);
		return this;
	}
	public Cnd isnull(String key) {
		//this.add(key,OpEnum.isnull,null);
		this.put(key, null);
		op.put(key, OpEnum.isnull);
		return this;
	}
	@Override
	public ICondition isnull(String key, Object value) {
		if(value==null) {
			return this.isnull(key);
		} else {
			return this.add(key, value);
		}
	}

	@Override
	public ICondition isnotnull(String key, Object value) {
		if(value==null) {
			return this.isnotnull(key);
		} else {
			return this.noteq(key, value);
		}
	}
	public Cnd isnotnull(String key) {
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
	public Cnd like(String key,String value) {
		if(!StringUtil.hasText(key) || !StringUtil.hasText(value)) {
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
	public Cnd likeprefix(String key,String value) {
		if(!StringUtil.hasText(key) || !StringUtil.hasText(value)) {
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
	public Cnd likesuffix(String key,String value) {
		if(!StringUtil.hasText(key) || !StringUtil.hasText(value)) {
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
	public Cnd like_i(String key,String value) {
		if(!StringUtil.hasText(key) || !StringUtil.hasText(value)) {
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
	public Cnd likeprefix_i(String key,String value) {
		if(!StringUtil.hasText(key) || !StringUtil.hasText(value)) {
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
	public Cnd likesuffix_i(String key,String value) {
		if(!StringUtil.hasText(key) || !StringUtil.hasText(value)) {
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
	public Cnd notlike(String key,String value) {
		if(!StringUtil.hasText(key) || !StringUtil.hasText(value)) {
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
	public Cnd notlikeprefix(String key,String value) {
		if(!StringUtil.hasText(key) || !StringUtil.hasText(value)) {
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
	public Cnd notlikesuffix(String key,String value) {
		if(!StringUtil.hasText(key) || !StringUtil.hasText(value)) {
			return this;
		}
		this.add(key,OpEnum.notlikesuffix,value+"%");
		return this;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		this.isPageCondition=true;
	}

	public int getLimit() {
		return limit;
	}

	protected void setLimit(int limit) {
		this.limit = limit;
		this.isPageCondition=true;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
		this.isPageCondition=true;
	}

	/**
	 * 参数是Example
	 * @return
	 */
	public boolean isExampleCondition() {
		return isExampleCondition;
	}

	/**
	 * true：这个是要分页的条件
	 * @return
	 */
	public boolean isPageCondition() {
		return isPageCondition;
	}

	//===============================================================================
	private Map<String,Object> updatefields;
	@Override
	public Cnd update(String field, Object value) {
		// TODO Auto-generated method stub
		if(updatefields==null) {
			updatefields=new HashMap<String,Object>();
		}
		updatefields.put(field, value);
		return this;
	}

	@Override
	public Cnd update(Map<String, Object> sets) {
		// TODO Auto-generated method stub
		if(updatefields==null) {
			updatefields=new HashMap<String,Object>();
		}
		updatefields.putAll(sets);
		return this;
	}
	
	public Map<String,Object> getUpdatefields(){
		return updatefields;
	}



//========================================
	private Map<String,String> orderbyes=new LinkedHashMap<String,String>();
	/**
	 * 每调用一次就增加一个排序属性
	 * @param prop 排序的属性
	 * @param dir 排序的方向
	 */
	private Cnd orderBy(String prop,String dir) {
		if(!StringUtils.hasText(prop)) {
			return this;
		}
		if(dir==null) {
			dir="";
		}
		this.orderbyes.put(prop, dir);
		return this;
	}
	/**
	 * 每调用一次就增加一个排序规则
	 * @param prop
	 * @return
	 */
	public Cnd asc(String prop) {
		return orderBy(prop,"asc");
	}
	/**
	 * 每调用一次就增加一个排序规则
	 * @param prop
	 * @return
	 */
	public Cnd desc(String prop) {
		return orderBy(prop,"desc");
	}

	public Map<String, String> getOrderbyes() {
		return orderbyes;
	}
	
	//or构建or的条件====================================================
	private List<Cnd> ores;
	/**
	 * 添加or条件，会自动以()括号括起来。
	 * cnd.or(Cnd.of().eq(M.Star.id, 1).eq(M.Star.blood, "A"));
	 * 结果就是：where (star0_.id=1 or star0_.blood=?) and star0_.name=? 。
	 * 
	 * 但是如果or里面是同个属性的话，不支持，例如Cnd.of().eq("id",1).eq("id",2)这样暂时不支持。可以使用in替代。
	 * @param cnd
	 * @return
	 */
	public Cnd or(Cnd cnd) {
		if(cnd!=null && cnd.size()>=0) {
			if(ores==null) {
				ores=new ArrayList<Cnd>();
			}
			ores.add(cnd);
		}
		return this;
	}

	public List<Cnd> getOres() {
		return ores;
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
