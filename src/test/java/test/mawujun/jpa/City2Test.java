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

import com.mawujun.repository.utils.Params;

import test.mawujun.model.City2;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { JpaMybatisApp.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
@Rollback(false)
@ActiveProfiles("h2")
public class City2Test {
	@Autowired
	private City2Mapper city2Mapper;

	private static Long id;

	@Test
	public void test() {
		City2 city2 = new City2();
		city2.setName("测试");
		city2Mapper.create(city2);
		Assert.assertNotNull(city2.getId());
		id = city2.getId();
	}

	// 测试完后，移动到City的测试类上
	@Test
	public void test1_logicDelete() {
		City2 city2 = city2Mapper.getById(id);
		Assert.assertNotNull(city2);
		Assert.assertEquals(new Integer(0), city2.getDeleted());
		city2.setDeleted(null);
		City2 city2_ = city2Mapper.update(city2);
		Assert.assertEquals(new Integer(0), city2_.getDeleted());

		City2 city23 = new City2();
		city23.setId(id);
		city23.setName("测试");
		// 这种情况及Ubuntu是同个对象
		City2 city23_ = city2Mapper.update(city23);
		Assert.assertEquals(new Integer(0), city23_.getDeleted());

		int result=city2Mapper.remove(city2);
		Assert.assertEquals(1, result);
		City2 city22 = city2Mapper.getById(id);
		Assert.assertNotNull(city22);
		Assert.assertEquals(new Integer(1), city22.getDeleted());

		// 当更新的时候，还是可把逻辑删除更新为没有删除的，只要把deleted设为正确的值就可以了
		city22.setDeleted(0);
		city2Mapper.update(city22);
		city22 = city2Mapper.getById(id);
		Assert.assertNotNull(city22);
		Assert.assertEquals(new Integer(0), city22.getDeleted());
		
		result=city2Mapper.removeAll();
		Assert.assertEquals(1, result);
		city22 = city2Mapper.getById(id);
		Assert.assertNotNull(city22);
		Assert.assertEquals(new Integer(1), city22.getDeleted());
		
		city22.setDeleted(0);
		city2Mapper.update(city22);
		city22 = city2Mapper.getById(id);
		Assert.assertEquals(new Integer(0), city22.getDeleted());
		result=city2Mapper.removeById(id);
		Assert.assertEquals(1, result);
		city22 = city2Mapper.getById(id);
		Assert.assertNotNull(city22);
		Assert.assertEquals(new Integer(1), city22.getDeleted());
		
		
		city22.setDeleted(0);
		city2Mapper.update(city22);
		city22 = city2Mapper.getById(id);
		Assert.assertEquals(new Integer(0), city22.getDeleted());
		result=city2Mapper.removeByMap(Params.of("name", "测试"));
		Assert.assertEquals(1, result);
		city22 = city2Mapper.getById(id);
		Assert.assertNotNull(city22);
		Assert.assertEquals(new Integer(1), city22.getDeleted());
		
		city22.setDeleted(0);
		city2Mapper.update(city22);
		city22 = city2Mapper.getById(id);
		Assert.assertEquals(new Integer(0), city22.getDeleted());
		result=city2Mapper.removeByIds(id,111L);
		Assert.assertEquals(1, result);
		city22 = city2Mapper.getById(id);
		Assert.assertNotNull(city22);
		Assert.assertEquals(new Integer(1), city22.getDeleted());


		

	}
	
	@Test
	public void test2_forceDelete() {
		//测试删除detacted的对象，会不会爆出异常，原生的话是会爆出异常的额
		City2 deleteForce = new City2();
		deleteForce.setId(1111L);
		int result=city2Mapper.removeForce(deleteForce);
		Assert.assertEquals(0, result);
		Assert.assertNull(city2Mapper.getById(1111L));
		
		result=city2Mapper.removeForce(city2Mapper.getById(id));
		Assert.assertEquals(1, result);
		Assert.assertNull(city2Mapper.getById(id));
		
		//按id强制删除，入股id存在，如果id不存在
		result=city2Mapper.removeForceById(id);
		Assert.assertEquals(0, result);
		Assert.assertNull(city2Mapper.getById(id));
		
		test();
		result=city2Mapper.removeForceById(id);
		Assert.assertEquals(1, result);
		Assert.assertNull(city2Mapper.getById(id));
		
		test();
		result=city2Mapper.removeForceAll();
		Assert.assertEquals(1, result);
		Assert.assertNull(city2Mapper.getById(id));
		
		test();
		result=city2Mapper.removeForceByMap(Params.of("name", "测试"));
		Assert.assertEquals(1, result);
		Assert.assertNull(city2Mapper.getById(id));
	}

}
