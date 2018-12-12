package test.mawujun.jpa.service;

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

import test.mawujun.jpa.JpaMybatisApp;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={JpaMybatisApp.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
@Rollback(false)
//@ActiveProfiles("sqlserver")//h2,hsqldb,derby,sqlserver,mysql,oracle,db2,postgresql
@ActiveProfiles("h2")
public class JpaMybatisServiceTest {
	@Autowired
	private JpaMybatisService jpaMybatisService;
	
	@Test
	public void test() {
		jpaMybatisService.count();
	}

}
