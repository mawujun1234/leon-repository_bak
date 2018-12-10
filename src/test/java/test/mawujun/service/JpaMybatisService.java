package test.mawujun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.mawujun.service.BaseService;

import test.mawujun.jpa.JpaMybatisMapper;
import test.mawujun.model.City;

@Service
//@DependsOn({"jpaMapperListener"})
public class JpaMybatisService extends BaseService<JpaMybatisMapper, City> {

	
	//protected JpaMybatisMapper repository;
	@Autowired
	public void setRepository(JpaMybatisMapper repository ) {
		super.repository=repository;
	}
}
