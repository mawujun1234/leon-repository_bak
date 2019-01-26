package com.mawujun.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.mawujun.repository.mybatis.IRepository;
import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.Page;

@Transactional(rollbackOn= {Exception.class})
public class BaseService<M extends IRepository<T>, T> implements IBaseService<T> {

	 @Autowired
	 protected M repo;

	@Override
	public Class<Serializable> getIdType() {
		// TODO Auto-generated method stub
		return repo.getIdType();
	}

	@Override
	public List<String> getIdAttributeNames() {
		// TODO Auto-generated method stub
		return repo.getIdAttributeNames();
	}

	@Override
	public String getIdAttributeNames2Str() {
		// TODO Auto-generated method stub
		return repo.getIdAttributeNames2Str();
	}

//	@Override
//	public boolean clear() {
//		// TODO Auto-generated method stub
//		return repository.clear();
//	}

	@Override
	public T create(T t) {
		// TODO Auto-generated method stub
		return repo.create(t);
	}

	@Override
	public List<T> createBatch(List<T> list) {
		// TODO Auto-generated method stub
		return repo.createBatch(list);
	}

	@Override
	public List<T> createBatchByArray(T... list) {
		// TODO Auto-generated method stub
		return repo.createBatchByArray(list);
	}

	@Override
	public T save(T t) {
		// TODO Auto-generated method stub
		return repo.save(t);
	}

	@Override
	public List<T> saveBatch(List<T> list) {
		// TODO Auto-generated method stub
		return repo.saveBatch(list);
	}

	@Override
	public List<T> saveBatchByArray(T... list) {
		// TODO Auto-generated method stub
		return repo.saveBatchByArray(list);
	}

	@Override
	public T getById(Serializable id) {
		// TODO Auto-generated method stub
		return repo.getById(id);
	}

	@Override
	public T getByMap(Map<String, Object> params) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return repo.getByMap(params);
	}

	@Override
	public T getByExample(T params) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return repo.getByExample(params);
	}

	@Override
	public List<T> listByExample(T params) {
		// TODO Auto-generated method stub
		return repo.listByExample(params);
	}

	@Override
	public Page<T> listPageByExample(T params, int page, int limit) {
		// TODO Auto-generated method stub
		return repo.listPageByExample(params, page, limit);
	}

	@Override
	public List<T> listAll() {
		// TODO Auto-generated method stub
		return repo.listAll();
	}

	@Override
	public List<T> listByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.listByMap(params);
	}

	@Override
	public Page<T> listPageByPageInfo(Page<Object> pageinfo) {
		// TODO Auto-generated method stub
		return repo.listPageByPage(pageinfo);
	}

	@Override
	public Page<T> listPageByMap(Map<String, Object> params, int page, int limit) {
		// TODO Auto-generated method stub
		return repo.listPageByMap(params, page, limit);
	}
	@Override
	public Page<T> listPageByMap(Map<String,Object> params){
//		Page<T> page=new Page<T>();
//		page.init(params);
//		return page;
		return repo.listPageByPage(Page.of(params));
	}

	@Override
	public T update(T t) {
		// TODO Auto-generated method stub
		return repo.update(t);
	}

	@Override
	public List<T> updateBatch(List<T> list) {
		// TODO Auto-generated method stub
		return repo.updateBatch(list);
	}

	@Override
	public List<T> updateBatchByArray(T... list) {
		// TODO Auto-generated method stub
		return repo.updateBatchByArray(list);
	}

	@Override
	public int updateByMap(Map<String, Object> sets, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.updateByMap(sets, params);
	}

	@Override
	public int updateById(Map<String, Object> sets, Serializable id) {
		// TODO Auto-generated method stub
		return repo.updateById(sets, id);
	}

	@Override
	public int remove(T t) {
		// TODO Auto-generated method stub
		return repo.remove(t);
	}
	@Override
	public int removeForce(T t) {
		// TODO Auto-generated method stub
		return repo.removeForce(t);
	}

	@Override
	public int removeAll() {
		// TODO Auto-generated method stub
		return repo.removeAll();
	}

	@Override
	public int removeForceAll() {
		// TODO Auto-generated method stub
		return repo.removeForceAll();
	}

	
	@Override
	public int removeByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.removeByMap(params);
	}
	

	@Override
	public int removeForceByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.removeByMap(params);
	}

	@Override
	public int removeById(Serializable id) {
		// TODO Auto-generated method stub
		return repo.removeById(id);
	}
	
	@Override
	public int removeForceById(Serializable id) {
		// TODO Auto-generated method stub
		return repo.removeById(id);
	}

	@Override
	public int removeByIds(Serializable... ids) {
		// TODO Auto-generated method stub
		return repo.removeByIds(ids);
	}
	@Override
	public int removeByIds(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return repo.removeByIds(ids);
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return repo.count();
	}

	@Override
	public long countByExample(T params) {
		// TODO Auto-generated method stub
		return repo.countByExample(params);
	}

	@Override
	public long countByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.countByMap(params);
	}

	@Override
	public boolean existsById(Serializable id) {
		// TODO Auto-generated method stub
		return repo.existsById(id);
	}

	@Override
	public boolean existsByExample(T params) {
		// TODO Auto-generated method stub
		return repo.existsByExample(params);
	}

	@Override
	public boolean existsByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.existsByMap(params);
	}

	@Override
	public BeanMap getMapById(Serializable id, String... fields) {
		// TODO Auto-generated method stub
		return repo.getMapById(id, fields);
	}

	@Override
	public BeanMap getMapByMap(Map<String, Object> params, String... fields)
			throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return repo.getMapByMap(params, fields);
	}

	@Override
	public List<BeanMap> listMapByMap(Map<String, Object> params, String... fields) {
		// TODO Auto-generated method stub
		return repo.listMapByMap(params, fields);
	}


 
}
