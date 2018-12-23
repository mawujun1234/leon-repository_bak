package com.mawujun.generator.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class EntityTable {
	//private String dbName;
	private Class entityClass;
	private String entityTableName;//表名
	private String entitySimpleClassName;//类名，不带包名的
	private String entitySimpleClassNameUncap;//首字母小写
	private String entityClassName;//全限定类名
	private String entityPackage;//实体类所在的包名
	
	private String comment;
	private String alias;//别名，主要用于mybatis。如果指定胃，就使用指定胡，否则就使用类名，并且全部小写,但是如果没有设置@Alias注解的话，就使用全限定类名
	
	private String basepackage;//基础包名。生成的类都放在这个基础包下面
	//private String idType;
	//private String idColumnName;
	//private String idPropertyName;
	//private String idGeneratorStrategy="";
	//private String sequenceName;
	//private boolean hasResultMap;//是组件关联的时候
	
	private IDGenEnum idGenEnum=IDGenEnum.none;
	private Class<?> idClass;
	private String idClassName;//自动
	private String idSimpleClassName;//自动
	private boolean isCompositeId=false;//判断是否是复合主键
	private String[] idColumns;
	private String[] idPropertys;
	private String idSequenceName;//序列化的时候的名字,如oralce、DB、SAP DB、PostgerSQL、McKoi中的sequence。MySQL这种不支持sequence的数据库则不行（可以使用identity）。

	List<PropertyColumn> propertyColumns=new ArrayList<PropertyColumn>();
	Map<String,PropertyColumn> propertyColumns_map=new HashMap<String,PropertyColumn>();
	//List<PropertyColumn> baseTypePropertyColumns=new ArrayList<PropertyColumn>();
	//存放需要产生查询条件的属性
	List<PropertyColumn> queryProperties =new ArrayList<PropertyColumn>();
	/**
	 * 会自动设置
	 * entitySimpleClassName，entitySimpleClassNameUncap，entityClassName，entityPackage
	 * 
	 * @param entityClass
	 */
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
		this.entitySimpleClassName = entityClass.getSimpleName();
		this.entitySimpleClassNameUncap=StringUtils.uncapitalize(this.getEntitySimpleClassName());
		this.entityClassName=entityClass.getName();
		this.entityPackage=entityClass.getPackage().getName();
		
	}
	/**
	 * 把tablename转换成classname,如果是复合主键，再转换一下
	 * @param entityClassName
	 */
	public void convertTableToClass(String basepackage,String entitySimpleClassNameUncap) {
		this.basepackage=basepackage;
		this.entitySimpleClassNameUncap=entitySimpleClassNameUncap;
		this.alias=entitySimpleClassNameUncap;
		this.entitySimpleClassName=StringUtils.capitalize(entitySimpleClassNameUncap);
		this.entityPackage=basepackage+".model";
		this.entityClassName=this.entityPackage+"."+entitySimpleClassName;
		this.entityClass =null;//还没有生成，不能用
		
		//还需要生成复合主键
		if(this.getIsCompositeId()) {
			String idClassName=this.getEntityClassName()+"Id";
			String idSimpleClassName=this.getEntitySimpleClassName()+"Id";
			//Class idClass=;
			this.idClassName=idClassName;
			this.idSimpleClassName=idSimpleClassName;
		}
		
	}
	
	public void setSimpleClassName(String simpleClassName) {
		this.entitySimpleClassName = simpleClassName;
		this.entitySimpleClassNameUncap=StringUtils.uncapitalize(this.getEntitySimpleClassName());;
	}
	
	public void setIdClass(Class<?> idClass) {
		this.idClass = idClass;
		this.idSimpleClassName=idClass.getSimpleName();
		this.idClassName=idClass.getName();
	}

	
	public void addIdColumn(String idColumn) {
		if(this.idColumns==null) {
			this.idColumns=new String[]{idColumn};
			return;
		}
		this.idColumns=ArrayUtils.add(this.idColumns, idColumn);
	}
	public void addIdProperty(String idProperty) {
		if(this.idPropertys==null) {
			this.idPropertys=new String[]{idProperty};
			return;
		}
		this.idPropertys=ArrayUtils.add(this.idPropertys, idProperty);
	}
	
	public PropertyColumn getPropertyColumn(String property) {
		return this.propertyColumns_map.get(property);
	}
	
	public void addPropertyColumn(PropertyColumn pc) {
		this.propertyColumns.add(pc);
		this.propertyColumns_map.put(pc.getProperty(), pc);
	}
	
	public String getUncapitalizeSimpleClassName() {
		return this.entitySimpleClassNameUncap;
	}

	public boolean getIsCompositeId() {
		return isCompositeId;
	}

	public void setIsCompositeId(boolean isIcompositeId) {
		this.isCompositeId = isIcompositeId;
	}

	public String[] getIdColumns() {
		return idColumns;
	}

	public void setIdColumns(String[] idColumns) {
		this.idColumns = idColumns;
	}

	public String[] getIdPropertys() {
		return idPropertys;
	}

	public void setIdPropertys(String[] idPropertys) {
		this.idPropertys = idPropertys;
	}



//	public void setClassName(String className) {
//		this.className = className;
//	}



	public String getBasepackage() {
		return basepackage;
	}

	public void setBasepackage(String basepackage) {
		this.basepackage = basepackage;
	}

	public List<PropertyColumn> getPropertyColumns() {
		return propertyColumns;
	}

	public void setPropertyColumns(List<PropertyColumn> propertyColumns) {
		this.propertyColumns = propertyColumns;
	}


	public List<PropertyColumn> getQueryProperties() {
		return queryProperties;
	}

	public void setQueryProperties(List<PropertyColumn> queryProperties) {
		this.queryProperties = queryProperties;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public IDGenEnum getIdGenEnum() {
		return idGenEnum;
	}

	public void setIdGenEnum(IDGenEnum idGenEnum) {
		this.idGenEnum = idGenEnum;
	}
	

	public String getIdClassName() {
		return idClassName;
	}

	public String getIdSimpleClassName() {
		return idSimpleClassName;
	}

	public Map<String, PropertyColumn> getPropertyColumns_map() {
		return propertyColumns_map;
	}

	public void setPropertyColumns_map(Map<String, PropertyColumn> propertyColumns_map) {
		this.propertyColumns_map = propertyColumns_map;
	}


	public String getIdSequenceName() {
		return idSequenceName;
	}

	public void setIdSequenceName(String idSequenceName) {
		this.idSequenceName = idSequenceName;
	}

	

	public Class<?> getIdClass() {
		return idClass;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	

	public String getEntityPackage() {
		return entityPackage;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	public void setIdClassName(String idClassName) {
		this.idClassName = idClassName;
	}

	public void setIdSimpleClassName(String idSimpleClassName) {
		this.idSimpleClassName = idSimpleClassName;
	}

	public void setCompositeId(boolean isCompositeId) {
		this.isCompositeId = isCompositeId;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public String getEntitySimpleClassName() {
		return entitySimpleClassName;
	}

	public String getEntitySimpleClassNameUncap() {
		return entitySimpleClassNameUncap;
	}

	public String getEntityTableName() {
		return entityTableName;
	}

	public void setEntityTableName(String entityTableName) {
		this.entityTableName = entityTableName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}



	




}
