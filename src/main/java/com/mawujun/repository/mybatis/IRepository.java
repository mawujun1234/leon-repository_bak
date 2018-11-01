package com.mawujun.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.PageInfo;

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
	/**
	 * 要分页就全部使用这种模式，参数是PageInfo，封装了sql要用的参数和分页信息，
	 * 返回值也是PageInfo
	 * @param params
	 * @return
	 */
	public PageInfo<T> listPageByMap(PageInfo<T> params);
	

	
	public int create(T t);
//	public int createBatch(List<T> list);
//	public int createBatch(T... list);
	/**
	 * 更新id为t.id的对象
	 * @param t
	 * @return
	 */
	public int update(T t);
	/**
	 * 更新id为list中的t.id的对象
	 * @param t
	 * @return
	 */
	public int updateBatch(List<T> list);
	/**
	 * 更新id为array中的t.id的对象
	 * @param t
	 * @return
	 */
	public int updateBatch(T... list);
	/**
	 * 更新id为params.id的对象
	 * @param t
	 * @return
	 */
	public int updateByMap(Map<String,Object> params);
	
	/**
	 * 和T一样的属性值的对象都会被删除
	 * 不要传入一个没有任何值的实体对象，这样会清空整个表的数据
	 * @param t
	 * @return
	 */
	public int remove(T t);
	public int removeByMap(Map<String,Object> params);
	
	public int removeById(ID id);
	public int removeByIds(ID... ids);
	
	
	
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
	
	
	public PageInfo<T> getPagerByMap(PageInfo pagerInfo);
	
	
	
	


}
