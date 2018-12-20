package test.mawujun.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mawujun.generator.other.JarFileSearch;

public class JarFileSearchTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<File> list=new ArrayList<File>();
		JarFileSearch.unzipJarFile("E:\\workspace\\leon-generator\\target\\leon-generator.jar", list,"templates/2.0","E:\\workspace\\leon-generator\\target\\");
//		String path="templates/2.0/server/${simpleClassName}Controller.java.ftl";
////		path=path.replaceAll("\\$", "\\$");
////		System.out.println(path);
////		path=path.replaceAll("\\{", "\\\\{").replace("\\}", "\\\\}");
////		System.out.println(path);
////		String pattern_str = "(templates/2.0/)^\\w*";
////		Pattern pattern = Pattern.compile(pattern_str);
////		
////		Matcher m = pattern.matcher(path);
////		System.out.println(m.find());
//		
//		if(path.lastIndexOf(".ftl")!=-1 && path.indexOf("templates/2.0")==0){
//			System.out.println("====================");
//		}

	}

}
