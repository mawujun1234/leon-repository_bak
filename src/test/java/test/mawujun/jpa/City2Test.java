package test.mawujun.jpa;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import test.mawujun.model.City2;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={JpaMybatisApp.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
@Rollback(false)
@ActiveProfiles("h2")
public class City2Test {
	@Autowired
	private City2Mapper city2Mapper;
	
	@Test
	public void test() {
		City2 city2=new City2();
		city2.setName("测试");
		city2Mapper.create(city2);
		Assert.assertNotNull(city2.getId());
	}

}
