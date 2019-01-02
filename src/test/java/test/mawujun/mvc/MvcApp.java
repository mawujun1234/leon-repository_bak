package test.mawujun.mvc;


import java.sql.SQLException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("test.mawujun.model")
@ComponentScan(basePackages= {"com.mawujun","test.mawujun.mvc"})//"com.mawujun",在测试时，这个不能去掉，不能加载自己的spring.factories
//@MapperScan("test.mawujun.jpa")
public class MvcApp  implements CommandLineRunner {
//	@Autowired
//	private CityMapper cityMapper;


	public static void main(String[] args) throws SQLException {
		//ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		ConfigurableApplicationContext context = new SpringApplicationBuilder(MvcApp.class).run(args);

	}
	



	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("===================开始使用mybatis进行查询了:");
		//System.out.println("mybatis:"+this.cityMapper.get("1111"));
//		City city=new City();
//		city.setName("宁波");
//		city.setPrice(10.253);
//		city.setAge(50);
//		city.setSex(Sex.Man);
//		city.setCreateDate(new Date());
//		int result=cityMapper.create(city);
//		System.out.println(result);
//		System.out.println(city.getId());
		
	}
}
