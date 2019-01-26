package com.mawujun.repository.mybatis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.NonUniqueResultException;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.Page;


/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 * 如果要使用mybatis进行分页查询，那必须参数是PageInfo<T>,返回值也是PageInfo<T>(返回值其实不是pageinfo也可以)，因为参数时引用传递。
 * @author admin
 *
 * @param <T>
 */
public interface IRepository<T> {
	/**
	 * 获取id的类型
	 * @return
	 */
	public Class<Serializable> getIdType();
	/**
	 * 获取id的属性名称
	 * @return
	 */
	public List<String> getIdAttributeNames();
	/**
	 * 获取id的属性字符串,如果 有多个属性就以逗号分隔
	 * @return
	 */
	public String getIdAttributeNames2Str();
	/**
	 * 清空持久化上下文中的内容
	 */
	public boolean clear();
	
	public T create(T t);
	/**
	 * 批量插入，如果打数据量，请用mybatis，进行性能调优 或者分批次插入
	 * @param list
	 * @return
	 */
	public List<T> createBatch(List<T> list);
	/***
	 * 批量插入，如果打数据量，请用mybatis，进行性能调优 或者分批次插入
	 * @param list
	 * @return
	 */
	public List<T> createBatchByArray(T... list);
	
	/**
	 * 如果存在就更新，如果不存在就插入
	 * @param t
	 * @return
	 */
	public T save(T t);
	/**
	 * 如果存在就更新，如果不存在就插入，如果打数据量，请用mybatis，进行性能调优 或者分批次插入
	 * @param list
	 * @return
	 */
	public List<T> saveBatch(List<T> list);
	/***
	 * 如果存在就更新，如果不存在就插入，如果打数据量，请用mybatis，进行性能调优 或者分批次插入
	 * @param list
	 * @return
	 */
	public List<T> saveBatchByArray(T... list);
	
	
	public T getById(Serializable id);
	/**
	 * 如果有多个，将爆出异常
	 * @param params
	 * @return
	 * @throws NonUniqueResultException
	 */
	public T getByMap(Map<String,Object> params)  throws IncorrectResultSizeDataAccessException;
	/**
	 * 如果有多条记录就返回第一条,如果有多个，将爆出异常
	 * Example还可以扩展，例如某个属性的名字不是=，而是使用like
	 * @param params
	 * @return
	 * @throws IncorrectResultSizeDataAccessException
	 */
	public T getByExample(T params)  throws IncorrectResultSizeDataAccessException;
	
	
	
	public List<T> listByExample(T params);
	
	//public PageInfo<T> listPageByExample(T params, PageInfo<T> pageinfo);
	/**
	 * 满足不了时，请参考listPageByPageInfo
	 * @param params
	 * @param page 第几页，第一页是1；
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
	/**
	 * 参数是PageInfo，封装了sql要用的参数和分页信息。
	 * PageInfo中的参数可以是Map也可以是T，但不能是其他类型，否则会报异常
	 * 还可以自定义复杂查询的分页，需要自己增加一个方法，参数是PageInfo<M> ，返回值也是PageInfo,并在Mapper.xml文件中，写对应的方法.泛型可以是T也可以是Map
	 * @param params
	 * @return
	 */
	public Page<T> listPageByPage(Page<? extends Object> page);
	
	/**
	 * 取分页数据，满足不了时，请参考listPageByPageInfo
	 * @param params 如果为null，按所有数据进行分页
	 * @param page 第几页  从1 开始
	 * @param limit 每页的页数
	 * @return
	 */
	public Page<T> listPageByMap(Map<String,Object> params,int page,int limit);


	/**
	 * 更新id为t.id的对象，所有属性都会更新，如果其他值没有设置，将会被更新为null
	 * 不是动态更新
	 * @param t
	 * @return 返回来的对象和参数对象，可能会不是同个对象
	 */
	public T update(T t);
	/**
	 * 更新id为list中的t.id的对象
	 * @param t
	 * @return
	 */
	public List<T> updateBatch(List<T> list);
	/**
	 * 更新id为array中的t.id的对象
	 * @param t
	 * @return
	 */
	public List<T> updateBatchByArray(T... list);
	/**
	 * sets就是要更新的值
	 * params是条件
	 * 是动态更新
	 * @param t
	 * @return
	 */
	public int updateByMap(Map<String,Object> sets,Map<String,Object> params);
	/**
	 * 根据id更新内容
	 * @param sets
	 * @param id
	 * @return
	 */
	public int updateById(Map<String,Object> sets,Serializable id);
	

	/**
	 * 如果是逻辑删除，就把注解了@LogicDelecte字段设置为1
	 * 
	 * @param t
	 * @return
	 */
	public int remove(T t);
	/**
	 * 强制删除，即使注解了@LogicDelect字段，也会被强制删除
	 * @param t
	 * @return
	 */
	public int removeForce(T t);
	/**
	 * 全部删除，如果是逻辑删除，就把注解了@LogicDelecte字段设置为1
	 * @param t
	 * @return
	 */
	public int removeAll();
	/**
	 * 强制删除所有，即使设置了逻辑删除，也会被强制删除
	 * @param t
	 * @return
	 */
	public int removeForceAll();
	/**
	 * 根据指定的条件删除对象，如果是逻辑删除，就把注解了@LogicDelecte字段设置为1
	 * @param params
	 * @return
	 */
	public int removeByMap(Map<String,Object> params);
	/**
	 * 根据指定的条件强制删除对象
	 * @param params
	 * @return
	 */
	public int removeForceByMap(Map<String,Object> params);
	/**
	 * 根据id删除对象，如果是逻辑删除，就把注解了@LogicDelecte字段设置为1
	 * @param id
	 * @return
	 */
	public int removeById(Serializable id);
	/**
	 * 根据id强制删除
	 * @param id
	 * @return
	 */
	public int removeForceById(Serializable id);
	/**
	 * 根据id数组删除对象，如果是逻辑删除，就把注解了@LogicDelecte字段设置为1
	 * @param ids
	 * @return
	 */
	public int removeByIds(Serializable... ids);
	/**
	 * 根据id数组删除对象，如果是逻辑删除，就把注解了@LogicDelecte字段设置为1
	 * @param ids
	 * @return
	 */
	public int removeByIds(List<Serializable> ids);
	
	/**
	 * 统计所有
	 * @return
	 */
	public long count();
	/**
	 * 类似的对象有几个，如果参数为null，将会返回所有的数据
	 * Example还可以扩展，例如某个属性的名字不是=，而是使用like
	 * @param params
	 * @return
	 */
	public long countByExample(T params);
	/**
	 * 参数为null，就统计所有的记录
	 * @param params
	 * @return
	 */
	public long countByMap(Map<String,Object> params);

	
	/**
	 * 判断是否存在某个id的数据
	 * @param id
	 * @return
	 */
	public boolean existsById(Serializable id);
	/**
	 * 是否存在相同的对象
	 * Example还可以扩展，例如某个属性的名字不是=，而是使用like
	 * @param params
	 * @return
	 */
	public boolean existsByExample(T params);
	/**
	 * 是否存在相同的对象
	 * 
	 * @param params
	 * @return
	 */
	public boolean existsByMap(Map<String,Object> params);
	
	
	
	/**
	 * 
	 * @param id
	 * @param fields 要查询的属性名
	 * @return
	 */
	public BeanMap getMapById(Serializable id,String... fields);
	/**
	 * 如果有多条，就抛出异常
	 * @param params
	 * @param fields 要查询的属性名称
	 * @return
	 * @throws IncorrectResultSizeDataAccessException
	 */
	public BeanMap getMapByMap(Map<String,Object> params,String... fields) throws IncorrectResultSizeDataAccessException;
	/**
	 * 
	 * @param params
	 * @param fields 要查询的属性名称
	 * @return
	 */
	public List<BeanMap> listMapByMap(Map<String,Object> params,String... fields);
//	/**
//	 * 
//	 * @param field 统计某个属性
//	 * @param params 条件
//	 * @return
//	 */
//	public Long sumAsLong(String field,Map<String,Object> params);
//	
//	/**
//	 * 
//	 * @param field 统计某个属性
//	 * @param params 条件
//	 * @return
//	 */
//	public Double sumAsDouble(String field,Map<String,Object> params);


}
