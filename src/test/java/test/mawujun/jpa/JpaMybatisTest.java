package test.mawujun.jpa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mawujun.repository.mybatis.extend.JpaMapperListenerConfig;
import com.mawujun.repository.utils.PageInfo;
import com.mawujun.repository.utils.Params;
import com.mawujun.utils.DateUtils;

import test.mawujun.model.City;
import test.mawujun.model.CoplxId1;
import test.mawujun.model.CoplxId1Entity;
import test.mawujun.model.CoplxId2;
import test.mawujun.model.CoplxId2Entity;
import test.mawujun.model.Sex;




/**
 * save,saveBatch,saveBatchByArray还未测试
 * updateBatch,updateBatchByArray.还未测试
 * create,createBatch,createBatchByArray还未测试
 * existsById,existsByExample,existsByMap还未测试
 * 
 * 时间条件测试，特别是3种形式的时间
 * @author admin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={JpaMybatisApp.class,JpaMapperListenerConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
@Rollback(false)
//@ActiveProfiles("sqlserver")//h2,hsqldb,derby,mysql,sqlserver,oracle,db2,postgresql
@ActiveProfiles("h2")
public class JpaMybatisTest {
	@Autowired
	private JpaMybatisMapper jpaMybatisMapper;
	@Autowired
	private CoplxId1EntityMapper coplxId1EntityMapper;
	@Autowired
	private CoplxId2EntityMapper coplxId2EntityMapper;
	
	
	@Autowired
	protected ApplicationContext ctx;

	@Value("${spring.profiles.active}")
	private String profile;
	
	private static String id;
	private static Date now=new Date();

	
	@Test
	public void atest() {
		Assert.assertNotNull(ctx);
		Assert.assertNotNull(profile);
		jpaMybatisMapper.removeAll();
	}
	@Test
	public void test() {
		
		
		City city=new City();
		city.setName("宁波");
		city.setPrice(10.253);
		city.setAge(50);
		city.setSex(Sex.Man);
		city.setCreateDate(now);
		jpaMybatisMapper.create(city);
		//Assert.assertEquals(1, result);
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
	public void test0GetId() {
		Class clz=jpaMybatisMapper.getIdType();
		Assert.assertEquals(String.class, clz);
		String ids=jpaMybatisMapper.getIdAttributeNames2Str();
		Assert.assertEquals("id", ids);
		Assert.assertEquals(1, jpaMybatisMapper.getIdAttributeNames().size());
		
		
		clz=coplxId1EntityMapper.getIdType();
		Assert.assertEquals(CoplxId1.class, clz);
		ids=coplxId1EntityMapper.getIdAttributeNames2Str();
		Assert.assertEquals("id", ids);//属性名还是老的
		Assert.assertEquals(1, coplxId1EntityMapper.getIdAttributeNames().size());


		
		clz=coplxId2EntityMapper.getIdType();
		Assert.assertEquals(CoplxId2.class, clz);
		ids=coplxId2EntityMapper.getIdAttributeNames2Str();
		if(ids.indexOf("id1")==0) {
			Assert.assertEquals("id1,id2", ids);//属性名还是老的
		} else {
			Assert.assertEquals("id2,id1", ids);//属性名还是老的
		}
		Assert.assertEquals(2, coplxId2EntityMapper.getIdAttributeNames().size());
	}
	
	@Test
	public void test1get() {
		City city = jpaMybatisMapper.getById(id);
		//
		Assert.assertEquals("宁波", city.getName());
		Assert.assertEquals((Double) 10.253, city.getPrice());
		Assert.assertEquals((Integer) 50, city.getAge());
		Assert.assertEquals(Sex.Man, city.getSex());
		Assert.assertEquals(now.getTime(), city.getCreateDate().getTime());
		
//		Params params = Params.of().add("sex", Sex.Man);
//		//Params params = Params.of().add("sex", "Man");//这样就不行，后端要转换成
//		//		.add("createDate", "2018-11-26 11:02:02");
//		List<City> list = jpaMybatisMapper.listByMap(params);
//		Assert.assertEquals(1, list.size());

		// 测试参数绑定，构建一个ParamsUtils返回一个map作为参数
		Params paramsUtils = Params.of().add("name", "宁波").add("sex", Sex.Man).add("createDate", now);
		//city = jpaMybatisMapper.getByMap(paramsUtils.getParams());
		city = jpaMybatisMapper.getByMap(paramsUtils);
		Assert.assertNotNull(city);
		Assert.assertEquals("宁波", city.getName());
		Assert.assertEquals((Double) 10.253, city.getPrice());
		Assert.assertEquals((Integer) 50, city.getAge());
		Assert.assertEquals(Sex.Man, city.getSex());
		Assert.assertEquals(now.getTime(), city.getCreateDate().getTime());
		
		City params =new City();
		//params.setId(id);
		params.setName("宁波");
		city=jpaMybatisMapper.getByExample(params);
		Assert.assertNotNull(city);
		Assert.assertEquals("宁波", city.getName());
		Assert.assertEquals((Double) 10.253, city.getPrice());
		Assert.assertEquals((Integer) 50, city.getAge());
		Assert.assertEquals(Sex.Man, city.getSex());
		Assert.assertEquals(now.getTime(), city.getCreateDate().getTime());
		
		boolean exists=jpaMybatisMapper.existsByExample(params);
		Assert.assertEquals(true, exists);
		params.setAge(11);
		exists=jpaMybatisMapper.existsByExample(params);
		Assert.assertEquals(false, exists);
		
		params.setAge(50);
		long count=jpaMybatisMapper.countByExample(params);
		Assert.assertEquals(1, count);
		params.setAge(11);
		count=jpaMybatisMapper.countByExample(params);
		Assert.assertEquals(0, count);
		
		params.setAge(50);
		List<City> list=jpaMybatisMapper.listByExample(params);
		Assert.assertEquals(1, list.size());
		params.setAge(11);
		list=jpaMybatisMapper.listByExample(params);
		Assert.assertEquals(0, list.size());
		
		params.setAge(50);
//		PageInfo<City> pagerequest=new PageInfo<City>();
//		pagerequest.setPage(0);
//		pagerequest.setLimit(1);
		PageInfo<City> pageinfo=jpaMybatisMapper.listPageByExample(params, 0,1);
		Assert.assertEquals(1, pageinfo.getTotal());
		Assert.assertEquals(1, pageinfo.getTotalPages());
		Assert.assertEquals(0, pageinfo.getPage());
		Assert.assertEquals(1, pageinfo.getLimit());
		Assert.assertEquals(1, pageinfo.getRootSize());
		params.setAge(11);
		pageinfo=jpaMybatisMapper.listPageByExample(params, 0,1);
		Assert.assertEquals(0, pageinfo.getTotal());
		Assert.assertEquals(0, pageinfo.getTotalPages());
		Assert.assertEquals(0, pageinfo.getPage());
		Assert.assertEquals(1, pageinfo.getLimit());
		Assert.assertEquals(0, pageinfo.getRootSize());

		
	}

	@Test
	public void test3list() {
		City city=new City();
		city.setName("杭州");
		city.setPrice(10.253);
		city.setAge(50);
		city.setSex(Sex.Women);
		city.setCreateDate(now);
		jpaMybatisMapper.create(city);
		//Assert.assertEquals(1, result);
		
		List<City> list=jpaMybatisMapper.listAll();
		Assert.assertEquals(2, list.size());
		City city0=list.get(0);
		Assert.assertEquals("宁波", city0.getName());
		Assert.assertEquals((Double)10.253, city0.getPrice());
		Assert.assertEquals((Integer)50, city0.getAge());
		Assert.assertEquals(Sex.Man, city0.getSex());
		Assert.assertEquals(now.getTime(), city0.getCreateDate().getTime());
		City city1=list.get(1);
		Assert.assertEquals("杭州", city1.getName());
		Assert.assertEquals((Double)10.253, city1.getPrice());
		Assert.assertEquals((Integer)50, city1.getAge());
		Assert.assertEquals(Sex.Women, city1.getSex());
		Assert.assertEquals(now.getTime(), city1.getCreateDate().getTime());
		
		
		Params paramsUtils = Params.of().add("name", "宁波").add("sex", Sex.Man).add("createDate", now);
		list=jpaMybatisMapper.listByMap(paramsUtils);
		Assert.assertEquals(1, list.size());
		city0=list.get(0);
		Assert.assertEquals("宁波", city0.getName());
		Assert.assertEquals((Double)10.253, city0.getPrice());
		Assert.assertEquals((Integer)50, city0.getAge());
		Assert.assertEquals(Sex.Man, city0.getSex());
		Assert.assertEquals(now.getTime(), city0.getCreateDate().getTime());
		
		
		list=jpaMybatisMapper.listByExample(city);
		Assert.assertEquals(1, list.size());
		city1=list.get(0);
		Assert.assertEquals("杭州", city1.getName());
		Assert.assertEquals((Double)10.253, city1.getPrice());
		Assert.assertEquals((Integer)50, city1.getAge());
		Assert.assertEquals(Sex.Women, city1.getSex());
		Assert.assertEquals(now.getTime(), city1.getCreateDate().getTime());
		
		
		paramsUtils = Params.of().add("age", 50);
		PageInfo<City> oage=jpaMybatisMapper.listPageByMap(paramsUtils, 0, 1);
		Assert.assertEquals(2, oage.getTotal());
		Assert.assertEquals(2, oage.getTotalPages());
		Assert.assertEquals(0, oage.getPage());
		Assert.assertEquals(1, oage.getLimit());
		Assert.assertEquals(1, oage.getRootSize());
	}

	
	@Test
	public void test3listPageByMap() {
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
//		jpaMybatisMapper.create(city);
//		jpaMybatisMapper.create(city1);
		long count=jpaMybatisMapper.countByExample(null);
		Assert.assertEquals(2, count);
		
		City city_params =new City();
		//params.setId(id);
		//params.setName("宁波");
		city_params.setAge(50);
		PageInfo<City> pageinfo=jpaMybatisMapper.listPageByExample(city_params, 0,1);
		Assert.assertEquals(2, pageinfo.getTotal());
		Assert.assertEquals(2, pageinfo.getTotalPages());
		Assert.assertEquals(0, pageinfo.getPage());
		Assert.assertEquals(1, pageinfo.getLimit());
		Assert.assertEquals(1, pageinfo.getRootSize());
		city_params.setAge(11);
		pageinfo=jpaMybatisMapper.listPageByExample(city_params, 0,1);
		Assert.assertEquals(0, pageinfo.getTotal());
		Assert.assertEquals(0, pageinfo.getTotalPages());
		Assert.assertEquals(0, pageinfo.getPage());
		Assert.assertEquals(1, pageinfo.getLimit());
		Assert.assertEquals(0, pageinfo.getRootSize());
		
		//PageInfo<City> params=new PageInfo<City>();
		//params.setCountColumn("id");
		pageinfo=jpaMybatisMapper.listPageByMap(null,0,10);
		Assert.assertEquals(2, pageinfo.getTotal());
		Assert.assertEquals(2, pageinfo.getRootSize());
		Assert.assertEquals(0, pageinfo.getPage());

		pageinfo=jpaMybatisMapper.listPageByMap(null,0,1);
		Assert.assertEquals(2, pageinfo.getTotal());
		Assert.assertEquals(1, pageinfo.getRootSize());
		Assert.assertEquals(0, pageinfo.getPage());
		
		pageinfo=jpaMybatisMapper.listPageByMap(null,1,1);
		Assert.assertEquals(2, pageinfo.getTotal());
		Assert.assertEquals(1, pageinfo.getRootSize());
		Assert.assertEquals(1, pageinfo.getPage());
		
		//params=new PageInfo<City>();
		//params.addParam("name", "宁波");
		Params params=Params.of().add("name", "宁波");
		pageinfo=jpaMybatisMapper.listPageByMap(params,0,10);
		Assert.assertEquals(1, pageinfo.getTotal());
		Assert.assertEquals(1, pageinfo.getRootSize());
		Assert.assertEquals(0, pageinfo.getPage());
		
		params=Params.of().add("name", "宁波");
		pageinfo=jpaMybatisMapper.listPageByMap(params,1,10);
		Assert.assertEquals(1, pageinfo.getTotal());
		Assert.assertEquals(0, pageinfo.getRootSize());
		Assert.assertEquals(1, pageinfo.getPage());
		
		PageInfo<City> pageInfo=PageInfo.of(0, 10).addParam("name", "宁波");
		PageInfo<City> pageinfo_result=jpaMybatisMapper.listPageByPageInfo(pageInfo);
		Assert.assertEquals(pageInfo, pageinfo_result);
		Assert.assertEquals(1, pageinfo_result.getTotal());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
		Assert.assertEquals(0, pageinfo_result.getPage());
		
		pageInfo.setPage(1);
		pageinfo_result=jpaMybatisMapper.listPageByPageInfo(pageInfo);
		Assert.assertEquals(pageInfo, pageinfo_result);
		Assert.assertEquals(1, pageinfo_result.getTotal());
		Assert.assertEquals(0, pageinfo_result.getRootSize());
		Assert.assertEquals(1, pageinfo_result.getPage());
		
		city_params.setAge(50);
		pageInfo=PageInfo.of(0, 1).setParams(city_params);
		pageinfo_result=jpaMybatisMapper.listPageByPageInfo(pageInfo);
		Assert.assertEquals(2, pageinfo_result.getTotal());
		Assert.assertEquals(2, pageinfo_result.getTotalPages());
		Assert.assertEquals(0, pageinfo_result.getPage());
		Assert.assertEquals(1, pageinfo_result.getLimit());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
		city_params.setAge(11);
		pageInfo=PageInfo.of(0, 1).setParams(city_params);
		pageinfo_result=jpaMybatisMapper.listPageByPageInfo(pageInfo);
		Assert.assertEquals(0, pageinfo_result.getTotal());
		Assert.assertEquals(0, pageinfo_result.getTotalPages());
		Assert.assertEquals(0, pageinfo_result.getPage());
		Assert.assertEquals(1, pageinfo_result.getLimit());
		Assert.assertEquals(0, pageinfo_result.getRootSize());
		
		//mybatis中的分页测试
		pageInfo=PageInfo.of(0, 10).addParam("name", "宁波");
		pageinfo_result=jpaMybatisMapper.listPageByMybatis(pageInfo);
		Assert.assertEquals(pageInfo, pageinfo_result);
		Assert.assertEquals(1, pageinfo_result.getTotal());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
		Assert.assertEquals(0, pageinfo_result.getPage());
		
		pageInfo.setPage(1);
		pageinfo_result=jpaMybatisMapper.listPageByMybatis(pageInfo);
		Assert.assertEquals(pageInfo, pageinfo_result);
		Assert.assertEquals(1, pageinfo_result.getTotal());
		Assert.assertEquals(0, pageinfo_result.getRootSize());
		Assert.assertEquals(1, pageinfo_result.getPage());
		
		city_params.setAge(50);
		pageInfo=PageInfo.of(0, 1).setParams(city_params);
		pageinfo_result=jpaMybatisMapper.listPageByMybatis(pageInfo);
		Assert.assertEquals(2, pageinfo_result.getTotal());
		Assert.assertEquals(2, pageinfo_result.getTotalPages());
		Assert.assertEquals(0, pageinfo_result.getPage());
		Assert.assertEquals(1, pageinfo_result.getLimit());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
		city_params.setAge(11);
		pageInfo=PageInfo.of(0, 1).setParams(city_params);
		pageinfo_result=jpaMybatisMapper.listPageByMybatis(pageInfo);
		Assert.assertEquals(0, pageinfo_result.getTotal());
		Assert.assertEquals(0, pageinfo_result.getTotalPages());
		Assert.assertEquals(0, pageinfo_result.getPage());
		Assert.assertEquals(1, pageinfo_result.getLimit());
		Assert.assertEquals(0, pageinfo_result.getRootSize());
	}
	/**
	 * 测试，如果Mapper.xml文件中，存在listPage和listPage_count的select，那就不执行自动构建count语句
	 */
	@Test
	public void test3listPageByMap1() {
		PageInfo<City> pageInfo=PageInfo.of(0, 1);
		PageInfo<City> pageinfo_result=jpaMybatisMapper.listPageByMybatis1(pageInfo);
		Assert.assertEquals(pageInfo, pageinfo_result);
		Assert.assertEquals(2, pageinfo_result.getTotal());
		Assert.assertEquals(2, pageinfo_result.getTotalPages());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
		Assert.assertEquals(0, pageinfo_result.getPage());
		
		pageInfo=PageInfo.of(1, 1);
		pageinfo_result=jpaMybatisMapper.listPageByMybatis1(pageInfo);
		Assert.assertEquals(pageInfo, pageinfo_result);
		Assert.assertEquals(2, pageinfo_result.getTotal());
		Assert.assertEquals(2, pageinfo_result.getTotalPages());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
		Assert.assertEquals(1, pageinfo_result.getPage());
	}
	
	@Test
	public void test4update() {
		City city=new City();
		city.setId(id);
		city.setName("宁波");
		city.setPrice(22.253);
		city.setAge(40);
		city.setSex(Sex.Man);
		city.setCreateDate(now);
		
		jpaMybatisMapper.update(city);
		City city0 = jpaMybatisMapper.getById(id);
		Assert.assertEquals("宁波", city0.getName());
		Assert.assertEquals((Double) 22.253, city0.getPrice());
		Assert.assertEquals((Integer) 40, city0.getAge());
		Assert.assertEquals(Sex.Man, city0.getSex());
		Assert.assertEquals(now.getTime(), city0.getCreateDate().getTime());
		
		Params paramsUtils = Params.of().add("price", 11.11).add("sex", Sex.Women);
		int result=jpaMybatisMapper.updateByMap(paramsUtils,Params.of().add("id", id));
		Assert.assertEquals(1, result);
		city0 = jpaMybatisMapper.getById(id);
		Assert.assertEquals("宁波", city0.getName());
		Assert.assertEquals((Double) 11.11, city0.getPrice());
		Assert.assertEquals((Integer) 40, city0.getAge());
		Assert.assertEquals(Sex.Women, city0.getSex());
		Assert.assertEquals(now.getTime(), city0.getCreateDate().getTime());
		
		jpaMybatisMapper.updateById(Params.of().add("price", 22.22).add("sex", Sex.Man),id);
		city0 = jpaMybatisMapper.getById(id);
		Assert.assertEquals("宁波", city0.getName());
		Assert.assertEquals((Double) 22.22, city0.getPrice());
		Assert.assertEquals((Integer) 40, city0.getAge());
		Assert.assertEquals(Sex.Man, city0.getSex());
		Assert.assertEquals(now.getTime(), city0.getCreateDate().getTime());
		
		
		
	}
	@Test
	public void test4updateByCompexId() {
		//如果以单独的id类在外面的时候
		CoplxId1 coplxId1=new CoplxId1();
		coplxId1.setId1("1111");
		coplxId1.setId2("2222");
		CoplxId1Entity coplxId1Entity=new CoplxId1Entity();
		coplxId1Entity.setId(coplxId1);
		coplxId1Entity.setName("coplxId1Entity");
		
		coplxId1EntityMapper.create(coplxId1Entity);
		
		coplxId1EntityMapper.updateById(Params.of("name", "new_coplxId1Entity"),coplxId1);
		CoplxId1Entity coplxId1Entity_=coplxId1EntityMapper.getById(coplxId1);
		Assert.assertEquals("new_coplxId1Entity", coplxId1Entity_.getName());
		//注意这里和下面那种updateByMap的区别
		coplxId1EntityMapper.updateByMap(Params.of("name", "new1_coplxId1Entity"), Params.of("id", coplxId1));
		coplxId1Entity_=coplxId1EntityMapper.getById(coplxId1);
		Assert.assertEquals("new1_coplxId1Entity", coplxId1Entity_.getName());
		
		
		
		
		//通过指定@IdClass(CoplxId2.class)
		CoplxId2Entity coplxId2Entity=new CoplxId2Entity();
		coplxId2Entity.setId1("3333");
		coplxId2Entity.setId2("4444");
		coplxId2Entity.setName("coplxId2Entity");
		coplxId2EntityMapper.create(coplxId2Entity);
		
		coplxId2Entity.setName("coplxId2Entity_new");
		CoplxId2 coplxId2=new CoplxId2();
		coplxId2.setId1(coplxId2Entity.getId1());
		coplxId2.setId2(coplxId2Entity.getId2());
		coplxId2EntityMapper.updateById( Params.of("name", "coplxId2Entity_new"),coplxId2);
		CoplxId2Entity coplxId2Entity_=coplxId2EntityMapper.getById(coplxId2);
		Assert.assertEquals("coplxId2Entity_new", coplxId2Entity_.getName());
		
		coplxId2EntityMapper.updateByMap(Params.of("name", "coplxId2Entity_new2"), Params.of("id1", coplxId2.getId1()).add("id2", coplxId2.getId2()));
		coplxId2Entity_=coplxId2EntityMapper.getById(coplxId2);
		Assert.assertEquals("coplxId2Entity_new2", coplxId2Entity_.getName());
	}
	
	@Test
	public void test5exists() {
		boolean bool=jpaMybatisMapper.existsById(id);
		Assert.assertEquals(true, bool);
		bool=jpaMybatisMapper.existsById("1111");
		Assert.assertEquals(false, bool);
		
		//City city=jpaMybatisMapper.getById(id);
		//jpaMybatisMapper.clear();
		City city=new City();
		city.setName("宁波");
		city.setSex(Sex.Man);
		bool=jpaMybatisMapper.existsByExample(city);
		Assert.assertEquals(true, bool);
		
		city.setId(id);
		bool=jpaMybatisMapper.existsByExample(city);
		Assert.assertEquals(true, bool);
		
		city.setAge(11);;
		bool=jpaMybatisMapper.existsByExample(city);
		Assert.assertEquals(false, bool);
		
		Params params = Params.of().add("name", "杭州");
		bool=jpaMybatisMapper.existsByMap(params);
		Assert.assertEquals(true, bool);
		
		params.add("age", 11);
		bool=jpaMybatisMapper.existsByMap(params);
		Assert.assertEquals(false, bool);
		
	}
	
	@Test
	public void test6remove() {
		List<City> list=jpaMybatisMapper.listByMap(null);
		Assert.assertEquals(2, list.size());	
	
		City city=new City();
		city.setId(id);
		jpaMybatisMapper.remove(city);
		list=jpaMybatisMapper.listByMap(null);
		Assert.assertEquals(1, list.size());	

		
		Params params = Params.of().add("name", "杭州");
		int result =jpaMybatisMapper.removeByMap(params);
		Assert.assertEquals(1, result);
		list=jpaMybatisMapper.listByMap(null);
		Assert.assertEquals(0, list.size());
		
		
		test();
		list=jpaMybatisMapper.listByMap(null);
		Assert.assertEquals(1, list.size());
		result =jpaMybatisMapper.removeById(id);
		Assert.assertEquals(1, result);
		 list=jpaMybatisMapper.listByMap(null);
		Assert.assertEquals(0, list.size());
		
		test();
		city=new City();
		city.setName("杭州");
		city.setPrice(10.253);
		city.setAge(50);
		city.setSex(Sex.Women);
		city.setCreateDate(now);
		jpaMybatisMapper.create(city);
		//Assert.assertEquals(1, result);
		list=jpaMybatisMapper.listByMap(null);
		Assert.assertEquals(2, list.size());
		String id1=city.getId();
		
		result=jpaMybatisMapper.removeByIds(id,id1);
		Assert.assertEquals(2, result);
		list=jpaMybatisMapper.listByMap(null);
		Assert.assertEquals(0, list.size());

	}
	
	
	
	@Test
	public void test7counts() {
		long count=jpaMybatisMapper.count();
		Assert.assertEquals(0, count);
		
		test();
		count=jpaMybatisMapper.count();
		Assert.assertEquals(1, count);
		
		City city=new City();
		city.setName("杭州");
		city.setPrice(10.253);
		city.setAge(50);
		city.setSex(Sex.Women);
		city.setCreateDate(now);
		jpaMybatisMapper.create(city);
		//Assert.assertEquals(1, result);
		
		count=jpaMybatisMapper.countByMap(null);
		Assert.assertEquals(2, count);
		
		Params params = Params.of().add("name", "宁波");
		count=jpaMybatisMapper.countByMap(params);
		Assert.assertEquals(1, count);
		
		City t_params=new City();
		t_params.setName("杭州");
		count=jpaMybatisMapper.countByExample(t_params);
		Assert.assertEquals(1, count);
		
		jpaMybatisMapper.removeById(id);
		jpaMybatisMapper.remove(city);
		count=jpaMybatisMapper.countByExample(null);
		Assert.assertEquals(0, count);
		
		
	}
	
	//位置不能动
	
	@Test
	public void test8heq() throws ParseException {
		Params params = Params.of().eq("age", 50);
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());

		
		City city=jpaMybatisMapper.getById(id);
		String name=city.getName();
		city.setName("AAAAA");
		jpaMybatisMapper.update(city);
		
		//sql server是不区分大小写的
				//ALTER DATABASE [DatabaseName] COLLATE Chinese_PRC_CS_AI 
				//https://www.cnblogs.com/sandunban/p/7272291.html
		params = Params.of().eq("name", "AAAAA");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().eq("name", "aAAAA");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		params = Params.of().eq_i("name", "aAAAA");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().noteq("name", "AAAAA");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().noteq("name", "aAAAA");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().noteq_i("name", "aAAAA");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		city.setName(name);
		jpaMybatisMapper.update(city);
	}
	//时间的专门测试
	private static SimpleDateFormat yyyyMMddHHmmss=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat yyyyMMdd=new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat yyyyMM=new SimpleDateFormat("yyyy-MM");
	private static SimpleDateFormat yyyy=new SimpleDateFormat("yyyy-MM");
	@Test
	public void test8time() throws ParseException {
		Params params = Params.of().eq("age", 50);
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().eq("createDate", now);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().eq("createDate", yyyyMMdd.parse(yyyyMMdd.format(now)));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());//如果是以时间的形式比较的话，要完全相等才可以
		
		params = Params.of().eq("createDate", yyyy.format(now));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().eq("createDate", yyyyMM.format(now));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().eq("createDate", yyyyMMdd.format(now));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().eq("createDate", yyyyMMddHHmmss.format(now));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		

		params = Params.of().between("createDate", yyyyMMdd.format(now),yyyyMMdd.format(DateUtils.addDays(now, 1)));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		//临界点测试
		params = Params.of().between("createDate",yyyyMMdd.parse(yyyyMMdd.format(now)),yyyyMMdd.parse(yyyyMMdd.format(now)));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		params = Params.of().between("createDate", yyyyMMdd.format(now),yyyyMMdd.format(now));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());//		
		params = Params.of().between("createDate", yyyyMMdd.format(DateUtils.addDays(now, -1)),yyyyMMdd.format(now));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());//因为yyyyMMdd.format(now)返回的结果是yyyy-MM-dd 00:00:00  比yyyy-MM-dd 01:22:33小，所以娶不到，需要进行日期格式化
		
		
		params = Params.of().in("createDate", now,DateUtils.addDays(now, 1));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().in("createDate", DateUtils.addDays(now, -1),DateUtils.addDays(now, 1));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		params = Params.of().in("createDate", yyyyMMdd.format(now),yyyyMMdd.format(DateUtils.addDays(now, 1)));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().in("createDate", yyyyMMdd.format(DateUtils.addDays(now, 1)),yyyyMMdd.format(DateUtils.addDays(now, -1)));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());


	}
	/**
	 * 测试各种操作符
	 */
	@Test
	public void test8gtORge() {
		long count=jpaMybatisMapper.count();
		Assert.assertEquals(0, count);
		
		City city=new City();
		city.setName("杭州");
		city.setPrice(10.253);
		city.setAge(50);
		city.setSex(Sex.Women);
		city.setCreateDate(now);
		jpaMybatisMapper.create(city);
		test();
		
		count=jpaMybatisMapper.count();
		Assert.assertEquals(2, count);
		
		Params params = Params.of().gt("age", 49);
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().gt("age", 51);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		params = Params.of().ge("age", 49);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().ge("age", 50);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().ge("age", 51);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		params = Params.of().gt("createDate", now);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		params = Params.of().ge("createDate", now);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		//其他方法也向这边靠拢
		params = Params.of().ge("age", 49);
		PageInfo<City> pageinfo_result=jpaMybatisMapper.listPageByMap(params, 0, 1);
		Assert.assertEquals(2, pageinfo_result.getTotal());
		Assert.assertEquals(2, pageinfo_result.getTotalPages());
		Assert.assertEquals(0, pageinfo_result.getPage());
		Assert.assertEquals(1, pageinfo_result.getLimit());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
		
		pageinfo_result=jpaMybatisMapper.listPageByMap(params, 1, 1);
		Assert.assertEquals(2, pageinfo_result.getTotal());
		Assert.assertEquals(2, pageinfo_result.getTotalPages());
		Assert.assertEquals(1, pageinfo_result.getPage());
		Assert.assertEquals(1, pageinfo_result.getLimit());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
	}
	
	@Test
	public void test8ltORle() {
		long count=jpaMybatisMapper.count();
		Assert.assertEquals(2, count);
		
		Params params = Params.of().lt("age", 49);
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		params = Params.of().lt("age", 51);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().le("age", 49);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		params = Params.of().le("age", 50);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().le("age", 51);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(02, list.size());
		
		params = Params.of().lt("createDate", now);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		params = Params.of().le("createDate", now);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		//其他方法也向这边靠拢
		params = Params.of().le("age", 50);
		PageInfo<City> pageinfo_result=jpaMybatisMapper.listPageByMap(params, 0, 1);
		Assert.assertEquals(2, pageinfo_result.getTotal());
		Assert.assertEquals(2, pageinfo_result.getTotalPages());
		Assert.assertEquals(0, pageinfo_result.getPage());
		Assert.assertEquals(1, pageinfo_result.getLimit());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
		
		pageinfo_result=jpaMybatisMapper.listPageByMap(params, 1, 1);
		Assert.assertEquals(2, pageinfo_result.getTotal());
		Assert.assertEquals(2, pageinfo_result.getTotalPages());
		Assert.assertEquals(1, pageinfo_result.getPage());
		Assert.assertEquals(1, pageinfo_result.getLimit());
		Assert.assertEquals(1, pageinfo_result.getRootSize());
	}
	
	@Test
	public void test8gtbetween() {
		long count=jpaMybatisMapper.count();
		Assert.assertEquals(2, count);
		
		Params params = Params.of().between("age", 49,50);
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().between("age", "49","50");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().between("age", 50,51);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().between("age", 49,51);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().between("age", 51,100);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
	
		
		params = Params.of().between("createDate", now,DateUtils.addDays(now, 1));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().between("createDate", DateUtils.addDays(now, -1),now);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().between("createDate", DateUtils.addDays(now, -2),DateUtils.addDays(now, -1));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());

	}
	
	@Test
	public void test8gtin() {
		Params params = Params.of().in("name", "宁波","杭州");
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().in("age", 50,51);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		params = Params.of().in("age", "50","51");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().in("createDate", now,DateUtils.addDays(now, -1));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void test8gtnotin() {
		Params params = Params.of().notin("name", "宁波","杭州");
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		params = Params.of().notin("age", 49,51);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().notin("age", 50,51);
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		params = Params.of().notin("age", "50","51");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		params = Params.of().notin("createDate", now,DateUtils.addDays(now, -1));
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
	}
	
	@Test
	public void test8hlike() {
		Params params = Params.of().like("name", "宁");
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().like("name", "波");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().likeprefix("name", "宁");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		params = Params.of().likeprefix("name", "波");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().likesuffix("name", "宁");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().likesuffix("name", "波");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		
		City city=jpaMybatisMapper.getById(id);
		String name=city.getName();
		city.setName("AAAAA");
		jpaMybatisMapper.update(city);
		
		
		params = Params.of().like("name", "aaa");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		params = Params.of().like_i("name", "aaa");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().likeprefix_i("name", "aaa");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().likesuffix_i("name", "aaa");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		city.setName(name);
		jpaMybatisMapper.update(city);
		
	}
	
	@Test
	public void test8hnotlike() {
		Params params = Params.of().notlike("name", "宁");
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().notlike("name", "波");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().notlikeprefix("name", "宁");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().notlikeprefix("name", "波");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().notlikesuffix("name", "宁");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().notlikesuffix("name", "波");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void test8isnull() {
		Params params = Params.of().isnull("name");
		List<City> list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		params = Params.of().isnull("age");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(0, list.size());
		
		City city=jpaMybatisMapper.getById(id);
		String name=city.getName();
		Integer age=city.getAge();
		
		params = Params.of().isnotnull("name");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().isnotnull("age");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		city.setName(null);
		city.setAge(null);
		jpaMybatisMapper.update(city);
		
		params = Params.of().isnull("name");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().isnull("age");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().isnotnull("name");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		params = Params.of().isnotnull("age");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(1, list.size());
		
		city.setName(name);
		city.setAge(age);
		jpaMybatisMapper.update(city);
		
		params = Params.of().isnotnull("name");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
		
		params = Params.of().isnotnull("age");
		list=jpaMybatisMapper.listByMap(params);
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void test9clear() {
		jpaMybatisMapper.clear();
	}
	
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
//		jpaMybatisMapper.createBatch(list);
//		//Assert.assertEquals(2, result);
//		long count=jpaMybatisMapper.countByExample(null);
//		Assert.assertEquals(2, count);
//		
//		int result=jpaMybatisMapper.removeByIds(city.getId(),city1.getId());
//		Assert.assertEquals(2, result);
//		count=jpaMybatisMapper.countByExample(null);
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
//		jpaMybatisMapper.createBatch(list);
//		//Assert.assertEquals(2, result);
//		long count=jpaMybatisMapper.countByExample(null);
//		Assert.assertEquals(2, count);
//		
//		city.setName("宁波1");
//		city1.setName("杭州1");
//		city1.setPrice(789.123);
//		jpaMybatisMapper.updateBatch(list);
//		//Assert.assertEquals(2, result);
//		
//		
//		City city3 = jpaMybatisMapper.getById(city.getId());
//		Assert.assertEquals("宁波1", city3.getName());
//		Assert.assertEquals((Double) 10.253, city3.getPrice());
//		Assert.assertEquals((Integer) 50, city3.getAge());
//		Assert.assertEquals(Sex.Man, city3.getSex());
//		Assert.assertEquals(now.getTime(), city3.getCreateDate().getTime());
//		
//		City city4=jpaMybatisMapper.getById(city1.getId());
//		Assert.assertEquals("杭州1", city4.getName());
//		Assert.assertEquals((Double)789.123, city4.getPrice());
//		Assert.assertEquals((Integer)50, city4.getAge());
//		Assert.assertEquals(Sex.Women, city4.getSex());
//		Assert.assertEquals(now.getTime(), city4.getCreateDate().getTime());
//		
//		city.setName("宁波2");
//		city1.setName("杭州2");
//		jpaMybatisMapper.updateBatchByArray(list.get(0),list.get(1));
//		
//		city3 = jpaMybatisMapper.getById(city.getId());
//		Assert.assertEquals("宁波2", city3.getName());
//		Assert.assertEquals((Double) 10.253, city3.getPrice());
//		Assert.assertEquals((Integer) 50, city3.getAge());
//		Assert.assertEquals(Sex.Man, city3.getSex());
//		Assert.assertEquals(now.getTime(), city3.getCreateDate().getTime());
//		
//		city4=jpaMybatisMapper.getById(city1.getId());
//		Assert.assertEquals("杭州2", city4.getName());
//		Assert.assertEquals((Double)789.123, city4.getPrice());
//		Assert.assertEquals((Integer)50, city4.getAge());
//		Assert.assertEquals(Sex.Women, city4.getSex());
//		Assert.assertEquals(now.getTime(), city4.getCreateDate().getTime());
//	}
	
	
	
	
	

//	/**
//	 * 测试列名大小写无关，在mybatis中测试，整合测试，同时还要测试mybatis中的自动分页功能
//	 */
//	@Test
//	public void test2() {	
//		//用于构建只获取某些字段的时候有用，返回beanmap
//		BeanMap city = jpaMybatisMapper.getMapById(id);
//		Assert.assertEquals("宁波", city.get("name"));
//		Assert.assertEquals((Double) 10.253, (Double)city.get("price"));
//		Assert.assertEquals((Integer) 50, (Integer)city.get("Age"));
//		Assert.assertEquals("Man", city.get("sex"));
//		Assert.assertEquals(now.getTime(), ((Date)city.get("createDate")).getTime());
//		
//		Params paramsUtils = Params.of().add("name", "宁波").add("sex", Sex.Man).add("createDate", now);
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
	
	//作为mybatis查询的时候的测试，因为mybatis分页查询的时候，必须要有PageInfo作为参数才可以
//	@Test
//	public void test3listPageByMap() {
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
//		jpaMybatisMapper.create(city);
//		jpaMybatisMapper.create(city1);
//		long count=jpaMybatisMapper.countByExample(null);
//		Assert.assertEquals(2, count);
//		
//		PageInfo<City> params=new PageInfo<City>();
//		params.setCountColumn("id");
//		PageInfo<City> pageinfo=jpaMybatisMapper.listPageByMap(params);
//		Assert.assertEquals(pageinfo, params);
//		Assert.assertEquals(2, params.getTotal());
//		Assert.assertEquals(2, params.getRootSize());
//
//		params=new PageInfo<City>();
//		params.setLimit(1);
//		pageinfo=jpaMybatisMapper.listPageByMap(params);
//		Assert.assertEquals(2, params.getTotal());
//		Assert.assertEquals(1, params.getRootSize());
//		
//		params=new PageInfo<City>();
//		params.addParam("name", "宁波");
//		pageinfo=jpaMybatisMapper.listPageByMap(params);
//		Assert.assertEquals(1, params.getTotal());
//		Assert.assertEquals(1, params.getRootSize());
//		
//		
//	}

}
