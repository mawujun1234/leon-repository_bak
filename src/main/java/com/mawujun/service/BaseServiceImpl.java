package com.mawujun.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.mawujun.exception.exceptions.BizException;
import com.mawujun.repository.mybatis.IRepository;
import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.Cnd;
import com.mawujun.repository.utils.Page;

@Transactional(rollbackOn= {Exception.class})
public class BaseServiceImpl<M extends IRepository<T>, T> implements IBaseService<T> {

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
	public T create(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return repo.create(map);
	}

	@Override
	public List<T> create(List<T> list) {
		// TODO Auto-generated method stub
		return repo.create(list);
	}

	@Override
	public List<T> create(T... list) {
		// TODO Auto-generated method stub
		return repo.create(list);
	}

	@Override
	public T save(T t) {
		// TODO Auto-generated method stub
		return repo.save(t);
	}

	@Override
	public List<T> save(List<T> list) {
		// TODO Auto-generated method stub
		return repo.save(list);
	}

	@Override
	public List<T> save(T... list) {
		// TODO Auto-generated method stub
		return repo.save(list);
	}

	@Override
	public T getById(Serializable id) {
		// TODO Auto-generated method stub
		return repo.getById(id);
	}

	@Override
	public T get(Map<String, Object> params) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return repo.get(params);
	}

	@Override
	public T get(T params) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return repo.get(params);
	}

	@Override
	public List<T> list(T params) {
		// TODO Auto-generated method stub
		return repo.list(params);
	}

//	@Override
//	public Page<T> listPageByExample(T params, int page, int limit) {
//		// TODO Auto-generated method stub
//		return repo.listPage(params, page, limit);
//	}

	@Override
	public List<T> listAll() {
		// TODO Auto-generated method stub
		return repo.listAll();
	}

	@Override
	public List<T> list(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.list(params);
	}

	@Override
	public Page<T> page(Cnd cnd) {
		if(!cnd.isPageCondition()) {
			throw new BizException("必须添加分页参数：start或page，limit");
		}
		// TODO Auto-generated method stub
		return repo.page(cnd);
	}

	@Override
	public Page<T> page(Map<String, Object> params, int page, int limit) {
		// TODO Auto-generated method stub
		return repo.page(params, page, limit);
	}
	@Override
	public Page<T> page(T params, int page, int limit) {
		return repo.page(params, page, limit);
	}

	@Override
	public Page<T> page(Map<String,Object> params){
//		Page<T> page=new Page<T>();
//		page.init(params);
//		return page;
		Cnd cnd=Cnd.of(params);
		if(!cnd.isPageCondition()) {
			throw new BizException("必须添加分页参数：start或page，limit");
		}
		return repo.page(cnd);
	}

	@Override
	public T update(T t) {
		// TODO Auto-generated method stub
		return repo.update(t);
	}

	@Override
	public List<T> update(List<T> list) {
		// TODO Auto-generated method stub
		return repo.update(list);
	}

	@Override
	public List<T> update(T... list) {
		// TODO Auto-generated method stub
		return repo.update(list);
	}

	@Override
	public int updateByMap(Map<String, Object> sets, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.update(sets, params);
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
	public int remove(T... list) {
		// TODO Auto-generated method stub
		return repo.remove(list);
	}

	@Override
	public int remove(List<T> list) {
		// TODO Auto-generated method stub
		return repo.remove(list);
	}
	@Override
	public int removeForce(T t) {
		// TODO Auto-generated method stub
		return repo.removeForce(t);
	}
	/**
	 * 强制删除，即使注解了@LogicDelect字段，也会被强制删除
	 * @param t
	 * @return
	 */
	public int removeForce(T... list) {
		return repo.removeForce(list);
	}
	/**
	 * 强制删除，即使注解了@LogicDelect字段，也会被强制删除
	 * @param t
	 * @return
	 */
	public int removeForce(List<T> list) {
		return repo.removeForce(list);
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
	public int remove(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.remove(params);
	}
	

	@Override
	public int removeForce(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.remove(params);
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
	public int removeByIds(Serializable[] ids) {
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
	public long count(T params) {
		// TODO Auto-generated method stub
		return repo.count(params);
	}

	@Override
	public long count(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.count(params);
	}

	@Override
	public boolean existsById(Serializable id) {
		// TODO Auto-generated method stub
		return repo.existsById(id);
	}

	@Override
	public boolean exists(T params) {
		// TODO Auto-generated method stub
		return repo.exists(params);
	}

	@Override
	public boolean exists(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return repo.exists(params);
	}

	@Override
	public BeanMap getMapById(Serializable id, String... fields) {
		// TODO Auto-generated method stub
		return repo.getMapById(id, fields);
	}

	@Override
	public BeanMap getMap(Map<String, Object> params, String... fields)
			throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return repo.getMap(params, fields);
	}

	@Override
	public List<BeanMap> listMap(Map<String, Object> params, String... fields) {
		// TODO Auto-generated method stub
		return repo.listMap(params, fields);
	}

	@Override
	public int update(Cnd cnd) {
		// TODO Auto-generated method stub
		return repo.update(cnd);
	}





	

 
}
