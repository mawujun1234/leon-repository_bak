package test.mawujun.mvc;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MvcApp.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Transactional
//@Rollback(false)
//@ActiveProfiles("sqlserver")//h2,hsqldb,derby,sqlserver,mysql,oracle,db2,postgresql
//@ActiveProfiles("h2")
public class MvcControllerTest {
	@LocalServerPort
	private int port;
	// 注入spring容器
	@Autowired
	private WebApplicationContext wac;
	// 实现了对http请求的模拟
	private MockMvc mvc;

	private String url;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new MvcController()).build();
		// mvc= MockMvcBuilders.webAppContextSetup(wac) .build();

		url = String.format("http://localhost:%d/", port);
	}
	


//	@Test
//	public void testException() throws Exception {
//		String json = "{\"author\":\"HAHAHAA\",\"title\":\"Spring\",\"url\":\"http://tengj.top/\"}";
//		mvc.perform(MockMvcRequestBuilders.post("/throwBusinessException")
//		// //传json参数
//		.accept(MediaType.APPLICATION_JSON_UTF8).content(json.getBytes()))
//		.andExpect(MockMvcResultMatchers.handler().handlerType(MvcController.class))
//        .andExpect(MockMvcResultMatchers.handler().methodName(("test")))
//		.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
//
//	}
//	
//	@Test
//	public void testException1() throws Exception {
//		//String json = "{\"author\":\"HAHAHAA\",\"title\":\"Spring\",\"url\":\"http://tengj.top/\"}";
//		mvc.perform(MockMvcRequestBuilders.post("/throwException").accept(MediaType.APPLICATION_JSON_UTF8)
//				//.content(json.getBytes())// //传json参数
//		)
//		.andExpect(MockMvcResultMatchers.handler().handlerType(MvcController.class))
//        .andExpect(MockMvcResultMatchers.handler().methodName(("test")))
//		.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
//
//	}

}
