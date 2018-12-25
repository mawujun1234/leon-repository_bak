package test.mawujun.generator;


import java.sql.SQLException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("aaaaa")
@ComponentScan(basePackages= {"aaaaa"})
@MapperScan(basePackages= {"aaaaa"})
public class TestApp  implements CommandLineRunner {
//	@Autowired
//	private CityMapper cityMapper;


	public static void main(String[] args) throws SQLException {
		//ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		ConfigurableApplicationContext context = new SpringApplicationBuilder(TestApp.class)
	    .web(WebApplicationType.NONE)
	    .run(args);
//		//JdbcTemplate jdbctemplate=context.getBean(JdbcTemplate.class;)
//		DataSource dataSource=context.getBean(DataSource.class);
//		System.out.println(dataSource.getConnection());
//		
//		SqlSessionFactory  sqlSessionFactory=context.getBean(SqlSessionFactory.class);
//		//首先获取Configuration，等初始化完后，然后反射
//		//替换MapperProxyFactory
//		//然后再替换MapperProxy
//		Configuration conf=sqlSessionFactory.getConfiguration();
//		Collection<Class<?>> list=conf.getMapperRegistry().getMappers();
//		System.out.println(list);
//		//MetaObject.forObject(object, objectFactory, objectWrapperFactory, reflectorFactory)
//		Map<Class<?>, MapperProxyFactory<?>> knownMappers=(Map<Class<?>, MapperProxyFactory<?>>)ReflectUtils.getFieldValue(conf.getMapperRegistry(), "knownMappers");
//		
//		for(Entry<Class<?>,MapperProxyFactory<?>> entry:knownMappers.entrySet()) {
//			knownMappers.put(entry.getKey(), new NewMapperProxyFactory(entry.getValue().getMapperInterface()));
//		}
//	
//		CityMapper cityMapper=context.getBean(CityMapper.class);
//		System.out.println(cityMapper.getById("111"));
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
