package test.mawujun.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mawujun.generator.code.EntityTable;
import com.mawujun.generator.code.IDGenEnum;
import com.mawujun.generator.code.JavaEntityMetadataService;
import com.mawujun.generator.code.PropertyColumn;

import test.mawujun.model.City;
import test.mawujun.model.City2;
import test.mawujun.model.CoplxId1;
import test.mawujun.model.CoplxId1Entity;
import test.mawujun.model.CoplxId2;
import test.mawujun.model.CoplxId2Entity;



public class JavaEntityMetaDataServiceTest {
	JavaEntityMetadataService javaEntityMetaDataService=new JavaEntityMetadataService();
	//id属性的情况没有搜集
	//枚举类型没有判断
	@Test
	public void test1 (){
		//System.out.println(Sex.class.isEnum());
		EntityTable root=javaEntityMetaDataService.getClassProperty(City.class);
		Assert.assertEquals("t_city", root.getEntityTableName());
		Assert.assertEquals("City", root.getEntitySimpleClassName());
		Assert.assertEquals("city", root.getUncapitalizeSimpleClassName());
		Assert.assertEquals("test.mawujun.model.City", root.getEntityClassName());
		Assert.assertEquals("city", root.getAlias());
		Assert.assertEquals("test.mawujun", root.getBasepackage());
		//Assert.assertEquals("", root.getIdType());
		
		
		//测试属性，列名注解
		List<PropertyColumn> lpc=root.getPropertyColumns();
		Assert.assertEquals(6, lpc.size());
		PropertyColumn id=root.getPropertyColumn("id");
		assertNotNull(id);
		assertEquals("id", id.getProperty());
		assertEquals("id", id.getColumn());
		assertEquals("id", id.getLabel());
		assertEquals("主键id", id.getComment());
		assertEquals(null, id.getDefaultValue());
		assertEquals(new Integer(36), id.getLength());
		assertEquals(null, id.getPrecision());
		assertEquals(null, id.getScale());
		assertEquals(true, id.isUnique());
		assertEquals(false, id.isNullable());
		assertEquals(true, id.isInsertable());
		assertEquals(false, id.isUpdatable());
		assertEquals(false, id.getIsEnum());
		assertEquals("java.lang", id.getBasepackage());
		assertEquals("java.lang.String", id.getClassName());
		assertEquals("String", id.getSimpleClassName());
		assertEquals(true,id.getIsId());
		assertEquals(IDGenEnum.uuid,id.getIdGenEnum());
		
		assertEquals(String.class,root.getIdClass());
		assertEquals("id", root.getIdPropertys()[0]);
		assertEquals("id", root.getIdColumns()[0]);
		assertEquals(null, root.getIdSequenceName());

		
		PropertyColumn name=root.getPropertyColumn("name");
		assertNotNull(name);
		assertEquals("name", name.getProperty());
		assertEquals("name", name.getColumn());
		assertEquals("名称", name.getLabel());
		assertEquals("名称注释", name.getComment());
		assertEquals(null, name.getDefaultValue());
		assertEquals(new Integer(30), name.getLength());
		assertEquals(null, name.getPrecision());
		assertEquals(null, name.getScale());
		assertEquals(false, name.isUnique());
		assertEquals(true, name.isNullable());
		assertEquals(true, name.isInsertable());
		assertEquals(true, name.isUpdatable());
		assertEquals(false, name.getIsEnum());
		assertEquals("java.lang", name.getBasepackage());
		assertEquals("java.lang.String", name.getClassName());
		assertEquals("String", name.getSimpleClassName());
		assertEquals(false,name.getIsId());
		assertEquals(IDGenEnum.none,name.getIdGenEnum());
		
		
		PropertyColumn age=root.getPropertyColumn("age");
		assertNotNull(age);
		assertEquals("age", age.getProperty());
		assertEquals("age", age.getColumn());
		assertEquals("age", age.getLabel());
		assertEquals("年龄", age.getComment());
		assertEquals("18", age.getDefaultValue());
		assertEquals(new Integer(30), name.getLength());
		assertEquals(null, name.getPrecision());
		assertEquals(null, name.getScale());
		assertEquals(false, age.isUnique());
		assertEquals(true, age.isNullable());
		assertEquals(true, age.isInsertable());
		assertEquals(true, age.isUpdatable());
		assertEquals(false, age.getIsEnum());
		assertEquals("java.lang", age.getBasepackage());
		assertEquals("java.lang.Integer", age.getClassName());
		assertEquals("Integer", age.getSimpleClassName());
		assertEquals(false,age.getIsId());
		assertEquals(IDGenEnum.none,age.getIdGenEnum());
		


		PropertyColumn price=root.getPropertyColumn("price");
		assertNotNull(price);
		assertEquals("price", price.getProperty());
		assertEquals("price", price.getColumn());
		assertEquals("price", price.getLabel());
		assertEquals("价格", price.getComment());
		assertEquals("0.0", price.getDefaultValue());
		assertEquals(new Integer(255), price.getLength());
		assertEquals(new Integer(10), price.getPrecision());
		assertEquals(new Integer(2), price.getScale());
		assertEquals(false, price.isUnique());
		assertEquals(true, price.isNullable());
		assertEquals(true, price.isInsertable());
		assertEquals(true, price.isUpdatable());
		assertEquals(false, price.getIsEnum());
		assertEquals("java.lang", price.getBasepackage());
		assertEquals("java.lang.Double", price.getClassName());
		assertEquals("Double", price.getSimpleClassName());
		assertEquals(false,price.getIsId());
		assertEquals(IDGenEnum.none,price.getIdGenEnum());
		
		
		PropertyColumn createDate=root.getPropertyColumn("createDate");
		assertNotNull(createDate);
		assertEquals("createDate", createDate.getProperty());
		assertEquals("create_date", createDate.getColumn());
		assertEquals("createDate", createDate.getLabel());
		assertEquals("创建时间", createDate.getComment());
		assertEquals(null, createDate.getDefaultValue());
		assertEquals(new Integer(255), createDate.getLength());
		assertEquals(null, createDate.getPrecision());
		assertEquals(null, createDate.getScale());
		assertEquals(false, createDate.isUnique());
		assertEquals(true, createDate.isNullable());
		assertEquals(true, createDate.isInsertable());
		assertEquals(true, createDate.isUpdatable());
		assertEquals(false, createDate.getIsEnum());
		assertEquals("java.util", createDate.getBasepackage());
		assertEquals("java.util.Date", createDate.getClassName());
		assertEquals("Date", createDate.getSimpleClassName());
		assertEquals(false,createDate.getIsId());
		assertEquals(IDGenEnum.none,createDate.getIdGenEnum());
		
		PropertyColumn sex=root.getPropertyColumn("sex");
		assertNotNull(sex);
		assertEquals("sex", sex.getProperty());
		assertEquals("sex", sex.getColumn());
		assertEquals("sex", sex.getLabel());
		assertEquals("性别", sex.getComment());
		assertEquals("Man", sex.getDefaultValue());
		assertEquals(new Integer(10), sex.getLength());
		assertEquals(null, sex.getPrecision());
		assertEquals(null, sex.getScale());
		assertEquals(false, sex.isUnique());
		assertEquals(true, sex.isNullable());
		assertEquals(true, sex.isInsertable());
		assertEquals(true, sex.isUpdatable());
		assertEquals(true, sex.getIsEnum());
		assertEquals("test.mawujun.model", sex.getBasepackage());
		assertEquals("test.mawujun.model.Sex", sex.getClassName());
		assertEquals("Sex", sex.getSimpleClassName());
		assertEquals(false,sex.getIsId());
		assertEquals(IDGenEnum.none,sex.getIdGenEnum());
		
		
	}
	
	@Test
	public void test2 (){
		EntityTable root=javaEntityMetaDataService.getClassProperty(City2.class);
		Assert.assertEquals("t_city2", root.getEntityTableName());
		Assert.assertEquals("City2", root.getEntitySimpleClassName());
		Assert.assertEquals("city2", root.getUncapitalizeSimpleClassName());
		Assert.assertEquals("test.mawujun.model.City2", root.getEntityClassName());
		Assert.assertEquals("city2", root.getAlias());
		Assert.assertEquals("test.mawujun", root.getBasepackage());
		//Assert.assertEquals("", root.getIdType());
		
		
		//测试属性，列名注解
		List<PropertyColumn> lpc=root.getPropertyColumns();
		Assert.assertEquals(6, lpc.size());
		PropertyColumn id=root.getPropertyColumn("id");
		assertNotNull(id);
		assertEquals("id", id.getProperty());
		assertEquals("id", id.getColumn());
		assertEquals("id", id.getLabel());
		assertEquals(null, id.getComment());
		assertEquals(null, id.getDefaultValue());
		assertEquals(new Integer(36), id.getLength());
		assertEquals(null, id.getPrecision());
		assertEquals(null, id.getScale());
		assertEquals(true, id.isUnique());
		assertEquals(false, id.isNullable());
		assertEquals(true, id.isInsertable());
		assertEquals(false, id.isUpdatable());
		assertEquals(false, id.getIsEnum());
		assertEquals("java.lang", id.getBasepackage());
		assertEquals("java.lang.String", id.getClassName());
		assertEquals("String", id.getSimpleClassName());
		assertEquals(true,id.getIsId());
		assertEquals(IDGenEnum.uuid,id.getIdGenEnum());
		
		assertEquals(String.class,root.getIdClass());
		assertEquals("id", root.getIdPropertys()[0]);
		assertEquals("id", root.getIdColumns()[0]);
		assertEquals(null, root.getIdSequenceName());

		
		PropertyColumn name=root.getPropertyColumn("name");
		assertNotNull(name);
		assertEquals("name", name.getProperty());
		assertEquals("name", name.getColumn());
		assertEquals("name", name.getLabel());
		assertEquals(null, name.getComment());
		assertEquals(null, name.getDefaultValue());
		assertEquals(new Integer(255), name.getLength());
		assertEquals(null, name.getPrecision());
		assertEquals(null, name.getScale());
		assertEquals(false, name.isUnique());
		assertEquals(true, name.isNullable());
		assertEquals(true, name.isInsertable());
		assertEquals(true, name.isUpdatable());
		assertEquals(false, name.getIsEnum());
		assertEquals("java.lang", name.getBasepackage());
		assertEquals("java.lang.String", name.getClassName());
		assertEquals("String", name.getSimpleClassName());
		assertEquals(false,name.getIsId());
		assertEquals(IDGenEnum.none,name.getIdGenEnum());
		


		PropertyColumn price=root.getPropertyColumn("price");
		assertNotNull(price);
		assertEquals("price", price.getProperty());
		assertEquals("price", price.getColumn());
		assertEquals("price", price.getLabel());
		assertEquals(null, price.getComment());
		assertEquals(null, price.getDefaultValue());
		assertEquals(new Integer(255), price.getLength());
		assertEquals(null, price.getPrecision());
		assertEquals(null, price.getScale());
		assertEquals(false, price.isUnique());
		assertEquals(true, price.isNullable());
		assertEquals(true, price.isInsertable());
		assertEquals(true, price.isUpdatable());
		assertEquals(false, price.getIsEnum());
		assertEquals("java.lang", price.getBasepackage());
		assertEquals("java.lang.Double", price.getClassName());
		assertEquals("Double", price.getSimpleClassName());
		assertEquals(false,price.getIsId());
		assertEquals(IDGenEnum.none,price.getIdGenEnum());
		
		PropertyColumn createDate=root.getPropertyColumn("createDate");
		assertNotNull(createDate);
		assertEquals("createDate", createDate.getProperty());
		assertEquals("create_date", createDate.getColumn());
		assertEquals("createDate", createDate.getLabel());
		assertEquals(null, createDate.getComment());
		assertEquals(null, createDate.getDefaultValue());
		assertEquals(new Integer(255), createDate.getLength());
		assertEquals(null, createDate.getPrecision());
		assertEquals(null, createDate.getScale());
		assertEquals(false, createDate.isUnique());
		assertEquals(true, createDate.isNullable());
		assertEquals(true, createDate.isInsertable());
		assertEquals(true, createDate.isUpdatable());
		assertEquals(false, createDate.getIsEnum());
		assertEquals("java.util", createDate.getBasepackage());
		assertEquals("java.util.Date", createDate.getClassName());
		assertEquals("Date", createDate.getSimpleClassName());
		assertEquals(false,createDate.getIsId());
		assertEquals(IDGenEnum.none,createDate.getIdGenEnum());
		
		PropertyColumn sex=root.getPropertyColumn("sex");
		assertNotNull(sex);
		assertEquals("sex", sex.getProperty());
		assertEquals("sex", sex.getColumn());
		assertEquals("sex", sex.getLabel());
		assertEquals(null, sex.getComment());
		assertEquals(null, sex.getDefaultValue());
		assertEquals(new Integer(10), sex.getLength());
		assertEquals(null, sex.getPrecision());
		assertEquals(null, sex.getScale());
		assertEquals(false, sex.isUnique());
		assertEquals(true, sex.isNullable());
		assertEquals(true, sex.isInsertable());
		assertEquals(true, sex.isUpdatable());
		assertEquals(true, sex.getIsEnum());
		assertEquals("test.mawujun.model", sex.getBasepackage());
		assertEquals("test.mawujun.model.Sex", sex.getClassName());
		assertEquals("Sex", sex.getSimpleClassName());
		assertEquals(false,sex.getIsId());
		assertEquals(IDGenEnum.none,sex.getIdGenEnum());
	}
	/**
	 * 测试复核主键
	 */
	@Test
	public void test3 (){
		//System.out.println(Sex.class.isEnum());
		EntityTable root=javaEntityMetaDataService.getClassProperty(CoplxId1Entity.class);
		Assert.assertEquals("t_coplxid1entity", root.getEntityTableName());
		Assert.assertEquals("CoplxId1Entity", root.getEntitySimpleClassName());
		Assert.assertEquals("coplxId1Entity", root.getUncapitalizeSimpleClassName());
		Assert.assertEquals("test.mawujun.model.CoplxId1Entity", root.getEntityClassName());
		Assert.assertEquals("coplxId1Entity", root.getAlias());
		Assert.assertEquals("test.mawujun", root.getBasepackage());
		
		PropertyColumn name=root.getPropertyColumn("name");
		assertNotNull(name);
		assertEquals("name", name.getProperty());
		assertEquals("name", name.getColumn());
		assertEquals("name", name.getLabel());
		assertEquals(null, name.getComment());
		assertEquals(null, name.getDefaultValue());
		assertEquals(new Integer(255), name.getLength());
		assertEquals(null, name.getPrecision());
		assertEquals(null, name.getScale());
		assertEquals(false, name.isUnique());
		assertEquals(true, name.isNullable());
		assertEquals(true, name.isInsertable());
		assertEquals(true, name.isUpdatable());
		assertEquals(false, name.getIsEnum());
		assertEquals("java.lang", name.getBasepackage());
		assertEquals("java.lang.String", name.getClassName());
		assertEquals("String", name.getSimpleClassName());
		assertEquals(false,name.getIsId());
		assertEquals(IDGenEnum.none,name.getIdGenEnum());
		
		PropertyColumn id=root.getPropertyColumn("id");
		assertNotNull(id);
		assertEquals(true, id.isCompositeId());
		assertEquals(true, id.getIsId());
		assertEquals("id", id.getProperty());
		assertEquals("id", id.getColumn());
		assertEquals("test.mawujun.model.CoplxId1", id.getClassName());
		assertEquals(CoplxId1.class, id.getClazz());
		
		//从root中获取复合主键信息
		assertEquals(CoplxId1.class,root.getIdClass());
		assertEquals(2,root.getIdPropertys().length);
		assertEquals(2,root.getIdColumns().length);
		assertEquals(true, root.getIsCompositeId());
		assertEquals("id1", root.getIdPropertys()[0]);
		assertEquals("id2", root.getIdPropertys()[1]);
		assertEquals("id1", root.getIdColumns()[0]);
		assertEquals("id2", root.getIdColumns()[1]);
		assertEquals(IDGenEnum.none, root.getIdGenEnum());
		assertEquals(null, root.getIdSequenceName());
		
	}
	
	
	@Test
	public void test4 (){
		//System.out.println(Sex.class.isEnum());
		EntityTable root=javaEntityMetaDataService.getClassProperty(CoplxId2Entity.class);
		Assert.assertEquals("t_coplxid2entity", root.getEntityTableName());
		Assert.assertEquals("CoplxId2Entity", root.getEntitySimpleClassName());
		Assert.assertEquals("coplxId2Entity", root.getUncapitalizeSimpleClassName());
		Assert.assertEquals("test.mawujun.model.CoplxId2Entity", root.getEntityClassName());
		Assert.assertEquals("test.mawujun.model.CoplxId2Entity", root.getAlias());
		Assert.assertEquals("test.mawujun", root.getBasepackage());
		
		PropertyColumn name=root.getPropertyColumn("name");
		assertNotNull(name);
		assertEquals("name", name.getProperty());
		assertEquals("name", name.getColumn());
		assertEquals("name", name.getLabel());
		assertEquals(null, name.getComment());
		assertEquals(null, name.getDefaultValue());
		assertEquals(new Integer(255), name.getLength());
		assertEquals(null, name.getPrecision());
		assertEquals(null, name.getScale());
		assertEquals(false, name.isUnique());
		assertEquals(true, name.isNullable());
		assertEquals(true, name.isInsertable());
		assertEquals(true, name.isUpdatable());
		assertEquals(false, name.getIsEnum());
		assertEquals("java.lang", name.getBasepackage());
		assertEquals("java.lang.String", name.getClassName());
		assertEquals("String", name.getSimpleClassName());
		assertEquals(false,name.getIsId());
		assertEquals(IDGenEnum.none,name.getIdGenEnum());
		
		PropertyColumn id1=root.getPropertyColumn("id1");
		assertNotNull(id1);
		assertEquals(true, id1.isCompositeId());
		assertEquals(true, id1.getIsId());
		assertEquals("id1", id1.getProperty());
		assertEquals("id1", id1.getColumn());
		assertEquals("java.lang.String", id1.getClassName());
		assertEquals(String.class, id1.getClazz());
		
		PropertyColumn id2=root.getPropertyColumn("id2");
		assertNotNull(id2);
		assertEquals(true, id2.isCompositeId());
		assertEquals(true, id2.getIsId());
		assertEquals("id2", id2.getProperty());
		assertEquals("id2", id2.getColumn());
		assertEquals("java.lang.String", id2.getClassName());
		assertEquals(String.class, id2.getClazz());
		
		//从root中获取复合主键信息
		assertEquals(CoplxId2.class,root.getIdClass());
		assertEquals(2,root.getIdPropertys().length);
		assertEquals(2,root.getIdColumns().length);
		assertEquals(true, root.getIsCompositeId());
		assertEquals("id1", root.getIdPropertys()[0]);
		assertEquals("id2", root.getIdPropertys()[1]);
		assertEquals("id1", root.getIdColumns()[0]);
		assertEquals("id2", root.getIdColumns()[1]);
		assertEquals(IDGenEnum.none, root.getIdGenEnum());
		assertEquals(null, root.getIdSequenceName());
		
	}

}
