package test.mawujun.repository1;

import org.apache.ibatis.annotations.Mapper;

import com.mawujun.repository.mybatis.IRepository;

import test.mawujun.model.City;

@Mapper
public interface CityMapper1 extends IRepository<City,String>{


}
