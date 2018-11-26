package test.mawujun.repository1;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.mawujun.repository.mybatis.extend.NewApplicationListenerConfig;
import com.mawujun.repository.mybatis.typeAliases.BeanMap;
import com.mawujun.repository.utils.Params;

import test.mawujun.model.City;
import test.mawujun.model.Sex;





@RunWith(SpringRunner.class)
@SpringBootTest(classes={JpaMybatisApp.class,NewApplicationListenerConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
@Rollback(false)
public class JpaMybatisTest {
	@Autowired
	private JpaMybatisMapper jpaMybatisMapper;
	
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
		int result=jpaMybatisMapper.create(city);
		
		Assert.assertEquals(1, result);
		id=city.getId();
		Assert.assertNotNull(id);
		
		city = jpaMybatisMapper.getById(id);
		//
		Assert.assertEquals("宁波", city.getName());
		Assert.assertEquals((Double) 10.253, city.getPrice());
		Assert.assertEquals((Integer) 50, city.getAge());
		Assert.assertEquals(Sex.Man, city.getSex());
		Assert.assertEquals(now.getTime(), city.getCreateDate().getTime());
	}
	
	@Test
	public void test1() {
		City city = jpaMybatisMapper.getById(id);
		//
		Assert.assertEquals("宁波", city.getName());
		Assert.assertEquals((Double) 10.253, city.getPrice());
		Assert.assertEquals((Integer) 50, city.getAge());
		Assert.assertEquals(Sex.Man, city.getSex());
		Assert.assertEquals(now.getTime(), city.getCreateDate().getTime());
		
//		Params params = Params.getInstance().add("sex", Sex.Man);
//		//Params params = Params.getInstance().add("sex", "Man");//这样就不行，后端要转换成
//		//		.add("createDate", "2018-11-26 11:02:02");
//		List<City> list = cityMapper.listByMap(params);
//		Assert.assertEquals(1, list.size());

		// 测试参数绑定，构建一个ParamsUtils返回一个map作为参数
		Params paramsUtils = Params.getInstance().add("name", "宁波").add("sex", Sex.Man).add("createDate", now);
		//city = cityMapper.getByMap(paramsUtils.getParams());
		city = jpaMybatisMapper.getByMap(paramsUtils);
		Assert.assertNotNull(city);
		Assert.assertEquals("宁波", city.getName());
		Assert.assertEquals((Double) 10.253, city.getPrice());
		Assert.assertEquals((Integer) 50, city.getAge());
		Assert.assertEquals(Sex.Man, city.getSex());
		Assert.assertEquals(now.getTime(), city.getCreateDate().getTime());
		
		City params =new City();
		params.setId(id);
		params.setName("宁波");
		city=jpaMybatisMapper.getByEntity(params);
		Assert.assertNotNull(city);
		Assert.assertEquals("宁波", city.getName());
		Assert.assertEquals((Double) 10.253, city.getPrice());
		Assert.assertEquals((Integer) 50, city.getAge());
		Assert.assertEquals(Sex.Man, city.getSex());
		Assert.assertEquals(now.getTime(), city.getCreateDate().getTime());
		
		paramsUtils = Params.getInstance().addIn("name", "宁波","杭州","苏州").addLike("sex", Sex.Man);
		Assert.assertEquals("'宁波','杭州','苏州'", paramsUtils.getParams().get("name"));
		Assert.assertEquals("%Man%", paramsUtils.getParams().get("sex"));
		
		
		
	}
//	/**
//	 * 测试列名大小写无关
//	 */
//	@Test
//	public void test2() {	
//		BeanMap city = jpaMybatisMapper.getMapById(id);
//		Assert.assertEquals("宁波", city.get("name"));
//		Assert.assertEquals((Double) 10.253, (Double)city.get("price"));
//		Assert.assertEquals((Integer) 50, (Integer)city.get("Age"));
//		Assert.assertEquals("Man", city.get("sex"));
//		Assert.assertEquals(now.getTime(), ((Date)city.get("createDate")).getTime());
//		
//		Params paramsUtils = Params.getInstance().add("name", "宁波").add("sex", Sex.Man).add("createDate", now);
//		city = jpaMybatisMapper.getMapByMap(paramsUtils.getParams());
//		Assert.assertEquals("宁波", city.get("name"));
//		Assert.assertEquals((Double) 10.253, (Double)city.get("price"));
//		Assert.assertEquals((Integer) 50, (Integer)city.get("Age"));
//		Assert.assertEquals("Man", city.get("sex"));
//		Assert.assertEquals(now.getTime(), ((Date)city.get("createDate")).getTime());
//		
//		City params =new City();
//		params.setId(id);
//		params.setName("宁波");
//		city=jpaMybatisMapper.getMapByEntity(params);
//		Assert.assertEquals("宁波", city.get("name"));
//		Assert.assertEquals((Double) 10.253, (Double)city.get("price"));
//		Assert.assertEquals((Integer) 50, (Integer)city.get("Age"));
//		Assert.assertEquals("Man", city.get("sex"));
//		Assert.assertEquals(now.getTime(), ((Date)city.get("createDate")).getTime());
//		
//		
//	}
//	@Test
//	public void test3() {
//		City city=new City();
//		city.setName("杭州");
//		city.setPrice(10.253);
//		city.setAge(50);
//		city.setSex(Sex.Women);
//		city.setCreateDate(now);
//		int result=cityMapper.create(city);
//		Assert.assertEquals(1, result);
//		
//		List<City> list=cityMapper.listByMap(null);
//		Assert.assertEquals(2, list.size());
//		City city0=list.get(0);
//		Assert.assertEquals("宁波", city0.getName());
//		Assert.assertEquals((Double)10.253, city0.getPrice());
//		Assert.assertEquals((Integer)50, city0.getAge());
//		Assert.assertEquals(Sex.Man, city0.getSex());
//		Assert.assertEquals(now.getTime(), city0.getCreateDate().getTime());
//		City city1=list.get(1);
//		Assert.assertEquals("杭州", city1.getName());
//		Assert.assertEquals((Double)10.253, city1.getPrice());
//		Assert.assertEquals((Integer)50, city1.getAge());
//		Assert.assertEquals(Sex.Women, city1.getSex());
//		Assert.assertEquals(now.getTime(), city1.getCreateDate().getTime());
//		
//		
//		Params paramsUtils = Params.getInstance().add("name", "宁波").add("sex", Sex.Man).add("createDate", now);
//		list=cityMapper.listByMap(paramsUtils);
//		Assert.assertEquals(1, list.size());
//		city0=list.get(0);
//		Assert.assertEquals("宁波", city0.getName());
//		Assert.assertEquals((Double)10.253, city0.getPrice());
//		Assert.assertEquals((Integer)50, city0.getAge());
//		Assert.assertEquals(Sex.Man, city0.getSex());
//		Assert.assertEquals(now.getTime(), city0.getCreateDate().getTime());
//		
//		
//		list=cityMapper.listByEntity(city);
//		Assert.assertEquals(1, list.size());
//		city1=list.get(0);
//		Assert.assertEquals("杭州", city1.getName());
//		Assert.assertEquals((Double)10.253, city1.getPrice());
//		Assert.assertEquals((Integer)50, city1.getAge());
//		Assert.assertEquals(Sex.Women, city1.getSex());
//		Assert.assertEquals(now.getTime(), city1.getCreateDate().getTime());
//		
//	}
//	
//	@Test
//	public void test4update() {
//		City city=new City();
//		city.setId(id);
//		//city.setName("宁波");
//		city.setPrice(22.253);
//		city.setAge(40);
//		//city.setSex(Sex.Man);
//		//city.setCreateDate(now);
//		
//		cityMapper.update(city);
//		City city0 = cityMapper.getById(id);
//		Assert.assertEquals("宁波", city0.getName());
//		Assert.assertEquals((Double) 22.253, city0.getPrice());
//		Assert.assertEquals((Integer) 40, city0.getAge());
//		Assert.assertEquals(Sex.Man, city0.getSex());
//		Assert.assertEquals(now.getTime(), city0.getCreateDate().getTime());
//		
//		Params paramsUtils = Params.getInstance().add("id", id).add("price", 11.11).add("sex", Sex.Women);
//		cityMapper.updateByMap(paramsUtils);
//		city0 = cityMapper.getById(id);
//		Assert.assertEquals("宁波", city0.getName());
//		Assert.assertEquals((Double) 11.11, city0.getPrice());
//		Assert.assertEquals((Integer) 40, city0.getAge());
//		Assert.assertEquals(Sex.Women, city0.getSex());
//		Assert.assertEquals(now.getTime(), city0.getCreateDate().getTime());
//		
//		
//	}
//	
//	@Test
//	public void test5remove() {
//		//这样就会把所有的数据删除了，这个问题要避免
////		City city=new City();
////		int result =cityMapper.remove(city);
////		Assert.assertEquals(0, result);
//		
//		City city=new City();
//		city.setName("杭州");
//		int result =cityMapper.remove(city);
//		Assert.assertEquals(1, result);
//		List<City> list=cityMapper.listByMap(null);
//		Assert.assertEquals(1, list.size());
//		
//		
//		
//		Params params = Params.getInstance().add("name", "宁波");
//		result =cityMapper.removeByMap(params);
//		Assert.assertEquals(1, result);
//		list=cityMapper.listByMap(null);
//		Assert.assertEquals(0, list.size());
//
//	}
//	
//	@Test
//	public void test6remove() {
//		test();
//		int result =cityMapper.removeById(id);
//		Assert.assertEquals(1, result);
//		List<City> list=cityMapper.listByMap(null);
//		Assert.assertEquals(0, list.size());
//		
//		test();
//		City city=new City();
//		city.setName("杭州");
//		city.setPrice(10.253);
//		city.setAge(50);
//		city.setSex(Sex.Women);
//		city.setCreateDate(now);
//		result=cityMapper.create(city);
//		Assert.assertEquals(1, result);
//		list=cityMapper.listByMap(null);
//		Assert.assertEquals(2, list.size());
//		String id1=city.getId();
//		
//		result=cityMapper.removeByIds(id,id1);
//		Assert.assertEquals(2, result);
//		list=cityMapper.listByMap(null);
//		Assert.assertEquals(0, list.size());
//		
//		
//	}
//	
//	@Test
//	public void test7getCounts() {
//		int count=cityMapper.getCounts(null);
//		Assert.assertEquals(0, count);
//		
//		test();
//		count=cityMapper.getCounts(null);
//		Assert.assertEquals(1, count);
//		
//		City city=new City();
//		city.setName("杭州");
//		city.setPrice(10.253);
//		city.setAge(50);
//		city.setSex(Sex.Women);
//		city.setCreateDate(now);
//		int result=cityMapper.create(city);
//		Assert.assertEquals(1, result);
//		
//		count=cityMapper.getCounts(null);
//		Assert.assertEquals(2, count);
//		
//		Params params = Params.getInstance().add("name", "宁波");
//		count=cityMapper.getCounts(params);
//		Assert.assertEquals(1, count);
//		
//		City t_params=new City();
//		t_params.setName("杭州");
//		count=cityMapper.getCountsByEntity(t_params);
//		Assert.assertEquals(1, count);
//		
//		cityMapper.removeById(id);
//		cityMapper.remove(city);
//		count=cityMapper.getCountsByEntity(null);
//		Assert.assertEquals(0, count);
//		
//		
//	}
//	
//	public void test8createBatch() {
//		City city=new City();
//		city.setName("宁波");
//		city.setPrice(10.253);
//		city.setAge(50);
//		city.setSex(Sex.Man);
//		city.setCreateDate(now);
//		
//		City city1=new City();
//		city1.setName("杭州");
//		city1.setPrice(10.253);
//		city1.setAge(50);
//		city1.setSex(Sex.Women);
//		city1.setCreateDate(now);
//		
//		List<City> list=new ArrayList<City>();
//		list.add(city);
//		list.add(city1);
//		
//		int result=cityMapper.createBatch(list);
//		Assert.assertEquals(2, result);
//		int count=cityMapper.getCountsByEntity(null);
//		Assert.assertEquals(2, count);
//		
//		result=cityMapper.removeByIds(city.getId(),city1.getId());
//		Assert.assertEquals(2, result);
//		count=cityMapper.getCountsByEntity(null);
//		Assert.assertEquals(0, count);
//	}
//	
//	public void test8updateBatch() {
//		City city=new City();
//		city.setName("宁波");
//		city.setPrice(10.253);
//		city.setAge(50);
//		city.setSex(Sex.Man);
//		city.setCreateDate(now);
//		
//		City city1=new City();
//		city1.setName("杭州");
//		city1.setPrice(10.253);
//		city1.setAge(50);
//		city1.setSex(Sex.Women);
//		city1.setCreateDate(now);
//		
//		List<City> list=new ArrayList<City>();
//		list.add(city);
//		list.add(city1);
//		
//		int result=cityMapper.createBatch(list);
//		Assert.assertEquals(2, result);
//		int count=cityMapper.getCountsByEntity(null);
//		Assert.assertEquals(2, count);
//		
//		city.setName("宁波1");
//		city1.setName("杭州1");
//		city1.setPrice(789.123);
//		result=cityMapper.updateBatch(list);
//		Assert.assertEquals(2, result);
//		
//		
//		City city3 = cityMapper.getById(city.getId());
//		Assert.assertEquals("宁波1", city3.getName());
//		Assert.assertEquals((Double) 10.253, city3.getPrice());
//		Assert.assertEquals((Integer) 50, city3.getAge());
//		Assert.assertEquals(Sex.Man, city3.getSex());
//		Assert.assertEquals(now.getTime(), city3.getCreateDate().getTime());
//		
//		City city4=cityMapper.getById(city1.getId());
//		Assert.assertEquals("杭州1", city4.getName());
//		Assert.assertEquals((Double)789.123, city4.getPrice());
//		Assert.assertEquals((Integer)50, city4.getAge());
//		Assert.assertEquals(Sex.Women, city4.getSex());
//		Assert.assertEquals(now.getTime(), city4.getCreateDate().getTime());
//		
//		city.setName("宁波2");
//		city1.setName("杭州2");
//		cityMapper.updateBatch(list.get(0),list.get(1));
//		
//		city3 = cityMapper.getById(city.getId());
//		Assert.assertEquals("宁波2", city3.getName());
//		Assert.assertEquals((Double) 10.253, city3.getPrice());
//		Assert.assertEquals((Integer) 50, city3.getAge());
//		Assert.assertEquals(Sex.Man, city3.getSex());
//		Assert.assertEquals(now.getTime(), city3.getCreateDate().getTime());
//		
//		city4=cityMapper.getById(city1.getId());
//		Assert.assertEquals("杭州2", city4.getName());
//		Assert.assertEquals((Double)789.123, city4.getPrice());
//		Assert.assertEquals((Integer)50, city4.getAge());
//		Assert.assertEquals(Sex.Women, city4.getSex());
//		Assert.assertEquals(now.getTime(), city4.getCreateDate().getTime());
//	}
//	
//	@Test
//	public void test8listPageByMap() {
//		City city=new City();
//		city.setName("宁波");
//		city.setPrice(10.253);
//		city.setAge(50);
//		city.setSex(Sex.Man);
//		city.setCreateDate(now);
//		
//		City city1=new City();
//		city1.setName("杭州");
//		city1.setPrice(10.253);
//		city1.setAge(50);
//		city1.setSex(Sex.Women);
//		city1.setCreateDate(now);
//		
////		List<City> list=new ArrayList<City>();
////		list.add(city);
////		list.add(city1);
//		
//		cityMapper.create(city);
//		cityMapper.create(city1);
//		int count=cityMapper.getCountsByEntity(null);
//		Assert.assertEquals(2, count);
//		
//		PageInfo<City> params=new PageInfo<City>();
//		params.setCountColumn("id");
//		PageInfo<City> pageinfo=cityMapper.listPageByMap(params);
//		Assert.assertEquals(pageinfo, params);
//		Assert.assertEquals(2, params.getTotal());
//		Assert.assertEquals(2, params.getRootSize());
//
//		params=new PageInfo<City>();
//		params.setLimit(1);
//		pageinfo=cityMapper.listPageByMap(params);
//		Assert.assertEquals(2, params.getTotal());
//		Assert.assertEquals(1, params.getRootSize());
//		
//		params=new PageInfo<City>();
//		params.addParam("name", "宁波");
//		pageinfo=cityMapper.listPageByMap(params);
//		Assert.assertEquals(1, params.getTotal());
//		Assert.assertEquals(1, params.getRootSize());
//		
//		
//	}

}
