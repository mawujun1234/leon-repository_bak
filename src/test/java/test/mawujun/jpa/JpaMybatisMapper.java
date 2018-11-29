package test.mawujun.jpa;

import org.apache.ibatis.annotations.Mapper;

import com.mawujun.repository.mybatis.IRepository;
import com.mawujun.repository.utils.PageInfo;

import test.mawujun.model.City;

@Mapper
public interface JpaMybatisMapper extends IRepository<City,String>{

	public PageInfo<City> listPageByMybatis(PageInfo<City> params);

}
