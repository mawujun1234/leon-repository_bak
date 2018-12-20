package com.mawujun.generator.code;

public class PropertyColumn {
	private String column;//列名
	private String columnType;//列的类型 如varcahr(20),主要用在逆向生成的时候
	
	private String property;//属性名称
	
	private String label;//列的中文名，如果没有设置，就使用column
	private String comment;//注释
	private String defaultValue;//默认值
	
	private int length=255;//列的长度
	private int precision=0;
	private int scale=0;
	
	private boolean unique=false;
	private boolean nullable=true;
	private boolean insertable=true;
	private boolean updatable=true;
	
//	public boolean hidden=false;//是否是隐藏字段
//	private Boolean nullable=true;//true表示可以为空
//	private Integer sort=0;//显示的顺序
//	private boolean genQuery=false;//是否生成查询条件，主要是在grid
	
	private Boolean isEnum=false;
//	private String showType="none";//显示的类型，是textfield，还是combobox，还是radio，还是
//	private Map<String,String> showType_values=new HashMap<String,String>();

	private String basepackage;//包名
	private String className;
	private Class<?> clazz;
	private String simpleClassName;//其实不用定义这个属性，其实是可以直接写get方法，下同
//	private String jsType;
	
	private boolean isId;//是否是id的属性
	private boolean isCompositeId=false;
	private IDGenEnum idGenEnum=IDGenEnum.none;
	
	
	//private Boolean isIdProperty=false;//是不是属于id的列
	//private Boolean isComponentType=false;
	//private Boolean isAssociationType=false;
	//private Boolean isBaseType=false;
	//private Boolean isCollectionType=false;
	//private Boolean isConstantType=false;//判断是不是常数
	
	//List<PropertyColumn> propertyColumns=new ArrayList<PropertyColumn>();
	
//	//前段展示的时候的标签名字
//	private String label;
//	//展现方式，是下拉框，数字矿还是文本框
//	private String showModel;
	
//	private static Map<Class,String> jsJavaMapper=new HashMap<Class,String>();
//	static {
//		jsJavaMapper.put(String.class, "string");
//		jsJavaMapper.put(Charset.class, "string");
//		jsJavaMapper.put(char.class, "string");
//		
//		jsJavaMapper.put(boolean.class, "bool");
//		jsJavaMapper.put(Boolean.class, "bool");
//		
//		jsJavaMapper.put(byte.class, "int");
//		jsJavaMapper.put(Byte.class, "int");
//		jsJavaMapper.put(short.class, "int");
//		jsJavaMapper.put(Short.class, "int");
//		jsJavaMapper.put(int.class, "int");
//		jsJavaMapper.put(Integer.class, "int");
//		jsJavaMapper.put(long.class, "int");
//		jsJavaMapper.put(Long.class, "int");
//		jsJavaMapper.put(BigInteger.class, "int");
//		
//		jsJavaMapper.put(float.class, "float");
//		jsJavaMapper.put(Float.class, "float");
//		jsJavaMapper.put(double.class, "float");
//		jsJavaMapper.put(Double.class, "float");
//		jsJavaMapper.put(BigDecimal.class, "float");
//		
//		jsJavaMapper.put(java.util.Date.class, "date");
//		jsJavaMapper.put(java.sql.Date.class, "date");
//		//jsJavaMapper.put(, "");
//		
//		//jsJavaMapper.put(, "");
//	}

	public void setClazz(Class<?> clazz) {
		this.clazz=clazz;
		this.className = clazz.getName();
		if(clazz.isPrimitive()) {
			throw new RuntimeException("请把"+this.getProperty()+"转换成包装类型");
		}
		this.basepackage=clazz.getPackage().getName();
		//System.out.println(javaType);
		this.simpleClassName=className.substring(className.lastIndexOf('.')+1);
//		//System.out.println(this.javaTypeClassName);
//		if(jsJavaMapper.get(clazz)==null){
//			this.jsType="string";
//		} else {
//			this.jsType=jsJavaMapper.get(clazz);//映射好后就是替换 自己写的类然后测试
//		}
//		
		//System.out.println(this.jsType);
	}

	public boolean getIsId() {
		return isId;
	}

	public void setIsId(boolean isID) {
		this.isId = isID;
	}

	public boolean isCompositeId() {
		return isCompositeId;
	}


	public void setIsCompositeId(boolean isCompositeId) {
		this.isCompositeId = isCompositeId;
	}

	public String getColumn() {
		return column;
	}


	public void setColumn(String column) {
		this.column = column;
	}


	public String getProperty() {
		return property;
	}


	public void setProperty(String property) {
		this.property = property;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getDefaultValue() {
		return defaultValue;
	}


	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public int getPrecision() {
		return precision;
	}


	public void setPrecision(int precision) {
		this.precision = precision;
	}


	public int getScale() {
		return scale;
	}


	public void setScale(int scale) {
		this.scale = scale;
	}


	public boolean isUnique() {
		return unique;
	}


	public void setUnique(boolean unique) {
		this.unique = unique;
	}


	public boolean isNullable() {
		return nullable;
	}


	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}


	public boolean isInsertable() {
		return insertable;
	}


	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}


	public boolean isUpdatable() {
		return updatable;
	}


	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}


	public Boolean getIsEnum() {
		return isEnum;
	}


	public void setIsEnum(Boolean isEnum) {
		this.isEnum = isEnum;
	}


	public String getBasepackage() {
		return basepackage;
	}


	public void setBasepackage(String basepackage) {
		this.basepackage = basepackage;
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public String getSimpleClassName() {
		return simpleClassName;
	}


	public void setSimpleClassName(String simpleClassName) {
		this.simpleClassName = simpleClassName;
	}


	

	public IDGenEnum getIdGenEnum() {
		return idGenEnum;
	}


	public void setIdGenEnum(IDGenEnum idGenEnum) {
		this.idGenEnum = idGenEnum;
	}


	public Class<?> getClazz() {
		return clazz;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	@Override
	public String toString() {
		return "PropertyColumn [column=" + column + ", columnType=" + columnType + ", property=" + property + ", label="
				+ label + ", comment=" + comment + ", defaultValue=" + defaultValue + ", length=" + length
				+ ", precision=" + precision + ", scale=" + scale + ", unique=" + unique + ", nullable=" + nullable
				+ ", insertable=" + insertable + ", updatable=" + updatable + ", isEnum=" + isEnum + ", basepackage="
				+ basepackage + ", className=" + className + ", clazz=" + clazz + ", simpleClassName=" + simpleClassName
				+ ", isId=" + isId + ", isCompositeId=" + isCompositeId + ", idGenEnum=" + idGenEnum + "]";
	}


	


	

}
