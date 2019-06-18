package com.mawujun.generator.code;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import com.mawujun.generator.other.DefaultNameStrategy;
import com.mawujun.generator.other.NameStrategy;
import com.mawujun.generator.rules.EmailRule;
import com.mawujun.generator.rules.Mobile;
import com.mawujun.generator.rules.MobileRule;
import com.mawujun.generator.rules.NumberRule;
import com.mawujun.generator.rules.Phone;
import com.mawujun.generator.rules.PhoneRule;
import com.mawujun.generator.rules.RequireRule;
import com.mawujun.generator.rules.StringRule;
import com.mawujun.generator.rules.URLRule;
import com.mawujun.repository.annotation.ColDefine;
import com.mawujun.repository.annotation.FK;
import com.mawujun.repository.annotation.LogicDelete;
import com.mawujun.repository.annotation.TableDefine;
import com.mawujun.util.PropertiesUtils;
import com.mawujun.util.ReflectUtil;
import com.mawujun.util.StringUtil;

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
			if(StringUtil.hasText(className)) {
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
			if(StringUtil.hasText(tableAnnotation.name())) {
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
			if(StringUtil.hasText(tableDefine.comment())) {
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
				if(StringUtil.hasText(column.name())) {
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
				//添加枚举类型的值
				Class<Enum> enumClass=(Class<Enum>) field.getType();
				Enum[] enumConstants = enumClass.getEnumConstants();
				//根据方法名获取方法
                Method label = enumClass.getMethod("getLabel");
                for (Enum enum1 : enumConstants) {
                	Object label_str = label.invoke(enum1);
                	propertyColumn.addEnumValues(enum1.name(), (String)label_str);
                }
			}

			
			//构建校验规则
			//先判断
			//boolean required=false;//!propertyColumn.isNullable();
			//String required_msg=null;
			if(notNull!=null) {
				RequireRule rule=new RequireRule();
				if("{javax.validation.constraints.NotNull.message}".equals(notNull.message())) {
					rule.setMessage(propertyColumn.getLabel()+"不能为空!");
				} else {
					rule.setMessage(notNull.message());
				}
				
				root.addRule(propertyColumn.getProperty(), rule);
			}
			NotBlank rule_notblank=field.getAnnotation(NotBlank.class);
			if(rule_notblank!=null) {
				RequireRule rule=new RequireRule();
				if("{javax.validation.constraints.NotBlank.message}".equals(rule_notblank.message())) {
					rule.setMessage(propertyColumn.getLabel()+"不能为空!");
				} else {
					rule.setMessage(rule_notblank.message());
				}
				root.addRule(propertyColumn.getProperty(), rule);
			}
			NotEmpty rule_notempty=field.getAnnotation(NotEmpty.class);
			if(rule_notempty!=null) {
				RequireRule rule=new RequireRule();
				if("{javax.validation.constraints.NotEmpty.message}".equals(rule_notempty.message())) {
					rule.setMessage(propertyColumn.getLabel()+"不能为空!");
				} else {
					rule.setMessage(rule_notempty.message());
				}			
				root.addRule(propertyColumn.getProperty(), rule);
			}

			
			Size rule_size=field.getAnnotation(Size.class);
			if(rule_size!=null) {
				StringRule rule=new StringRule();
				if(propertyColumn.getClazz()!=String.class) {
					rule.setType("'number'");
				}
				if("{javax.validation.constraints.Size.message}".equals(rule_size.message())) {
					if("'number'".equals(rule.getType())) {
						rule.setMessage(propertyColumn.getLabel()+"请输入"+rule_size.min()+"~"+rule_size.max()+"范围内的值。!");
						propertyColumn.setNumberValidRule(true);
					} else {
						rule.setMessage(propertyColumn.getLabel()+"长度应在"+rule_size.min()+"到"+rule_size.max()+" 个字符!");
					}				
				} else {
					rule.setMessage(rule_size.message());
				}	
				
				rule.setMin(rule_size.min());
				rule.setMax(rule_size.max());
				root.addRule(propertyColumn.getProperty(), rule);
			}
			Length rule_length=field.getAnnotation(Length.class);
			if(rule_length!=null) {
				StringRule rule=new StringRule();
				if(propertyColumn.getClazz()!=String.class) {
					rule.setType("'number'");
				}
				if("{org.hibernate.validator.constraints.Length.message}".equals(rule_length.message())) {
					if("'number'".equals(rule.getType())) {
						rule.setMessage(propertyColumn.getLabel()+"请输入"+rule_length.min()+"~"+rule_length.max()+"范围内的值。!");
						propertyColumn.setNumberValidRule(true);
					} else {
						rule.setMessage(propertyColumn.getLabel()+"长度应在"+rule_length.min()+"到"+rule_length.max()+" 个字符!");
					}				
				} else {
					rule.setMessage(rule_length.message());
				}	
				
				rule.setMin(rule_length.min());
				rule.setMax(rule_length.max());
				root.addRule(propertyColumn.getProperty(), rule);
			}
			
			Min rule_min=field.getAnnotation(Min.class);
			Max rule_max=field.getAnnotation(Max.class);
			if(rule_min!=null || rule_max!=null) {
				propertyColumn.setNumberValidRule(true);
				NumberRule rule=new NumberRule();
				if(rule_min!=null) {
					rule.setMessage(rule_min.message());
					rule.setMin((int)rule_min.value());
				}
				if(rule_max!=null) {
					rule.setMessage(rule.getMessage()!=null?(rule.getMessage()+","+rule_max.message()):rule_max.message());
					rule.setMax((int)rule_max.value());
				}
				root.addRule(propertyColumn.getProperty(), rule);
			}
			Range rule_range=field.getAnnotation(Range.class);
			if(rule_range!=null) {
				propertyColumn.setNumberValidRule(true);
				NumberRule rule=new NumberRule();
				if("{org.hibernate.validator.constraints.Range.message}".equals(rule_range.message())) {
					rule.setMessage(propertyColumn.getLabel()+"请输入"+rule_range.min()+"~"+rule_range.max()+"范围内的值。!");
				} else {
					rule.setMessage(rule_range.message());
				}
				
				rule.setMin((int)rule_range.min());
				rule.setMax((int)rule_range.max());
				root.addRule(propertyColumn.getProperty(), rule);
			}
			Email rule_email=field.getAnnotation(Email.class);
			if(rule_email!=null) {
				EmailRule rule=new EmailRule();
				if("{javax.validation.constraints.Email.message}".equals(rule_email.message())) {
					rule.setMessage(propertyColumn.getLabel()+"格式不正确!");
				} else {
					rule.setMessage(rule_email.message());
				}
				
				root.addRule(propertyColumn.getProperty(), rule);
			}
			
			Mobile rule_mobile=field.getAnnotation(Mobile.class);
			if(rule_mobile!=null) {
				MobileRule rule=new MobileRule();
				if("{com.mawujun.generator.rules.Mobile.message}".equals(rule_mobile.message())) {
					//rule.setMessage(propertyColumn.getLabel()+"格式不正确!");
				} else {
					rule.setMessage(rule_mobile.message());
				}
				
				root.addRule(propertyColumn.getProperty(), rule);
			}
			Phone rule_phone=field.getAnnotation(Phone.class);
			if(rule_phone!=null) {
				PhoneRule rule=new PhoneRule();
				if("{com.mawujun.generator.rules.Phone.message}".equals(rule_phone.message())) {
					//rule.setMessage(propertyColumn.getLabel()+"格式不正确!");
				} else {
					rule.setMessage(rule_phone.message());
				}
				
				root.addRule(propertyColumn.getProperty(), rule);
			}
			
			URL rule_url=field.getAnnotation(URL.class);
			if(rule_url!=null) {
				URLRule rule=new URLRule();
				if("{org.hibernate.validator.constraints.URL.message}".equals(rule_url.message())) {
					//rule.setMessage(propertyColumn.getLabel()+"格式不正确!");
				} else {
					rule.setMessage(rule_url.message());
				}
				
				root.addRule(propertyColumn.getProperty(), rule);
			}
			
			assignFK(field,propertyColumn);
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
	/**
	 * 读取fk的内容
	 * @param field
	 * @param propertyColumn
	 */
	private void assignFK(Field field,PropertyColumn propertyColumn) {
		FK fk=field.getAnnotation(FK.class);
		if(fk!=null) {
			propertyColumn.setIsFk(true);
			
			Class clazz=fk.refEntity();

			String basepackage=clazz.getPackage().getName();
			//System.out.println(javaType);
			String simpleClassName=clazz.getName().substring(clazz.getName().lastIndexOf('.')+1);
			propertyColumn.setFk_entitySimpleClassName(simpleClassName);
			propertyColumn.setFk_entitySimpleClassNameUncap(StringUtils.uncapitalize(simpleClassName));
			
			String[] packages=clazz.getPackage().getName().split("\\.");
			String module=packages[packages.length-2];//一般实体类是放在entity下面，所以模块就取前一个
			propertyColumn.setFk_module(module);
		}
	}
	private void assignComment(Field field,PropertyColumn propertyColumn) {
		ColDefine colDefinition=field.getAnnotation(ColDefine.class);
		if(colDefinition!=null) {
			if(StringUtil.hasText(colDefinition.defaultValue())) {
				propertyColumn.setDefaultValue(colDefinition.defaultValue());
			}
			if(StringUtil.hasText(colDefinition.label())) {
				propertyColumn.setLabel(colDefinition.label());
				propertyColumn.setComment(colDefinition.label());
			}
			if(StringUtil.hasText(colDefinition.comment())) {
				propertyColumn.setComment(colDefinition.comment());
			}
			
			propertyColumn.setCndable(colDefinition.cndable());
			
			propertyColumn.setUploadable(colDefinition.uploadable());
			propertyColumn.setDisabled(colDefinition.disabled());
		}
	}
	

}
