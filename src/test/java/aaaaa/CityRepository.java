package aaaaa;

import org.springframework.stereotype.Repository;

import com.mawujun.repository.mybatis.IRepository;
import org.apache.ibatis.annotations.Mapper;

import test.mawujun.model.City;
/**
 * @author mawujun qq:16064988 e-mail:16064988@qq.com 
 * @version 1.0
 * @since 1.0
 */
@Mapper
public interface CityRepository extends IRepository<City>{
	/**
	 * 这些注释掉的都是demo，可以拷贝出来用，并且如果不自定义方法，是不需要编写或拷贝xxxMapper.xml文件的。
	 */
//public PageInfo<City> listPageByMybatis(PageInfo<City> params);
//	
//	public PageInfo<City> listPageByMybatis1(PageInfo<City> params);
//	
//	public City getById__(String id);
//	/**
//	 * BeanMap不区分大小写
//	 * @param id
//	 * @return
//	 */
//	public BeanMap getMapById__(String id);
//	/**
//	 * 接收map或实体对象
//	 * @param params
//	 * @return
//	 */
//	public List<City> listByParams__(Object params);


}
