package ${basepackage}.repository;

import org.springframework.stereotype.Repository;

import com.mawujun.repository.mybatis.IRepository;
import org.apache.ibatis.annotations.Mapper;

import ${entityClassName};
<#include "/java_copyright.include"/>

@Mapper
public interface ${entitySimpleClassName}Repository extends IRepository<${entitySimpleClassName}>{
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
