package com.mawujun.generator.ddl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Selectable;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mawujun.generator.annotation.ColDefine;
import com.mawujun.generator.annotation.FK;
import com.mawujun.generator.annotation.TableDefine;
import com.mawujun.io.FileUtil;
import com.mawujun.lang.Assert;
import com.mawujun.util.ReflectUtil;
import com.mawujun.util.StringUtils;
import com.mawujun.utils.PropertiesUtils;

public class GenerateDDLService {
	private final static Logger logger = LoggerFactory.getLogger(GenerateDDLService.class);

	/**
	 * 通过在配置文件中配置packageToScan和outputFile
	 */
	public static void generateDLL() {
		PropertiesUtils utils = PropertiesUtils.load("generator.properties");
		String packageToScan = utils.getProperty("packageToScan");
		String outputFile = utils.getProperty("ddl.outputFile");
		Assert.notNull(packageToScan, "generator.properties中packageToScan不能为null");
		Assert.notNull(packageToScan, "generator.properties中ddl.outputFile不能为null");
		GenerateDDLService.generateDLL(packageToScan, outputFile);
	}
	
	private static Metadata metadata =null;

	/**
	 * GenerateDDLService.generateDLL("test.mawujun","/db/script");
	 * 
	 * @param packageToScan 包名，以逗号分隔
	 * @param outputFile    输出目录，相对于本项目根目录的位置
	 */
	public static void generateDLL(String packageToScan, String outputFile) {
		File file = generateCfg(packageToScan);
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure(file).build();
		metadata = new MetadataSources(serviceRegistry).buildMetadata();

		assignCommentAndDefault(metadata);

		SchemaExport export = new SchemaExport();
		String path = FileUtil.getProjectPath() + outputFile + File.separator + "create-drop.sql";
		File aaaa = new File(path);
		if (aaaa.exists()) {
			aaaa.delete();
		}
		export.setOutputFile(path);
		// export.setOutputFile("E:\\my-workspace\\leon-repository\\src\\test\\java\\test\\mawujun\\jpa\\gnerator\\aaaa.sql");
		export.setDelimiter(";");
		export.setFormat(true);

		// export.drop(EnumSet.of(TargetType.SCRIPT), metadata);
		// export.createOnly(EnumSet.of(TargetType.SCRIPT),metadata);
		

		export.create(EnumSet.of(TargetType.SCRIPT), metadata);
		System.exit(0);
	}

	private static File generateCfg(String packageToScan) {
		Set<Class> entitys = new HashSet<Class>();
		try {

			for (String pkg : packageToScan.split(",")) {
				entitys.addAll(getClassName(pkg));
			}

		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error("搜索实体类失败！", e1);
		}
		if (entitys == null || entitys.size() == 0) {
			throw new RuntimeException("没有找到实体类");
		}

		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		builder.append(
				"<!DOCTYPE hibernate-configuration PUBLIC \"-//Hibernate/Hibernate Configuration DTD 3.0//EN\" \"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">");
		builder.append("<hibernate-configuration>");
		builder.append("<session-factory>");
//		builder.append("<property name=\"hibernate.connection.driver_class\">com.mysql.jdbc.Driver</property>");
//		builder.append("<property name=\"hibernate.connection.url\">jdbc:mysql://172.16.80.252:3306/test</property>");
//		builder.append("<property name=\"hibernate.connection.username\">root</property>");
//		builder.append("<property name=\"hibernate.connection.password\">aikucun2018</property>");
		builder.append("<property name=\"hibernate.dialect\">org.hibernate.dialect.MySQL57Dialect</property>");
		builder.append(
				"<property name=\"hibernate.physical_naming_strategy\">org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy</property>");
		builder.append("");
		builder.append("");
		// builder.append("<mapping class=\"test.mawujun.model.City\" />");
		for (Class clazz : entitys) {
			builder.append("<mapping class=\"" + clazz.getName() + "\" />");
		}
		builder.append("</session-factory>");
		builder.append("</hibernate-configuration>");
		builder.append("");
		builder.append("");
		builder.append("");
		builder.append("");
		builder.append("");

		File file = new File(FileUtil.getTempDirectory() + File.separator + "hibernate.cfg.xml");
		logger.info("cfg文件生成地址为:" + file.getAbsolutePath());
		System.out.println(file.getAbsolutePath());
		//try {
			FileUtil.writeString(builder.toString(), file, Charset.forName("UTF-8"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			logger.error("生成文件失败！", e);
//
//		}
		return file;

	}

	public static void assignCommentAndDefault(Metadata metadata) {
//		Table table=metadata.getEntityBinding("test.mawujun.model.City").getIdentityTable();
//		metadata.getEntityBinding("test.mawujun.model.City").getTable();
//		metadata.getDatabase().getNamespaces().iterator().next().getTables().iterator().next().getColumn(1);

		System.out.println(metadata.getEntityBinding("test.mawujun.model.City").getProperty("name").getValue());
		Collection<PersistentClass> entity = metadata.getEntityBindings();
		for (PersistentClass pc : entity) {
			// Iterator
			// property_terator=pc.getPropertyIterator();//pc.getProperty(propertyName)
			// property_terator.next()
			Table table = pc.getTable();
			Iterator columns_terator = table.getColumnIterator();
			while (columns_terator.hasNext()) {
				Iterator<Property> property_terator = pc.getPropertyIterator();
				final Column col = (Column) columns_terator.next();
				System.out.println(col.getName());
				System.out.println(col.getCanonicalName());
				while (property_terator.hasNext()) {
					Property prop = property_terator.next();
					System.out.println("------" + prop.getName());
					Iterator<Selectable> prop_col_itr = prop.getColumnIterator();// .getValue().getColumnIterator();
					while (prop_col_itr.hasNext()) {
						Selectable select = prop_col_itr.next();
						if (select.getText().equals(col.getName())) {
							// System.out.println(pc.getProperty("_identifierMapper"));
							System.out.println(pc.getIdentifierProperty());
							assignColCommentAndDefaultvalue(pc.getMappedClass(), prop.getName(), col);
						}
					}

				}
			}

			assignCommentAndefault(table, pc);
		}

		System.out.println("=======================================================");
	}

	private static void assignCommentAndefault(Table table, PersistentClass pc) {
		assignTableCommentAndFks(pc.getMappedClass(),table);
		// 循环主键
		PrimaryKey pk = table.getPrimaryKey();
		List<Column> pk_list = pk.getColumns();
		if (pc.getIdentifier() instanceof Component) {
			Component kv = (Component) pc.getIdentifier();

			for (Column col : pk_list) {
				Iterator<Property> property_terator = kv.getPropertyIterator();
				while (property_terator.hasNext()) {
					Property prop = property_terator.next();
					Iterator<Selectable> prop_col_itr = prop.getColumnIterator();

					while (prop_col_itr.hasNext()) {
						Selectable select = prop_col_itr.next();
						if (select.getText().equals(col.getName())) {
							// System.out.println(pc.getProperty("_identifierMapper"));
							System.out.println(pc.getIdentifierProperty());
							//简单粗暴的处理两种情况，并且具有优先级
							assignColCommentAndDefaultvalue(kv.getComponentClass(), prop.getName(), col);
							assignColCommentAndDefaultvalue(pc.getMappedClass(), prop.getName(), col);
						}
					}
				}
			}

		} else {
			SimpleValue kv = (SimpleValue) pc.getIdentifier();
			for (Column col : pk_list) {
				// 简单粗暴的处理了,兼容id直接设置为驼峰形式和下划线形式
				assignColCommentAndDefaultvalue(pc.getMappedClass(), StringUtils.underlineToCamel(col.getName()), col);
				assignColCommentAndDefaultvalue(pc.getMappedClass(), col.getName(), col);
			}
		}

		for (Column col : pk_list) {
//			Iterator kv_terator=kv.getPropertyIterator();
//			while ( kv_terator.hasNext() ) {
//				Selectable select=(Selectable)kv_terator.next();
//				if(select.getText().equals(col.getName())) {
//					assignCommentAndDefaultvalue(kv.getComponentClass(),select.getText(),col);
//				}
//				
//			}
		}
	}
	
	private static void assignTableCommentAndFks(Class clazz,Table table) {
		TableDefine tableDefine=(TableDefine)clazz.getAnnotation(TableDefine.class);
		if(tableDefine==null) {
			return;
		}
		if(StringUtils.hasText(tableDefine.comment())) {
			table.setComment(tableDefine.comment());
		}
		FK[] fks=tableDefine.fks();
		if(fks==null || fks.length==0) {
			return;
		}
		
		for(FK fk:fks) {
			List<Column> keyColumns=new ArrayList<Column>();
			for(String name:fk.columnNames()) {
				Identifier iden=Identifier.toIdentifier(name);
				if(table.getColumn(iden)==null) {
					throw new RuntimeException("当前指定的列不存在");
				}
				keyColumns.add(table.getColumn(iden));
			}
			
			List<Column> referencedColumns=new ArrayList<Column>();
			String referencedEntityName=fk.refEntity().getName();
			Table referencedTable=metadata.getEntityBinding(referencedEntityName).getTable();
			
			for(String name:fk.refColumnNames()) {
				Identifier iden=Identifier.toIdentifier(name);
				if(referencedTable.getColumn(iden)==null) {
					throw new RuntimeException("被引用的表不存在列:"+name);
				}
				referencedColumns.add(referencedTable.getColumn(iden));
				
//				List<Column> pks=table.getPrimaryKey().getColumns();
//				for(Column pk:pks) {
//					if(pk.getCanonicalName().equals(iden.getCanonicalName())) {
//						referencedColumns.add(pk);
//					}
//				}
				
			}
			
			if(referencedColumns==null || referencedColumns.size()==0) {
				throw new RuntimeException("没有找到关联表的主键列或唯一列");
			}
			
			//keyDefinition应该是和colDefinition一样，自己定义的东西
			ForeignKey foreignKey=table.createForeignKey("fk_"+StringUtils.randomStr(8), 
					keyColumns, 
					referencedEntityName, null,  
					referencedColumns);
			foreignKey.setReferencedEntityName(referencedEntityName);
			foreignKey.setReferencedTable(referencedTable);
		}
		
		
	}

	private static void assignColCommentAndDefaultvalue(Class clazz, String property, Column col) {
		Field field = ReflectUtil.getField(clazz, property);
		System.out.println(property + "......");
		if (field == null) {
			return;
		}
		ColDefine colDefine = (ColDefine) field.getAnnotation(ColDefine.class);
		if (colDefine != null) {
			// String[] aa=new String[2];
			if (StringUtils.hasText(colDefine.comment())) {
				col.setComment(colDefine.comment());
			}
			if (StringUtils.hasText(colDefine.defaultValue())) {
				col.setDefaultValue(colDefine.defaultValue());
			}

		}
	}

	/**
	 * 获取某包下所有类
	 *
	 * @param packageName  包名
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static Set<Class> getClassName(String packageName) throws IOException, ClassNotFoundException {
		Set<Class> fileNames = new HashSet<Class>();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String packagePath = packageName.replace(".", File.separator);
		Enumeration<URL> urls = loader.getResources(packagePath);
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if (url == null)
				continue;
			String type = url.getProtocol();
			if (type.equals("file")) {
				fileNames.addAll(getClassNameByFile(URLDecoder.decode(url.getPath(), "UTF-8"), true));
			} else if (type.equals("jar")) {
				// fileNames.addAll(getClassNameByJar(url.getPath()));
			}
		}
		// fileNames.addAll(getClassNameByJars(((URLClassLoader) loader).getURLs(),
		// packagePath, childPackage));
		return fileNames;
	}

	/**
	 * 从项目文件获取某包下所有类
	 *
	 * @param filePath     文件路径 类名集合
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 * @throws UnsupportedEncodingException
	 * @throws ClassNotFoundException
	 */
	private static Set<Class> getClassNameByFile(String filePath, boolean childPackage)
			throws UnsupportedEncodingException {
		Set<Class> myClassName = new HashSet<Class>();
		// filePath = UrlDecode.getURLDecode(filePath);
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		if (childFiles == null)
			return myClassName;
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				if (childPackage) {
					myClassName.addAll(getClassNameByFile(childFile.getPath(), childPackage));
				}
			} else {
				String childFilePath = childFile.getPath();
				// childFilePath = FileUtil.clearPath(childFilePath);
				if (childFilePath.endsWith(".class")) {
					if (childFilePath.indexOf(File.separator + "test-classes" + File.separator) != -1) {
						childFilePath = childFilePath.substring(
								childFilePath.indexOf(File.separator + "test-classes" + File.separator) + 14,
								childFilePath.lastIndexOf("."));
					} else {
						childFilePath = childFilePath.substring(
								childFilePath.indexOf(File.separator + "classes" + File.separator) + 9,
								childFilePath.lastIndexOf("."));
					}

					childFilePath = childFilePath.replace(File.separator, ".");
					Class clazz = null;
					try {
						clazz = Class.forName(childFilePath);
						Annotation entity = clazz.getAnnotation(Entity.class);
						if (entity != null) {
							myClassName.add(clazz);
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error("找不到类", e);
					}

				}
			}
		}
		return myClassName;
	}

//    /**
//     * 从jar获取某包下所有类
//     *
//     * @param jarPath      jar文件路径
//     * @param childPackage 是否遍历子包
//     * @return 类的完整名称
//     * @throws UnsupportedEncodingException
//     */
//    private static Set<Class> getClassNameByJar(String jarPath, boolean childPackage) throws UnsupportedEncodingException {
//    	Set<Class> myClassName = new HashSet<Class>();
//        String[] jarInfo = jarPath.split("!");
//        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
//        jarFilePath = UrlDecode.getURLDecode(jarFilePath);
//        String packagePath = jarInfo[1].substring(1);
//        try {
//            JarFile jarFile = new JarFile(jarFilePath);
//            Enumeration<JarEntry> entrys = jarFile.entries();
//            while (entrys.hasMoreElements()) {
//                JarEntry jarEntry = entrys.nextElement();
//                String entryName = jarEntry.getName();
//                if (entryName.endsWith(".class")) {
//                    if (childPackage) {
//                        if (entryName.startsWith(packagePath)) {
//                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
//                            myClassName.add(jarEntry.);
//                        }
//                    } else {
//                        int index = entryName.lastIndexOf("/");
//                        String myPackagePath;
//                        if (index != -1) {
//                            myPackagePath = entryName.substring(0, index);
//                        } else {
//                            myPackagePath = entryName;
//                        }
//                        if (myPackagePath.equals(packagePath)) {
//                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
//                            myClassName.add(entryName);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            //SystemLog.Log(LogType.systemInfo, e.getMessage(), e);
//        }
//        return myClassName;
//    }
//
//    /**
//     * 从所有jar中搜索该包，并获取该包下所有类
//     *
//     * @param urls         URL集合
//     * @param packagePath  包路径
//     * @param childPackage 是否遍历子包
//     * @return 类的完整名称
//     * @throws UnsupportedEncodingException
//     */
//    private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) throws UnsupportedEncodingException {
//        List<String> myClassName = new ArrayList<String>();
//        if (urls != null) {
//            for (int i = 0; i < urls.length; i++) {
//                URL url = urls[i];
//                String urlPath = url.getPath();
//                // 不必搜索classes文件夹
//                if (urlPath.endsWith("classes/")) {
//                    continue;
//                }
//                String jarPath = urlPath + "!/" + packagePath;
//                myClassName.addAll(getClassNameByJar(jarPath, childPackage));
//            }
//        }
//        return myClassName;
//    }

}
