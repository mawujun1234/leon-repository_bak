package test.mawujun.jpa;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mawujun.repository.mybatis.IRepository;
import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.Page;

import test.mawujun.model.City;

@Mapper
public interface JpaMybatisMapper extends IRepository<City>{

	public Page<City> listPageByMybatis(Page<City> params);
	
	public Page<City> listPageByMybatis1(Page<City> params);
	
	public City getById__(String id);
	/**
	 * BeanMap不区分大小写
	 * @param id
	 * @return
	 */
	public BeanMap getMapById__(String id);
	/**
	 * 接收map或实体对象
	 * @param params
	 * @return
	 */
	public List<City> listByParams__(Object params);

}
