package test.mawujun.jpa.gnerator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.mawujun.generator.code.GeneratorCodeService;

import freemarker.template.TemplateException;
import test.mawujun.model.City;
/**
 * 生成代码的主类，以ExtenConfig_�?头的类，是用来控制代码生成的，因为可能存在在不同的情况下，生成的代码会不�?样，有个性化的需求，但大部分�?样�??
 * 如果大部分都不样的话，就自己重写ftl文件
 * @author mawujun email:16064988@163.com qq:16064988
 *
 */
public class GeneratorCodeByTable {
	//static GeneratorCodeService generatorService=new GeneratorCodeService();

	public static void main(String[] args) throws TemplateException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {	
		//指定模板搜索路径
		GeneratorCodeService.generator("classpath*:/templates/2.0","d:/webapp-generator-output","com.mawujun","test_test");

	}
	


}
