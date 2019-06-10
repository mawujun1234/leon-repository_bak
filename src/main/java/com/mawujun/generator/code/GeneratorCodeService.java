package com.mawujun.generator.code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mawujun.generator.other.JarFileSearch;
import com.mawujun.io.FileUtil;
import com.mawujun.lang.Assert;
import com.mawujun.util.StringUtils;
import com.mawujun.utils.PropertiesUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.utility.NullArgumentException;

/**
 * * com.mawujun.model:里面放的是模型类 com.mawujun.controller:里面放的是congtroller
 * com.mawujun.service: com.mawujun.repository:
 * 
 * @author mawujun
 *
 */
public class GeneratorCodeService {
	private static final Logger logger = LoggerFactory.getLogger(GeneratorCodeService.class);

	private static JavaEntityMetadataService javaEntityMetaDataService = new JavaEntityMetadataService();
	private static DbTableMetadataService dbTableMetadataService = new DbTableMetadataService();
	private static Configuration cfg = null;
	static List<FtlFileInfo> ftl_file_manes = new ArrayList<FtlFileInfo>();// ftl文件的名称

//	/**
//	 * 文件生成模式1 com.mawujun.model:里面放的是模型类 com.mawujun.controller:里面放的是congtroller
//	 * com.mawujun.service: com.mawujun.repository:
//	 */
//	public static final String file_model_one = "file_model_one";

	/**
	 * 默认的生成的基础包名
	 */
	private static String basepackage;
	
	private static String ftldir;// 再classpath路径下的ftl文件
	private static String ftlpath;// ftldir去掉classpath等内容后的地址
	private static String outputdir;// 输出的目录
	
	private static void initProperty() {
		PropertiesUtils utils=PropertiesUtils.load("generator.properties");
		ftldir = utils.getProperty("code.ftldir");
		outputdir = utils.getProperty("code.outputdir");
		basepackage = utils.getProperty("code.basepackage");
		Assert.notNull(outputdir,"generator.properties中code.outputdir不能为null");
		Assert.notNull(basepackage,"generator.properties中code.basepackage不能为null");
		//dbTableMetadataService.setBasepackage(basepackage);
		
	}

	/**
	 * 通过表名来生成代码
	 * @param tables
	 */
	public static void generator_(String... tables) {
		initProperty();
		generator(ftldir,outputdir,basepackage,tables);
	}
	
	/**
	 * 通过表名来生成代码
	 * @param ftldir 可以设置为null
	 * @param outputdir
	 * @param basepackage
	 * @param tablenames
	 */
	public static void generator(String ftldir,String outputdir,String basepackage,String... tablenames) {
		Assert.notNull(outputdir,"outputdir不能为null");
		Assert.notNull(basepackage,"basepackage不能为null");
		
		GeneratorCodeService.ftldir = ftldir;//utils.getProperty("code.ftldir");
		GeneratorCodeService.outputdir = outputdir;//utils.getProperty("code.outputdir");
		GeneratorCodeService.basepackage = basepackage;//utils.getProperty("code.basepackage");
		
		if(ftldir==null) {
			GeneratorCodeService.ftldir="classpath*:/template/2.0";
		}
		try {
			initConfiguration();
			
			FileUtil.mkParentDirs(outputdir);

			List<EntityTable> root = dbTableMetadataService.getTablesInfo(tablenames);
			for (EntityTable et : root) {
				dbTableMetadataService.convertTable2Class(basepackage,et);
				for (FtlFileInfo ftlFile : ftl_file_manes) {
					
					generatorFile(et, ftlFile, outputdir);
				}
			}
			// 打开文件夹
			Runtime.getRuntime().exec("cmd.exe /c start " + outputdir);
		} catch (Exception e) {
			logger.error("生成代码失败!", e);
		}
	}
	/**
	 * 记得在generator.properties中配置对应的参数:
	 * code.ftldir,code.outputdir,code.basepackage
	 */
	public static void generator(Class... clazzs) {
		initProperty();
		generator(ftldir,outputdir,basepackage,clazzs);
		
	}

	/**
	 * 
	 * @param ftldir 可以为null，模板文件的位置  1：classpath:/template/2.0 2：classpath*:/template/2.0 3：相对路径：相对本项目根目录的路径,/template/2.0
	 * @param outputdir 输出的目录。d:/webapp-generator-output
	 * @param basepackage 生成的类的基础包com.mawujun。文件生成模式1 com.mawujun.model:里面放的是模型类 com.mawujun.controller:里面放的是congtroller。com.mawujun.service: com.mawujun.repository:
	 * @param clazzs
	 */
	public static void generator(String ftldir,String outputdir,String basepackage,Class... clazzs) {
		System.out.println("生成路径是："+outputdir);
		Assert.notNull(outputdir,"outputdir不能为null");
		Assert.notNull(basepackage,"basepackage不能为null");
		
		GeneratorCodeService.ftldir = ftldir;//utils.getProperty("code.ftldir");
		GeneratorCodeService.outputdir = outputdir;//utils.getProperty("code.outputdir");
		GeneratorCodeService.basepackage = basepackage;//utils.getProperty("code.basepackage");
		
		if(ftldir==null) {
			GeneratorCodeService.ftldir="classpath*:/template/2.0";
		}
		try {
			initConfiguration();
			
			FileUtil.mkParentDirs(outputdir);

			for (Class cls : clazzs) {
				/* 创建数据模型 */
				EntityTable root = javaEntityMetaDataService.initClassProperty(cls);

				for (FtlFileInfo ftlFile : ftl_file_manes) {
					if(ftlFile.getName().equalsIgnoreCase("${entitySimpleClassName}.java.ftl") || ftlFile.getName().equalsIgnoreCase("${entitySimpleClassName}Id.java.ftl")) {
						continue;
					}
					generatorFile(root, ftlFile, outputdir);
				}
			}
			if(SystemUtils.IS_OS_WINDOWS) {
				// 打开文件夹
				Runtime.getRuntime().exec("cmd.exe /c start " + outputdir);
			} else if(SystemUtils.IS_OS_MAC) {
				Runtime.getRuntime().exec("open  " + outputdir);
			}
			
		} catch (Exception e) {
			logger.error("生成代码失败!", e);
		}

	}

	// 额外的配置选项
	// private ExtenConfig extenConfig;

	private static void initConfiguration() throws IOException {
		// TODO Auto-generated method stub
		if (cfg != null) {
			return;
		}

////		//加載多個文件
////		FileTemplateLoader ftl1 = new FileTemplateLoader(new File(basePath+"\\extjs4"));
////		FileTemplateLoader ftl2 = new FileTemplateLoader(new File(basePath+"\\java\\controller"));
////		FileTemplateLoader ftl3 = new FileTemplateLoader(new File(basePath+"\\java\\service"));
////		FileTemplateLoader ftl4 = new FileTemplateLoader(new File(basePath+"\\java\\mybatis"));
////		TemplateLoader[] loaders = new TemplateLoader[] { ftl1, ftl2,ftl3,ftl4 };
////		MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
////		cfg.setTemplateLoader(mtl);
//		
//		PathMatchingResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
//		Resource[] reses= resolver.getResources("classpath:templates/ftl1/**/*.ftl");
////		String basePath=this.getClass().getResource("/").toString();
////		Resource[] reses= resolver.getResources("file:"+1+"/templates/**/*.ftl");
//		if(reses==null || reses.length==0){
//			return ;
//		}
		
		if (ftldir == null) {
			throw new RuntimeException("ftldir未指定，可以通过代码指定。也可以通过generator.properties中的code.ftldir属性指定。");
		}

		List<File> files = null;
		if (ftldir.startsWith("classpath:")) {
			ftlpath = ftldir.substring("classpath:".length());
			files = FileUtil.findFiles(FileUtil.getClassRootPath(GeneratorCodeService.class) + ftlpath, "*.ftl");

		} else if (ftldir.startsWith("classpath*:")) {
			ftlpath = ftldir.substring("classpath*:".length());
			files = FileUtil.findFiles(FileUtil.getClassRootPath(GeneratorCodeService.class) + ftlpath, "*.ftl");
			if (files == null || files.size() == 0) {
				// String basePath=this.getClass().getResource("").getPath().toString();
				// GeneratorService.class.getProtectionDomain().getCodeSource().getLocation().getFile()获取jar文件
				// 因为是开发环境，leon-generator是直接存在的，所以直接到leon-generator中获取默认的模板文件了
				// String path =
				// GeneratorService.class.getProtectionDomain().getCodeSource().getLocation().getFile()+classpath;
				// files=FileUtil.findFiles(path, "*.ftl");

				URL url = FileUtil.getJarPath(GeneratorCodeService.class);
				if ("file".equals(url.getProtocol()) && url.getFile().indexOf(".jar!") == -1) {
					//File classpath_file = new File(url.getFile() + "/../../src/main/resources".replaceAll("\\\\/", File.separator) + ftlpath);
					File classpath_file = new File(url.getFile().substring(0,url.getFile().length()-16)+File.separator+"src"+File.separator+"main"+File.separator+"resources" + ftlpath);
					files = FileUtil.findFiles(classpath_file.getAbsolutePath(), "*.ftl");

					System.out.println("查找路径："+classpath_file);
				} else {
					String jarpath=FileUtil.getJarAbstractPath(GeneratorCodeService.class);
					//jarpath="E:\\workspace\\leon-generator\\target\\leon-generator.jar";
					//把文件内容写到临时目录中
					files=JarFileSearch.unzipJarFile(jarpath, ftlpath, ".ftl");

				}

				// 如果是直接依赖于leon-generator.jar,而没有leon-generator这个项目存在的时候，要先获取到ftl文件所在的jar，然后通过
				// JarFileSearch专门搜索这个jar中的ftl文件，然后读取出来，然后再写到当前项目的classpath中，加上ftldir的前缀
				// 然后再把这些模板文件读取出来，用来生成代码。

//				
//				String dir=FileUtil.getCurrentClassPath(this)+"../lib";
//				ArrayList<InputStream> list=new ArrayList<InputStream>();
//				JarFileSearch.searchFtl(dir, list);
			}
		} else {
			// 从绝对路径中获取
			// 从当前项目的相对路径中获取
			ftlpath = ftldir;
			files = FileUtil.findFiles(FileUtil.getProjectPath() + ftlpath, "*.ftl");

		}
		if (files == null || files.size() == 0) {
			throw new RuntimeException("没有找到ftl文件");
		}

		// ftl_file_manes=files;
		// cfg = new Configuration();
		cfg = new Configuration(Configuration.getVersion());
		cfg.setEncoding(Locale.CHINA, "UTF-8");
		// cfg.setEncoding(Locale.CHINA, "UTF-8");

		// 循环出 所有包含ftl的文件夹
		Set<String> list = new HashSet<String>();
		List<TemplateLoader> templateLoaders = new ArrayList<TemplateLoader>();
		for (File file : files) {
			// System.out.println(res.getURI().getPath());
			// System.out.println(res.getURL().getPath());
			// String
			// path=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf('/'));//SystemUtils.FILE_SEPARATOR
			String path = file.getParent();
			if (!list.contains(path)) {
				list.add(path);
				FileTemplateLoader ftl1 = new FileTemplateLoader(new File(path));
				templateLoaders.add(ftl1);
			}
			// ftl_file_manes.add(file.getName());

			FtlFileInfo ftlFileInfo = new FtlFileInfo();
			ftlFileInfo.setName(file.getName());
			ftlFileInfo.setParentpath(file.getParent());
			ftl_file_manes.add(ftlFileInfo);

		}
//		String path=reses[0].getURI().getPath().substring(0,reses[0].getURI().getPath().indexOf("templates")+9);
//		FileTemplateLoader ftl1 = new FileTemplateLoader(new File(path));
//		templateLoaders.add(ftl1);

		MultiTemplateLoader mtl = new MultiTemplateLoader(
				templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]));
		cfg.setTemplateLoader(mtl);

		cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.getVersion()));

	}


	/**
	 * 根据字符串产生名称
	 * 
	 * @param clazz
	 * @param ftl
	 * @return
	 * @throws ClassNotFoundException
	 * @throws TemplateException
	 * @throws IOException
	 */
	private static String generatorFileName(EntityTable root, String ftl)
			throws ClassNotFoundException, TemplateException, IOException {

		//EntityTable root = javaEntityMetaDataService.getClassProperty(clazz);
		if (basepackage != null) {
			root.setBasepackage(basepackage);
		}
//		if(this.getExtenConfig()!=null){
//			root.setExtenConfig(this.getExtenConfig());
//		}
		if (root == null) {
			throw new NullPointerException("SubjectRoot为null");
		}

		String fileName = FreemarkerHelper.processTemplateString(ftl, root, cfg);
		fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		return fileName;
	}


	/**
	 * 
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @param clazz       领域类
	 * @param ftl         模板文件的名称
	 * @param dirPath     生成后文件存放的地址
	 * @param extenConfig 额外的属性，用来控制生成的代码的
	 * @throws TemplateException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static void generatorFile(EntityTable root, FtlFileInfo ftlfile, String dirPath)
			throws TemplateException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		initConfiguration();
		/* 创建数据模型 */
		//EntityTable root = javaEntityMetaDataService.initClassProperty(clazz);
//		if(this.getExtenConfig()!=null){
//			root.setExtenConfig(this.getExtenConfig());
//		}

		String fileName = generatorFileName(root, ftlfile.getName());
//		//按照模板的目录结构生成
//		if(ftldir==null) {
//			ftldir=PropertiesUtils.load("generator.properties").getProperty("ftldir");
//
//		}
//		String parentpath=ftlfile.getParentpath();
//		
//		parentpath=parentpath.replaceAll("\\\\/", File.separator);
//		if(parentpath.indexOf(ftldir)==-1){
//			throw new IllegalPathStateException("路径错误，请注意是window还是linux平台");
//		}
//		String ftlfilepath=parentpath.substring(parentpath.indexOf(ftldir)+ftldir.length());//   .replaceAll(ftldir, "");
//		
//		
//		String fileDir=dirPath+ftlfilepath;
//		String filePath=dirPath+ftlfilepath+File.separatorChar+fileName;

		String ftlfilepath=ftlfile.getParentpath();
		if(SystemUtils.IS_OS_WINDOWS) {
			if(ftlfilepath.indexOf(ftlpath.replace('/', '\\'))!=-1) {
				//System.out.println(ftlfilepath.substring(ftlfilepath.indexOf(ftlpath.replace('/', '\\'))+ftlpath.length()+1));
				ftlfilepath=ftlfilepath.substring(ftlfilepath.indexOf(ftlpath.replace('/', '\\')));
			}
		} else {
			if(ftlfilepath.indexOf(ftlpath.replace('\\', '/'))!=-1) {
				//System.out.println(ftlfilepath.substring(ftlfilepath.indexOf(ftlpath.replace('/', '\\'))+ftlpath.length()+1));
				ftlfilepath=ftlfilepath.substring(ftlfilepath.indexOf(ftlpath.replace('\\', '/')));
			}
		}
		
		
		// 生成文件的目录地址
		String fileDir = dirPath + ftlfilepath;
		String filePath = fileDir + File.separatorChar + fileName;

		File dir = new File(fileDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(filePath);
		if (file.exists()) {
//			int i=filePath.lastIndexOf("/");
//			if(i==-1){
//				i=filePath.lastIndexOf("\\");
//			}
//			Runtime.getRuntime().exec("cmd.exe /c start "+filePath.substring(0, i));
//			throw new FileExistsException(file);

		} else {
			// filePath.
			// filePath.substring(beginIndex,
			// endIndex)filePath.lastIndexOf(File.separatorChar);
			file.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(filePath);
		generator(root, ftlfile.getName(), fileWriter);
	}

	/**
	 * 
	 * @param clazz 要
	 * @param ftl   模板文件在的地方
	 * @throws TemplateException
	 * @throws IOException
	 */
	private static void generator(EntityTable root, String ftl, Writer writer) throws TemplateException, IOException {
		if (!StringUtils.hasLength(ftl)) {
			throw new NullArgumentException("模板文件名称不能为null");
		}

		// String basePath=System.getProperty("user.dir");

		/* 在整个应用的生命周期中，这个工作你可以执行多次 */
		/* 获取或创建模板 */
		Template templete = cfg.getTemplate(ftl, "UTF-8");
		// templete.setEncoding("UTF-8");
		// templete.setOutputEncoding("UTF-8");
		/* 创建数据模型 */
		//EntityTable root = javaEntityMetaDataService.getClassProperty(clazz);
//		if(this.getExtenConfig()!=null){
//			root.setExtenConfig(this.getExtenConfig());
//		}

		templete.process(root, writer);
		// out.flush();
		// return out;
	}

	

}
