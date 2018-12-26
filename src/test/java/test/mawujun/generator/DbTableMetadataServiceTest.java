package test.mawujun.generator;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import aaaaa.TestId;
import aaaaa.TestRepository;



@RunWith(SpringRunner.class)
@SpringBootTest(classes={TestApp.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
@Rollback(false)
//@ActiveProfiles("sqlserver")//h2,hsqldb,derby,sqlserver,mysql,oracle,db2,postgresql
@ActiveProfiles("h2")
public class DbTableMetadataServiceTest {
	@Autowired
	TestRepository testRepository;
	
//	//加载建表sql，
//	//再指定位置生成java代码
//	//测试各种功能，看看能不能完成测试
//	@Test
//	public void test1() {
//		DbTableMetadataService service=new DbTableMetadataService();
//		 List<EntityTable> list=service.getTablesInfo();
//		 for(EntityTable et:list) {
//			 System.out.println(et.getEntityTableName());
//			 List<PropertyColumn> columns=et.getPropertyColumns();
//			 for(PropertyColumn col:columns) {
//				 System.out.println(col);
//			 }
//		 }
//		
//	}
	
	
	@Test
	public void test() {
		TestId testId=new TestId();
		testId.setId1("1");
		testId.setId2("2");
		
		aaaaa.Test test=new aaaaa.Test();
		test.setId1("1");
		test.setId2("2");
		test.setName("测试");
		
		testRepository.create(test);
		//Assert.notNull(test.get);
		
		aaaaa.Test tes1t=testRepository.getById(testId);
		assertEquals("测试", tes1t.getName());
		
	}

}
