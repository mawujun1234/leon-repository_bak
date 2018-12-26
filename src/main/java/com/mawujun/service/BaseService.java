package com.mawujun.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.mawujun.repository.mybatis.IRepository;
import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.Page;

public class BaseService<M extends IRepository<T>, T> implements IBaseService<T> {

	 @Autowired
	 protected M repository;

	@Override
	public Class<Serializable> getIdType() {
		// TODO Auto-generated method stub
		return repository.getIdType();
	}

	@Override
	public List<String> getIdAttributeNames() {
		// TODO Auto-generated method stub
		return repository.getIdAttributeNames();
	}

	@Override
	public String getIdAttributeNames2Str() {
		// TODO Auto-generated method stub
		return repository.getIdAttributeNames2Str();
	}

//	@Override
//	public boolean clear() {
//		// TODO Auto-generated method stub
//		return repository.clear();
//	}

	@Override
	public T create(T t) {
		// TODO Auto-generated method stub
		return repository.create(t);
	}

	@Override
	public List<T> createBatch(List<T> list) {
		// TODO Auto-generated method stub
		return repository.createBatch(list);
	}

	@Override
	public List<T> createBatchByArray(T... list) {
		// TODO Auto-generated method stub
		return repository.createBatchByArray(list);
	}

	@Override
	public T save(T t) {
		// TODO Auto-generated method stub
		return repository.save(t);
	}

	@Override
	public List<T> saveBatch(List<T> list) {
		// TODO Auto-generated method stub
		return repository.saveBatch(list);
	}

	@Override
	public List<T> saveBatchByArray(T... list) {
		// TODO Auto-generated method stub
		return repository.saveBatchByArray(list);
	}

	@Override
	public T getById(Serializable id) {
		// TODO Auto-generated method stub
		return repository.getById(id);
	}

	@Override
	public T getByMap(Map<String, Object> params) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return repository.getByMap(params);
	}

	@Override
	public T getByExample(T params) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return repository.getByExample(params);
	}

	@Override
	public List<T> listByExample(T params) {
		// TODO Auto-generated method stub
		return repository.listByExample(params);
	}

	@Override
	public Page<T> listPageByExample(T params, int page, int limit) {
		// TODO Auto-generated method stub
		return repository.listPageByExample(params, page, limit);
	}

	@Override
	public List<T> listAll() {
		// TODO Auto-generated method stub
		return repository.listAll();
	}

	@Override
	public List<T> listByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repository.listByMap(params);
	}

	@Override
	public Page<T> listPageByPageInfo(Page<T> pageinfo) {
		// TODO Auto-generated method stub
		return repository.listPageByPageInfo(pageinfo);
	}

	@Override
	public Page<T> listPageByMap(Map<String, Object> params, int page, int limit) {
		// TODO Auto-generated method stub
		return repository.listPageByMap(params, page, limit);
	}

	@Override
	public T update(T t) {
		// TODO Auto-generated method stub
		return repository.update(t);
	}

	@Override
	public List<T> updateBatch(List<T> list) {
		// TODO Auto-generated method stub
		return repository.updateBatch(list);
	}

	@Override
	public List<T> updateBatchByArray(T... list) {
		// TODO Auto-generated method stub
		return repository.updateBatchByArray(list);
	}

	@Override
	public int updateByMap(Map<String, Object> sets, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repository.updateByMap(sets, params);
	}

	@Override
	public int updateById(Map<String, Object> sets, Serializable id) {
		// TODO Auto-generated method stub
		return repository.updateById(sets, id);
	}

	@Override
	public int remove(T t) {
		// TODO Auto-generated method stub
		return repository.remove(t);
	}
	@Override
	public int removeForce(T t) {
		// TODO Auto-generated method stub
		return repository.removeForce(t);
	}

	@Override
	public int removeAll() {
		// TODO Auto-generated method stub
		return repository.removeAll();
	}

	@Override
	public int removeForceAll() {
		// TODO Auto-generated method stub
		return repository.removeForceAll();
	}

	
	@Override
	public int removeByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repository.removeByMap(params);
	}
	

	@Override
	public int removeForceByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repository.removeByMap(params);
	}

	@Override
	public int removeById(Serializable id) {
		// TODO Auto-generated method stub
		return repository.removeById(id);
	}
	
	@Override
	public int removeForceById(Serializable id) {
		// TODO Auto-generated method stub
		return repository.removeById(id);
	}

	@Override
	public int removeByIds(Serializable... ids) {
		// TODO Auto-generated method stub
		return repository.removeByIds(ids);
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return repository.count();
	}

	@Override
	public long countByExample(T params) {
		// TODO Auto-generated method stub
		return repository.countByExample(params);
	}

	@Override
	public long countByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repository.countByMap(params);
	}

	@Override
	public boolean existsById(Serializable id) {
		// TODO Auto-generated method stub
		return repository.existsById(id);
	}

	@Override
	public boolean existsByExample(T params) {
		// TODO Auto-generated method stub
		return repository.existsByExample(params);
	}

	@Override
	public boolean existsByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repository.existsByMap(params);
	}

	@Override
	public BeanMap getMapById(Serializable id, String... fields) {
		// TODO Auto-generated method stub
		return repository.getMapById(id, fields);
	}

	@Override
	public BeanMap getMapByMap(Map<String, Object> params, String... fields)
			throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return repository.getMapByMap(params, fields);
	}

	@Override
	public List<BeanMap> listMapByMap(Map<String, Object> params, String... fields) {
		// TODO Auto-generated method stub
		return repository.listMapByMap(params, fields);
	}
 
}
