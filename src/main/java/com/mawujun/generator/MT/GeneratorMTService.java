package com.mawujun.generator.MT;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mawujun.generator.annotation.ColDefine;
import com.mawujun.lang.Assert;
import com.mawujun.util.StringUtils;
import com.mawujun.utils.PropertiesUtils;

/**
 * 生成的类用来快速的引用某个领域类的字段，这样既可以保证字段的准确度，又可以快速引用
 * @author mawujun email:16064988@qq.com qq:16064988
 *
 */
public class GeneratorMTService {
	static Logger logger = LoggerFactory.getLogger(GeneratorMTService.class);
	
	//private Class annotationClass=javax.persistence.Entity.class;
	//private Class annotationTable=javax.persistence.Table.class;
	
	private static String targetPackage;
	
	/**
	 * 从generator.properties目录汇总读取packageToScan
	 * @param targetPackage 存放的目标包 例如：com.mawujun.utils
	 * @param isTest 生成放在test目录还是main目录
	 */
	public static void generateMT(String targetPackage,Boolean isTest) {
		PropertiesUtils utils=PropertiesUtils.load("generator.properties");
		String packageToScan=utils.getProperty("packageToScan");
		Assert.notNull(packageToScan,"generator.properties中packageToScan不能为null");
		generateMT(packageToScan,targetPackage,isTest);

	}
	/**
	 * 在当前项目的源代码目录生成,M，T类,即在src/main/java目录下
	 * @param packageName
	 * @param targetPackage 存放的目标包 例如：com.mawujun.utils
	 * @param isTest 生成放在test目录还是main目录
	 * @throws IOException
	 */
	public static void generateMT(String packageToScan,String targetPackage,Boolean isTest) {
		generateMT(packageToScan,System.getProperty("user.dir")+File.separator+"src"+File.separator+(isTest?"test":"main")+File.separator+"java",targetPackage);
	}
	/**
	 * 搜索某个路径下面，标注了@Entity的类，并生成和android中的R类似的类，M
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param packageToScan 从哪些包中进行搜索,支持以都还分隔的多个报名
	 * @param targetMDir 生成的目标地址 :E:\\eclipse\\workspace\\hujibang\\src\\test\\java
	 * @param targetPackage 存放的目标包 例如：com.mawujun.utils
	 * @param isTest 生成放在test目录还是main目录
	 * @throws IOException
	 */
	public static void generateMT(String packageToScan,String targetMDir,String targetPackage) {
		Assert.notNull(packageToScan,"packageToScan不能为null");
		Assert.notNull(targetMDir,"targetMDir不能为null");
		Assert.notNull(targetPackage,"targetPackage不能为null");
		GeneratorMTService.targetPackage=targetPackage;
		try {
			Set<Class> classes=new HashSet<Class>();
			for(String pkg:packageToScan.split(",")) {
				classes.addAll(getClasssFromPackage(pkg));
			}
			generateM(classes,targetMDir);
			
			generateT(classes,targetMDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
    /** 
     * 获得包下面的所有的class 
     *  
     * @param pack 
     *            package完整名称 
     * @return List包含所有class的实例 
     */  
    private static List<Class> getClasssFromPackage(String pack) {  
        List<Class> clazzs = new ArrayList<Class>();  
      
        // 是否循环搜索子包  
        boolean recursive = true;  
      
        // 包名字  
        String packageName = pack;  
        // 包名对应的路径名称  
        String packageDirName = packageName.replace('.','/');  
      
        Enumeration<URL> dirs;  
      
        try {  
            dirs = GeneratorMTService.class.getClassLoader().getResources(packageDirName);  
            while (dirs.hasMoreElements()) {  
                URL url = dirs.nextElement();  
      
                String protocol = url.getProtocol();  
      
                if ("file".equals(protocol)) {  
                	logger.info("file类型的扫描");  
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");  
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);  
                } else if ("jar".equals(protocol)) {  
                	logger.info("jar类型的扫描");  
                }  
            }  
      
        } catch (Exception e) {  
            e.printStackTrace();  
            logger.info(e.getMessage()); 
        }  
      
        return clazzs;  
    } 
    
    
    /** 
     * 在package对应的路径下找到所有的class 
     *  
     * @param packageName 
     *            package名称 
     * @param filePath 
     *            package对应的路径 
     * @param recursive 
     *            是否查找子package 
     * @param clazzs 
     *            找到class以后存放的集合 
     */  
    private static void findClassInPackageByFile( String packageName, String filePath, final boolean recursive, List<Class> clazzs) {  

        File dir = new File(filePath);  
        if (!dir.exists() || !dir.isDirectory()) {  
            return;  
        }  
        // 在给定的目录下找到所有的文件，并且进行条件过滤  
        File[] dirFiles = dir.listFiles(new FileFilter() {  
            public boolean accept(File file) {  
                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录  
                boolean acceptClass = file.getName().endsWith("class");// 接受class文件  
                return acceptDir || acceptClass;  
            }  
        });  
      
        for (File file : dirFiles) {  
            if (file.isDirectory()) {  
                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);  
            } else {  
                String className = file.getName().substring(0, file.getName().length() - 6);  
                try {  
                	Class clazz=GeneratorMTService.class.getClassLoader().loadClass(packageName + "." + className);
                	Annotation annoation=clazz.getAnnotation(Entity.class);
    				if(annoation!=null){
    					logger.info("============================找到实体类:"+clazz.getName());
    					clazzs.add(clazz); 
    				}		
                     
                   
                } catch (Exception e) {  
                    //e.printStackTrace();  
                    logger.error("",e);  
                    
                }  catch (NoClassDefFoundError e) {  
                    //e.printStackTrace();  
                    logger.error("",e);  
                    return;
                }  
                logger.info(packageName + "." + className);  
            }  
        }  
    }  
    /**
     * 产生领域模型的类
     * @author mawujun email:160649888@163.com qq:16064988
     * @param entities
     * @throws IOException
     */
    private static void generateM(Set<Class> entities,String targetMDir) throws IOException{
    	File dir=new File(targetMDir+File.separatorChar+GeneratorMTService.targetPackage.replace('.', File.separatorChar));
    	if(!dir.exists()) {
    		dir.mkdirs();
    	}
    	//生成M
    	File file=new File(targetMDir+File.separatorChar+GeneratorMTService.targetPackage.replace('.', File.separatorChar)+File.separatorChar+"M.java");
    	//file.delete();
    	if(!file.exists()){
    		file.createNewFile();
    	}
    	FileWriter fileWrite=new FileWriter(file);
    	
    	    	
    	//StringBuilder builder=new StringBuilder();
    	fileWrite.append("package "+GeneratorMTService.targetPackage+";\n");
    	fileWrite.append("public final class M {\n");
    	
    	
    	for(Class clazz:entities){
    		logger.info("============================================="+clazz.getName());
    		fileWrite.append(generateComment(clazz));
    		fileWrite.append("public static final class "+clazz.getSimpleName()+" {\n");
    		 //Field[]fields = clazz.getDeclaredFields();
    		 List<Field> fields= getClassField(clazz);
    		 
    		 Set<String> existField=new HashSet<String>();
             for (Field field : fields) { //完全等同于上面的for循环
            	 if(!existField.contains(field.getName())){
            		 existField.add(field.getName());
            	 } else {
            		 continue;
            	 }
            	 logger.info(field.getName());
                 //System.out.println(field.getName()+" "+field.getType());
                 //fileWrite.append("public static final "+field.getType().getName()+" "+field.getName()+"=\""+field.getName()+"\";\n");
                 if(isBaseType(field.getType()) || field.getType().isEnum()){
//                	 Id id=field.getAnnotation(Id.class);
//                	 if(id!=null) {
//                		 IdClass idclass=(IdClass)clazz.getAnnotation(IdClass.class);
//                		 
//                	 } else {
                	 //现在还有点问题，IdClass类中的注释读取不到
                		 fileWrite.append(generateComment(field));
                    	 fileWrite.append("	public static final String "+field.getName()+"=\""+field.getName()+"\";\n");
//                	 }
                	 
                 } else if(!isOf(field.getType(),Map.class) && !isOf(field.getType(),Collection.class)){
                	 Class<?> fieldClass=field.getType();
                	 Annotation embeddedIdAnnotataion=field.getAnnotation(EmbeddedId.class);
                	 //是复合主键的情况下
                	 if(embeddedIdAnnotataion!=null){
                		 fileWrite.append("	 /**\n");
                    	 fileWrite.append("	 * 对复合主键的embeddedId的属性访问，返回值为id.id1,id.id2的形式\n");
                    	 fileWrite.append("	 */\n");
                    	 fileWrite.append("	public static final class "+field.getName()+" {\n");
                    	 //Field[] embeddedIdFields = fieldClass.getDeclaredFields();
                    	 List<Field> embeddedIdFields= getClassField(fieldClass);
                    	 for (Field embeddedIdfield : embeddedIdFields) { 
                    		 fileWrite.append(generateComment(embeddedIdfield));
                    		 fileWrite.append("		public static final String "+embeddedIdfield.getName()+"=\""+field.getName()+"."+embeddedIdfield.getName()+"\";\n");
                    	 }
                    	 fileWrite.append("			\n");
                    	 
                     	 fileWrite.append("	    /**\n");
	                	 fileWrite.append("	    * 返回的是复合主键类的名称\n");
	                	 fileWrite.append("	    */\n");
	                	 fileWrite.append("	    public static String name(){ \n");
	                	 fileWrite.append("		    return \""+fieldClass.getSimpleName()+"\";\n");
	                	 fileWrite.append("	    }\n");
	                	 
                    	 fileWrite.append("	}\n");
                    	 
                    	 
                    	 fileWrite.append("	/**\n");
                    	 fileWrite.append("	* 这是一个复合主键，返回的是该复合主键的属性名称，在hql中使用:"+field.getName()+"\n");
                    	 fileWrite.append("	*/\n");
                    	 fileWrite.append("	public static final String "+field.getName()+"=\""+field.getName()+"\";\n");
                	 } else {

	                	 //返回关联类的属性，以对象关联的方式
	                	 fileWrite.append("	 /**\n");
                    	 fileWrite.append("	 * 返回关联对象的属性，，以对象关联的方式(a.b这种形式)，只有一些基本属性，层级不再往下了\n");
                    	 fileWrite.append("	 */\n");
                    	 fileWrite.append("	public static final class "+field.getName()+" {\n");
                    	 //Field[] embeddedIdFields = fieldClass.getDeclaredFields();
                    	 List<Field> embeddedIdFields= getClassField(fieldClass);
                    	 for (Field embeddedIdfield : embeddedIdFields) { 
                    		 if(isBaseType(embeddedIdfield.getType()) || embeddedIdfield.getType().isEnum()) {
                    			 fileWrite.append(generateComment(embeddedIdfield));
                    			 fileWrite.append("		public static final String "+embeddedIdfield.getName()+"=\""+field.getName()+"."+embeddedIdfield.getName()+"\";\n");
                    		 }
                    	 }
                    	 //返回该属性的名称
                    	 fileWrite.append("			\n");
                     	 fileWrite.append("	    /**\n");
	                	 fileWrite.append("	    * 返回的是关联类的属性名称，主要用于属性过滤的时候\n");
	                	 fileWrite.append("	    */\n");
	                	 fileWrite.append("	    public static String name(){ \n");
	                	 fileWrite.append("		    return \""+field.getName()+"\";\n");
	                	 fileWrite.append("	    }\n");
	                	 
	                	 
                    	 fileWrite.append("	}\n");
                    	 
	   
                    	        	 
//	                	 fileWrite.append("	/**\n");
//	                	 fileWrite.append("	* 访问关联类的id，用于hql的时候，返回的是"+field.getName()+".id\n");
//	                	 fileWrite.append("	*/\n");
//	                	 fileWrite.append("	public static final String "+field.getName()+"_id=\""+field.getName()+".id\";\n");
//	                	 fileWrite.append("	/**\n");
//	                	 fileWrite.append("	* 返回的是关联类的属性名称，主要用于属性过滤的时候\n");
//	                	 fileWrite.append("	*/\n");
//	                	 fileWrite.append("	public static final String "+field.getName()+"=\""+field.getName()+"\";\n");
                	 }
                 } else {
                	 //其他关联类，例如集合等
                	 fileWrite.append("	/**\n");
                	 fileWrite.append("	* 这里一般是集合属性，返回的是"+field.getName()+"\n");
                	 fileWrite.append("	*/\n");
                	 fileWrite.append("	public static final String "+field.getName()+"=\""+field.getName()+"\";\n");
                 }
                
             }
             fileWrite.append("}\n");
    	}
    	fileWrite.append("}\n");
    	fileWrite.close();
    }
    
    private static StringBuilder generateComment(Class clazz) {
    	StringBuilder builder=new StringBuilder();
    	builder.append("	/**\n");
    	builder.append("	* 对应的类名是："+clazz.getName()+" \n");
    	Table table = (Table) clazz.getAnnotation(Table.class);
		if (table != null) {
			// String[] aa=new String[2];
			if (StringUtils.hasText(table.name())) {
				builder.append("	* 对应的表名是："+table.name()+" \n");
			}
		}else {
			builder.append("	* 对应的表名是："+clazz.getSimpleName()+" \n");
		}
		org.hibernate.annotations.Table table_hibernate = (org.hibernate.annotations.Table) clazz.getAnnotation(org.hibernate.annotations.Table.class);
		if (table_hibernate != null) {
			// String[] aa=new String[2];
			if (StringUtils.hasText(table_hibernate.comment())) {
				builder.append("	* 对应的表注释是："+table_hibernate.comment()+" \n");
			}
		}
		
    	
    	builder.append("	* \n");
    	builder.append("	*/\n");
    	return builder;
    	
    }
    private static StringBuilder generateComment(Field field) {
    	StringBuilder builder=new StringBuilder();
    	builder.append("	/**\n");
    	ColDefine colDefine = (ColDefine) field.getAnnotation(ColDefine.class);
		if (colDefine != null) {
			// String[] aa=new String[2];
			if (StringUtils.hasText(colDefine.comment())) {
				builder.append("	*"+colDefine.comment()+" \n");
			}
			if (StringUtils.hasText(colDefine.defaultValue())) {
				builder.append("	* 默认值是:"+colDefine.defaultValue()+" \n");
			}

		}
		
    	
    	builder.append("	* \n");
    	builder.append("	*/\n");
    	return builder;
    	
    }
    
    /**
     * 产生表的字段名
     * @author mawujun email:160649888@163.com qq:16064988
     * @param entities
     * @throws IOException
     */
    private static void generateT(Set<Class> entities,String targetMDir) throws IOException{
    	//生成T
    	File file=new File(targetMDir+File.separatorChar+GeneratorMTService.targetPackage.replace('.', File.separatorChar)+File.separatorChar+"T.java");
    	//file.delete();
    	if(!file.exists()){
    		file.createNewFile();
    	}
    	FileWriter fileWrite=new FileWriter(file);
    	logger.info("生成的文件地址:"+file.getAbsolutePath());
    	
    	    	
    	//StringBuilder builder=new StringBuilder();
    	fileWrite.append("package "+GeneratorMTService.targetPackage+";\n");
    	fileWrite.append("public final class T {\n");
    	
    	
    	for(Class clazz:entities){
    		

    		Table annoation=(Table)clazz.getAnnotation(Table.class);
    		String tablename=null;
    		if(annoation==null){
    			//throw new NullPointerException(clazz.getClass()+"的Table注解没有设置");
    			tablename=clazz.getSimpleName();
    		} else {
    			tablename=annoation.name();
    		}
    		logger.info("============================================="+annoation.name());
    		
    		//fileWrite.append("public static final class "+clazz.getSimpleName()+" {\n");
    		fileWrite.append(generateComment(clazz));
    		fileWrite.append("public static final class "+tablename+" {\n");
    		
    		 fileWrite.append("	 /**\n");
         	 fileWrite.append("	 *  这个是表的名称,返回的是这个表的名称\n");
         	 fileWrite.append("	 */\n");
         	 fileWrite.append("	public static final String tablename_=\""+tablename+"\";\n");
         	 
         	 
    		 //Field[]fields = clazz.getDeclaredFields();
    		 Set<String> existField=new HashSet<String>();
    		 
    		 IdClass idClassAnnotation=(IdClass)clazz.getAnnotation(IdClass.class);
    		 List<Field> idClass_fiekds=null;
    		 if(idClassAnnotation!=null) {
    			 Class idClass=idClassAnnotation.value();
    			 
    			 idClass_fiekds=getClassField(idClass);
    			 if(idClass_fiekds!=null && idClass_fiekds.size()>0) {
             		// Class<?> fieldClass=field.getType();
             		 fileWrite.append("	 /**\n");
                 	 fileWrite.append("	 * 这个是复合主键。里面的是复合组件的组成列的列名\n");
                 	 fileWrite.append("	 */\n");
                 	 fileWrite.append("	public static final class PK {\n");
                 	 //Field[] embeddedIdFields = fieldClass.getDeclaredFields();
                 	 //List<Field> embeddedIdFields= getClassField(fieldClass);
                 	 for (Field embeddedIdfield : idClass_fiekds) { 
                 		fileWrite.append(generateComment(embeddedIdfield));
                 		 Column columnAnnotation=(Column)embeddedIdfield.getAnnotation(Column.class);
                 		 if(columnAnnotation==null || (columnAnnotation!=null && columnAnnotation.name().equals(""))){
             				 fileWrite.append("		public static final String "+StringUtils.camelToUnderline(embeddedIdfield.getName())+"=\""+StringUtils.camelToUnderline(embeddedIdfield.getName())+"\";\n");
             			 } else {
             				 fileWrite.append("		public static final String "+columnAnnotation.name()+"=\""+columnAnnotation.name()+"\";\n");
             			 }
                 	 }
                 	 fileWrite.append("			\n");
                 	 fileWrite.append("	}\n");
                 	 
//                 	 for (Field embeddedIdfield : idClass_fiekds) {
//                 		 Column columnAnnotation=(Column)embeddedIdfield.getAnnotation(Column.class);
//                 		 if(columnAnnotation==null || (columnAnnotation!=null && columnAnnotation.name().equals(""))){
//             				 fileWrite.append("	public static final String "+embeddedIdfield.getName()+"=\""+embeddedIdfield.getName()+"\";\n");
//             			 } else {
//             				 fileWrite.append("	public static final String "+columnAnnotation.name()+"=\""+columnAnnotation.name()+"\";\n");
//             			 }
//                 	 }
                 	 idClass_fiekds=null;
             	 }
    		 }
    				 
    		List<Field> fields= getClassField(clazz);
             for (Field field : fields) { //完全等同于上面的for循环
                 //System.out.println(field.getName()+" "+field.getType());
            	 if(!existField.contains(field.getName())){
            		 existField.add(field.getName());
            	 } else {
            		 continue;
            	 }
            	 logger.info(field.getName());
            	
            	 Annotation embeddedIdAnnotataion=field.getAnnotation(EmbeddedId.class);
            	 //是复合主键的情况下
            	 if(embeddedIdAnnotataion!=null){
            		 Class<?> fieldClass=field.getType();
            		 fileWrite.append("	 /**\n");
                	 fileWrite.append("	 * 这个是复合主键。里面的是复合组件的组成列的列名\n");
                	 fileWrite.append("	 */\n");
                	 fileWrite.append("	public static final class PK {\n");
                	 //Field[] embeddedIdFields = fieldClass.getDeclaredFields();
                	 List<Field> embeddedIdFields= getClassField(fieldClass);
                	 for (Field embeddedIdfield : embeddedIdFields) { 
                		 fileWrite.append(generateComment(embeddedIdfield));
                		 Column columnAnnotation=(Column)embeddedIdfield.getAnnotation(Column.class);
                		 if(columnAnnotation==null || (columnAnnotation!=null && !StringUtils.hasText(columnAnnotation.name()))){
            				 fileWrite.append("		public static final String "+StringUtils.camelToUnderline(embeddedIdfield.getName())+"=\""+StringUtils.camelToUnderline(embeddedIdfield.getName())+"\";\n");
            			 } else {
            				 fileWrite.append("		public static final String "+columnAnnotation.name()+"=\""+columnAnnotation.name()+"\";\n");
            			 }
                	 }
                	 fileWrite.append("			\n");
                	 fileWrite.append("	}\n");
                	 
                	 for (Field embeddedIdfield : embeddedIdFields) {
                		 fileWrite.append(generateComment(embeddedIdfield));
                		 Column columnAnnotation=(Column)embeddedIdfield.getAnnotation(Column.class);
                		 if(columnAnnotation==null || (columnAnnotation!=null && !StringUtils.hasText(columnAnnotation.name()))){
            				 fileWrite.append("	public static final String "+StringUtils.camelToUnderline(embeddedIdfield.getName())+"=\""+StringUtils.camelToUnderline(embeddedIdfield.getName())+"\";\n");
            			 } else {
            				 fileWrite.append("	public static final String "+columnAnnotation.name()+"=\""+columnAnnotation.name()+"\";\n");
            			 }
                	 }
            	 }  else if(isBaseType(field.getType()) || field.getType().isEnum()){
            		 	fileWrite.append(generateComment(field));
            			 Column columnAnnotation=(Column)field.getAnnotation(Column.class);
            			 if(columnAnnotation==null || (columnAnnotation!=null && !StringUtils.hasText(columnAnnotation.name()))){
            				 fileWrite.append("	public static final String "+StringUtils.camelToUnderline(field.getName())+"=\""+StringUtils.camelToUnderline(field.getName())+"\";\n");
            			 } else {
            				 fileWrite.append("	public static final String "+columnAnnotation.name()+"=\""+columnAnnotation.name()+"\";\n");
            			 }
                    	
                 } else if(!isOf(field.getType(),Map.class) && !isOf(field.getType(),Collection.class)){ 
                	 fileWrite.append(generateComment(field));
                	 Column columnAnnotation=(Column)field.getAnnotation(Column.class);
                    	 if(columnAnnotation==null || (columnAnnotation!=null && !StringUtils.hasText(columnAnnotation.name()))){
                    		 fileWrite.append("	/**\n");
                        	 fileWrite.append("	* 访问外键的列名，用于sql的时候，返回的是"+StringUtils.camelToUnderline(field.getName())+"_id\n");
                        	 fileWrite.append("	*/\n");
                        	 fileWrite.append("	public static final String "+StringUtils.camelToUnderline(field.getName())+"_id=\""+StringUtils.camelToUnderline(field.getName())+"_id\";\n");
                    	 } else {
                    		 fileWrite.append("	/**\n");
                        	 fileWrite.append("	* 访问外键的列名，用于sql的时候，返回的是"+columnAnnotation.name()+"_id\n");
                        	 fileWrite.append("	*/\n");
                        	 fileWrite.append("	public static final String "+columnAnnotation.name()+"=\""+columnAnnotation.name()+"\";\n");
                    	 }
                    	 
                 }     
                
             }
             fileWrite.append("}\n");
    	}
    	fileWrite.append("}\n");
    	fileWrite.close();
    }
    
    /** 
     * 这个方法，是最重要的，关键的实现在这里面 
     *  
     * @param aClazz 
     * @param aFieldName 
     * @return 
     */  
    private static List<Field> getClassField(Class aClazz) {  
	    Field[] declaredFields = aClazz.getDeclaredFields();  
	    List<Field> fields=new ArrayList<Field>();
	    
	    for (Field field : declaredFields) {  
	    	if("serialVersionUID".equals(field.getName())){
	    		continue;
	    	}
	    	fields.add(field);
	    }  
	  
	    Class superclass = aClazz.getSuperclass();  
	    
	    if (superclass != null) {// 简单的递归一下  
	       
	        fields.addAll( getClassField(superclass));
	    }  
	    return fields;  
	} 
    public static boolean isBaseType(Class clz){
		//如果是基本类型就返回true
		if(clz == UUID.class || clz == URL.class || clz == String.class || clz==Date.class || clz==java.sql.Date.class || clz==java.sql.Timestamp.class || clz.isPrimitive() || isWrapClass(clz)){
			return true;
		}
		return false;
	}
    public static boolean isOf(Class<?> orginType,Class<?> type){
    	return type.isAssignableFrom(orginType);
    }
    
    public static boolean isWrapClass(Class clz) {
		try {
			return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}
}