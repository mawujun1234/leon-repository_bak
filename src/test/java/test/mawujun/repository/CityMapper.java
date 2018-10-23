package test.mawujun.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CityMapper {
	public City get(String id);

}
