package com.mawujun.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.mawujun.utils.page.Pager;

public interface IRepository<T,ID> {
	public T getById(ID id);
	public T getByMap(Map<String,Object> params);
	public List<T> getListByMap(Map<String,Object> params);
	public Pager<T> getPagerByMap(Map<String,Object> params);
	public long getCounts(Map<String,Object> params);
	
	public int create(T t);
	public int update(T t);
	public int updateByMap(Map<String,Object> params);
	public int remove(T t);
	public int removeById(ID id);
	public int removeByIds(ID... ids);
	public int removeByMap(Map<String,Object> params);
	
	
	
	

}
