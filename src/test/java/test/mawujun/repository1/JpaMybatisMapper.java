package test.mawujun.repository1;

import org.apache.ibatis.annotations.Mapper;

import com.mawujun.repository.mybatis.IRepository;

import test.mawujun.model.City;

@Mapper
public interface JpaMybatisMapper extends IRepository<City,String>{


}
