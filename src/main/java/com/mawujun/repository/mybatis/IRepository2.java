package com.mawujun.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.Page;

/**
 * 如果要使用mybatis进行分页查询，那必须参数是PageInfo<T>,返回值也是PageInfo<T>(返回值其实不是pageinfo也可以)，因为参数时引用传递。
 * 用于mybatis的，和IRepository的区别是很多方法都返回的int的影响函数的值，但是IRepository不能返回影响函数的值
 * @author admin
 *
 * @param <T>
 * @param <ID>
 */
public interface IRepository2<T,ID> {
	
	public int create(T t);
	
	
	public T getById(ID id);
	/**
	 * 如果有多条记录就返回第一条,如果有多个，将爆出异常
	 * @param params
	 * @return
	 */
	public T getByMap(Map<String,Object> params);
	/**
	 * 如果有多条记录就返回第一条,如果有多个，将爆出异常
	 * Example还可以扩展，例如某个属性的名字不是=，而是使用like
	 * @param params
	 * @return
	 */
	public T getByExample(T params);
	/**
	 * 是否存在相同的对象
	 * Example还可以扩展，例如某个属性的名字不是=，而是使用like
	 * @param params
	 * @return
	 */
	public boolean existsByExample(T params);
	/**
	 * 类似的对象有几个
	 * Example还可以扩展，例如某个属性的名字不是=，而是使用like
	 * @param params
	 * @return
	 */
	public long countByExample(T params);
	
	public List<T> listByExample(T params);
	
	//public PageInfo<T> listPageByExample(T params, PageInfo<T> pageinfo);
	/**
	 * 
	 * @param params
	 * @param page 第几页，第一页是0；
	 * @param limit 一页放几行
	 * @return
	 */
	public Page<T> listPageByExample(T params, int page,int limit);
	
	
	public List<T> listAll();
	
	/**
	 * 参数不能为null,如果为null，将会返回所有数据
	 * @param params
	 * @return
	 */
	public List<T> listByMap(Map<String,Object> params);
//	/**
//	 * 要分页就全部使用这种模式，参数是PageInfo，封装了sql要用的参数和分页信息，
//	 * 返回值也是PageInfo
//	 * PageInfo中的参数可以是Map也可以是T，但不能是其他类型，否则会报异常
//	 * @param params
//	 * @return
//	 */
//	public PageInfo<T> listPageByMap(PageInfo<T> pageinfo);
	public Page<T> listPageByMap(Map<String,Object> params,int page,int limit);


	/**
	 * 更新id为t.id的对象，所有属性都会更新，如果其他值没有设置，将会被更新为null
	 * 不是动态更新
	 * @param t
	 * @return
	 */
	public int update(T t);
	/**
	 * 更新id为params.id的对象,如果没有id参数，江会报错
	 * 是动态更新
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
	public int countByMap(Map<String,Object> params);
//	/**
//	 * 参数为null，就返回所有的数据
//	 * @param t
//	 * @return
//	 */
//	public int getCountsByEntity(T t);
	
	
	public Page<T> getPagerByMap(Page pagerInfo);
	
	
	
	/**
	 * 批量插入，如果打数据量，请用mybatis，进行性能调优
	 * @param list
	 * @return
	 */
	public int createBatch(List<T> list);
	/***
	 * 批量插入，如果打数据量，请用mybatis，进行性能调优
	 * @param list
	 * @return
	 */
	public int createBatch(T... list);
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


}
