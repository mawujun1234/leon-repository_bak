package com.mawujun.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.PagerInfo;

public interface IRepository<T,ID> {
	public T getById(ID id);
	/**
	 * 如果有多条记录就返回第一条
	 * @param params
	 * @return
	 */
	public T getByMap(Map<String,Object> params);
	/**
	 * 如果有多条记录就返回第一条
	 * @param params
	 * @return
	 */
	public T getByEntity(T params);
	
	public BeanMap getMapById(ID id);
	/**
	 * 如果有多条记录就返回第一条
	 * @param params
	 * @return
	 */
	public BeanMap getMapByMap(Map<String,Object> params);
	/**
	 * 如果有多条记录就返回第一条
	 * @param params
	 * @return
	 */
	public BeanMap getMapByEntity(T params);
	
	/**
	 * 如果传入null，就返回所有的数据
	 * @param params
	 * @return
	 */
	public List<T> listByMap(Map<String,Object> params);
	/**
	 * 如果传入null，就返回所有的数据
	 * @param params
	 * @return
	 */
	public List<T> listByEntity(T params);
	

	
	public int create(T t);
	
	public int update(T t);
	public int updateByMap(Map<String,Object> params);
	
	/**
	 * 不要传入一个没有任何值的实体对象，这样会清空整个表的数据
	 * @param t
	 * @return
	 */
	public int remove(T t);
	public int removeByMap(Map<String,Object> params);
	
	public int removeById(ID id);
	public int removeByIds(ID... ids);
	
	public int createBatch(List<T> list);
	
	/**
	 * 参数为null，就统计所有的记录
	 * @param params
	 * @return
	 */
	public int getCounts(Map<String,Object> params);
	/**
	 * 参数为null，就返回所有的数据
	 * @param t
	 * @return
	 */
	public int getCountsByEntity(T t);
	
	
	public PagerInfo<T> getPagerByMap(PagerInfo pagerInfo);
	
	
	
	


}
