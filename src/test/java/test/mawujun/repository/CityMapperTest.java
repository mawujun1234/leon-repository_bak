package test.mawujun.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;





@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CityMapperTest {
	@Autowired
	private CityMapper cityMapper;
	
	private static String id;
	private static Date now=new Date();
	
	@Test
	public void test() {
		City city=new City();
		city.setName("宁波");
		city.setPrice(10.253);
		city.setAge(50);
		city.setSex(Sex.Man);
		city.setCreateDate(now);
		int result=cityMapper.create(city);
		
		Assert.assertEquals(1, result);
		id=city.getId();
		Assert.assertNotNull(id);
	}
	
	@Test
	public void test1() {
		City city=cityMapper.getById(id);
		//
		Assert.assertEquals("宁波", city.getName());
		Assert.assertEquals((Double)10.253, city.getPrice());
		Assert.assertEquals((Integer)50, city.getAge());
		Assert.assertEquals(Sex.Man, city.getSex());
		Assert.assertEquals(now.getTime(), city.getCreateDate().getTime());
		
		
	}
	
	public void test2() {
		List<City> list=cityMapper.getListByMap(null);
		Assert.assertEquals(1, list.size());
		City city=list.get(0);
		Assert.assertEquals("宁波", city.getName());
		Assert.assertEquals((Double)10.253, city.getPrice());
		Assert.assertEquals((Integer)50, city.getAge());
		Assert.assertEquals(Sex.Man, city.getSex());
		Assert.assertEquals(now.getTime(), city.getCreateDate().getTime());
		
		//测试参数绑定，构建一个ParamsUtils返回一个map作为参数
		Map<String,Object> params=new HashMap<String,Object>();
		params
		
		//测试实体作为一个参数
		
	}

}
