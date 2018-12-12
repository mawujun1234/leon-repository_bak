package test.mawujun.jpa.service;

import org.springframework.stereotype.Service;

import com.mawujun.service.BaseService;

import lombok.extern.slf4j.Slf4j;
import test.mawujun.jpa.JpaMybatisMapper;
import test.mawujun.model.City;

@Service
@Slf4j
public class JpaMybatisService extends BaseService<JpaMybatisMapper, City> {

//	
//	//protected JpaMybatisMapper repository;
//	@Autowired
//	public void setRepository(JpaMybatisMapper repository ) {
//		super.repository=repository;
//	}
}
