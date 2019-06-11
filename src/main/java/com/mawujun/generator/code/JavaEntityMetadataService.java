package com.mawujun.generator.code;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.ibatis.type.Alias;

import com.mawujun.generator.annotation.ColDefine;
import com.mawujun.generator.annotation.LogicDelete;
import com.mawujun.generator.annotation.TableDefine;
import com.mawujun.generator.other.DefaultNameStrategy;
import com.mawujun.generator.other.NameStrategy;
import com.mawujun.util.PropertiesUtils;
import com.mawujun.util.ReflectUtil;
import com.mawujun.util.StringUtils;

/**
 * 用于从领域模型中读取 meta信息的
 * 
 * @author mawujun qq:16064988 e-mail:16064988@qq.com
 */
public class JavaEntityMetadataService {

	NameStrategy nameStrategy=new DefaultNameStrategy();
	
	//String id_name="id";//默认的id名称
	
	private String code_tablePrefix;
	private String code_columnPrefix;
	
	private static Map<String,EntityTable> cache=new HashMap<String,EntityTable>();
	
	public JavaEntityMetadataService() {
		
		try {
			PropertiesUtils aa = PropertiesUtils.load("generator.properties");
			String className=aa.getProperty("nameStrategy");
			if(StringUtils.hasText(className)) {
				Class clazz=Class.forName(className);
				nameStrategy=(NameStrategy) clazz.newInstance();
			}
			
			code_tablePrefix=aa.getProperty("code.tablePrefix");
			code_columnPrefix=aa.getProperty("code.columnPrefix");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	public EntityTable getClassProperty(Class clazz){
		if(cache.containsKey(clazz.getName())){
			return cache.get(clazz.getName());
		}
		try {
			return initClassProperty(clazz);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public EntityTable initClassProperty(Class clazz) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(cache.containsKey(clazz.getName())){
			return cache.get(clazz.getName());
		}
		EntityTable root=new EntityTable();
		//ll
		Table tableAnnotation=(Table)clazz.getAnnotation(Table.class);
		if(tableAnnotation!=null){
			if(StringUtils.hasText(tableAnnotation.name())) {
				root.setEntityTableName(tableAnnotation.name());
			} else {
				//throw new RuntimeException("@Table注解的表名需要设置");
				root.setEntityTableName(nameStrategy.classToTableName(clazz.getSimpleName(),code_tablePrefix));
			}
			
		} else {
			throw new RuntimeException("没有在实体类上添加@Table注解");
		}
		Entity entityAnnotation=(Entity)clazz.getAnnotation(Entity.class);
		if(entityAnnotation!=null){
			//root.setTableName(entityAnnotation.name());
		} else {
			throw new RuntimeException("没有在实体类上添加@Entity注解");
		}
		Alias aliasAnnotation=(Alias)clazz.getAnnotation(Alias.class);
		if(aliasAnnotation!=null){
			root.setAlias(aliasAnnotation.value());
		} else {
			//throw new RuntimeException("@Alias注解需要设置，因为现在使用mybatis");
			//root.setAlias(nameStrategy.classToAlias(clazz.getSimpleName()));
			root.setAlias(clazz.getName());
		}
		
		root.setEntityClass(clazz);
		//root.setSimpleClassName(clazz.getSimpleName());
		//root.setClassName(clazz.getName());
		
		root.setBasepackage(clazz.getPackage().getName());
		if(root.getBasepackage().lastIndexOf('.')!=-1) {
			root.setBasepackage(root.getBasepackage().substring(0, root.getBasepackage().lastIndexOf('.')));
		}
		
		TableDefine tableDefine=(TableDefine)clazz.getAnnotation(TableDefine.class);
		if(tableDefine!=null) {
			if(StringUtils.hasText(tableDefine.comment())) {
				root.setComment(tableDefine.comment());
			}
		}

		
		
		Field[] fields=ReflectUtil.getFields(clazz);
		//List<PropertyColumn> propertyColumns =new ArrayList<PropertyColumn>();
		//存放需要产生查询条件的属性
		//List<PropertyColumn> queryProperties =new ArrayList<PropertyColumn>();
		for(Field field:fields){
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			if(isStatic) {
				continue;
			}
			PropertyColumn propertyColumn=new PropertyColumn();
			propertyColumn.setProperty(field.getName());
			propertyColumn.setLabel(field.getName());
//			FieldDefine fieldDefine=field.getAnnotation(FieldDefine.class);
//			if(fieldDefine!=null){
//				if(fieldDefine.title()==null || "".equals(fieldDefine.title())){
//					propertyColumn.setLabel(field.getName());
//				} else {
//					propertyColumn.setLabel(fieldDefine.title());
//				}
//				propertyColumn.setHidden(fieldDefine.hidden());
//				propertyColumn.setSort(fieldDefine.sort());
//				propertyColumn.setShowType(fieldDefine.showType().toString());
//				if(fieldDefine.showType()!=ShowType.none){
//					//如果是枚举类型，就反射获取枚举值，作为数据的内容，如果不是枚举类型，就弄成一个从后台获取内容的combobox
//					if(field.getType().isEnum()){
//						propertyColumn.setIsEnum(true);
//						//field.get
//						Class clz =field.getType();
//						Method toName = clz.getMethod("getName");
//						//Map<String,String> showTypeValues=new HashMap<String,String>();
//						for (Object obj : clz.getEnumConstants()) {
//							propertyColumn.addShowType_value(obj.toString(), toName.invoke(obj).toString());
//							//System.out.println(obj);
//							//System.out.println(toName.invoke(obj));
//						}
//					}
//				}
//				propertyColumn.setGenQuery(fieldDefine.genQuery());
//			}
			//不准为空的判断
			Column column=field.getAnnotation(Column.class);
			if(column!=null){
				propertyColumn.setNullable(column.nullable());
				propertyColumn.setUnique(column.unique());
				propertyColumn.setInsertable(column.insertable());
				propertyColumn.setUpdatable(column.updatable());
				if(StringUtils.hasText(column.name())) {
					propertyColumn.setColumn(column.name());
				} else {
					propertyColumn.setColumn(nameStrategy.propertyToColumnName(propertyColumn.getProperty(),code_columnPrefix));
				}
				propertyColumn.setLength(column.length());
				propertyColumn.setScale(column.scale());
				propertyColumn.setPrecision(column.precision());
				
			} else {
				propertyColumn.setColumn(nameStrategy.propertyToColumnName(propertyColumn.getProperty(),code_columnPrefix));
			}
			propertyColumn.setClazz(field.getType());
			
			
			
			
			//表示可嵌入类的组合主键
			EmbeddedId embeddedId=field.getAnnotation(EmbeddedId.class);
			if(embeddedId!=null) {
				Field[] embeddedIdFields=ReflectUtil.getFields(field.getType());
				String[] idColumns=new String[embeddedIdFields.length];
				String[] idPropertys=new String[embeddedIdFields.length];
				
				for(int i=0;i<embeddedIdFields.length;i++) {
					Field field_temp=embeddedIdFields[i];
					idColumns[i]=nameStrategy.propertyToColumnName(field_temp.getName(),code_columnPrefix);
					idPropertys[i]=field_temp.getName();
				}
				
				propertyColumn.setIsId(true);
				propertyColumn.setIsCompositeId(true);
				
				root.setIsCompositeId(true);
				root.setIdColumns(idColumns);
				root.setIdPropertys(idPropertys);
				root.setIdClass(field.getType());
				root.setIdGenEnum(IDGenEnum.none);
				root.setIdSequenceName(null);
			} 
			//是id主键
			Id id=field.getAnnotation(Id.class);
			if(id!=null){
				//有可能是复合主键，也有可能是单主键
				IdClass idClass=(IdClass)clazz.getAnnotation(IdClass.class);
				if(idClass!=null) {
					propertyColumn.setNullable(false);
					propertyColumn.setUnique(false);
					propertyColumn.setInsertable(true);
					propertyColumn.setUpdatable(true);
					
					propertyColumn.setIsId(true);
					propertyColumn.setIsCompositeId(true);
					
					root.setIsCompositeId(true);
					root.addIdColumn(propertyColumn.getColumn());
					root.addIdProperty(propertyColumn.getProperty());
					root.setIdClass(idClass.value());
					root.setIdGenEnum(IDGenEnum.none);
					root.setIdSequenceName(null);
					
					Field[] idClass_fields=ReflectUtil.getFields(idClass.value());
					for(Field idField:idClass_fields) {
						assignComment(idField,propertyColumn);
					}
					
					
					
				} else {
					propertyColumn.setNullable(false);
					propertyColumn.setUnique(true);
					propertyColumn.setInsertable(true);
					propertyColumn.setUpdatable(false);
					
					propertyColumn.setIsCompositeId(false);
					propertyColumn.setIsId(true);
					
					root.setIsCompositeId(false);
					root.setIdColumns(new String[] {propertyColumn.getColumn()});
					root.setIdPropertys(new String[] {propertyColumn.getProperty()});
					root.setIdClass(field.getType());
					root.setIdGenEnum(IDGenEnum.none);
					root.setIdSequenceName(null);
					//
					GeneratedValue generatedValue=field.getAnnotation(GeneratedValue.class);
					if(generatedValue!=null) {
						GenerationType strategy=generatedValue.strategy();
						if(strategy==GenerationType.SEQUENCE) {
							propertyColumn.setIdGenEnum(IDGenEnum.sequence);
							root.setIdGenEnum(propertyColumn.getIdGenEnum());
							SequenceGenerator sequenceGenerator=field.getAnnotation(SequenceGenerator.class);
							root.setIdSequenceName(sequenceGenerator.sequenceName());
						} else if(strategy==GenerationType.IDENTITY) {
							propertyColumn.setIdGenEnum(IDGenEnum.identity);
							root.setIdGenEnum(propertyColumn.getIdGenEnum());
						} else if(strategy==GenerationType.TABLE) {
							propertyColumn.setIdGenEnum(IDGenEnum.table);
							root.setIdGenEnum(propertyColumn.getIdGenEnum());
						} else {
							propertyColumn.setIdGenEnum(IDGenEnum.uuid);
							root.setIdGenEnum(propertyColumn.getIdGenEnum());
						}
					} else {
						if(field.getType()==String.class) {
							propertyColumn.setIdGenEnum(IDGenEnum.assigned_str);
							root.setIdGenEnum(propertyColumn.getIdGenEnum());
						} else {
							propertyColumn.setIdGenEnum(IDGenEnum.assigned_long);
							root.setIdGenEnum(propertyColumn.getIdGenEnum());
						} 
						
					}
				}	
			}
			
			//注释和默认值,一定要放在id判断的后面
			assignComment(field,propertyColumn);
			
			
			NotNull notNull=field.getAnnotation(NotNull.class);
			if(notNull!=null){
				propertyColumn.setNullable(false);
			}
			NotEmpty notEmpty=field.getAnnotation(NotEmpty.class);
			if(notEmpty!=null){
				propertyColumn.setNullable(false);
			}
			Size size=field.getAnnotation(Size.class);
			if(size!=null){
				if(size.max()!=Integer.MAX_VALUE) {
					propertyColumn.setLength(size.max());
				}
				
			}
			Version version=field.getAnnotation(Version.class);
			if(version!=null){
				propertyColumn.setIsVersion(true);
			}
			LogicDelete logicDelete=field.getAnnotation(LogicDelete.class);
			if(logicDelete!=null){
				propertyColumn.setIsLogicDelete(true);
			}
			
			//判断是否进行持久化
			Transient ttransient = field.getAnnotation(Transient.class);
			if(ttransient==null){
				propertyColumn.setPersistable(true);
			}
			
			//
			//if(field.isEnumConstant()) {
			if(field.getType().isEnum()) {
				propertyColumn.setIsEnum(true);
			}
			
			
			
			
			//propertyColumns.add(propertyColumn);
			root.addPropertyColumn(propertyColumn);
			
//			//默认是使用id作为名称
//			if(id_name.equals(propertyColumn.getProperty())){
//				root.setIdType(field.getType().getSimpleName());
//			}
			
//			if(propertyColumn.getGenQuery()){
//				queryProperties.add(propertyColumn);
//			}
		}

		//对属性显示的时候进行排序
		//propertyColumns.sort(new PropertyColumnComparator());
		
		
		//root.setPropertyColumns(propertyColumns);
		//root.setQueryProperties(queryProperties);
		cache.put(clazz.getName(), root);
		return root;
	}
	
	private void assignComment(Field field,PropertyColumn propertyColumn) {
		ColDefine colDefinition=field.getAnnotation(ColDefine.class);
		if(colDefinition!=null) {
			if(StringUtils.hasText(colDefinition.defaultValue())) {
				propertyColumn.setDefaultValue(colDefinition.defaultValue());
			}
			if(StringUtils.hasText(colDefinition.label())) {
				propertyColumn.setLabel(colDefinition.label());
				propertyColumn.setComment(colDefinition.label());
			}
			if(StringUtils.hasText(colDefinition.comment())) {
				propertyColumn.setComment(colDefinition.comment());
			}
			
			propertyColumn.setCndable(colDefinition.cndable());
		}
	}
	

}
