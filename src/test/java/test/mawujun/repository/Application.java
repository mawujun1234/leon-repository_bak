package test.mawujun.repository;


import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application  implements CommandLineRunner {
	@Autowired
	private CityMapper cityMapper;


	public static void main(String[] args) throws SQLException {
		//ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
	    .web(WebApplicationType.NONE)
	    .run(args);
		
		//JdbcTemplate jdbctemplate=context.getBean(JdbcTemplate.class;)
		DataSource dataSource=context.getBean(DataSource.class);
		System.out.println(dataSource.getConnection());
				
				
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("===================开始使用mybatis进行查询了:");
		//System.out.println("mybatis:"+this.cityMapper.get("1111"));
		City city=new City();
		city.setName("宁波");
		city.setPrice(10.253);
		city.setAge(50);
		city.setSex(Sex.Man);
		city.setCreateDate(new Date());

	}
}
