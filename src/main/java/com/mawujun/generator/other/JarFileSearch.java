package com.mawujun.generator.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mawujun.utils.FileUtils;

//http://hanxuedog.iteye.com/blog/1825723
//http://blog.csdn.net/hfhwfw/article/details/7266957
//https://www.cnblogs.com/0616--ataozhijia/p/4094952.html  逆向读取jar中的内容
//https://blog.csdn.net/sinat_34620530/article/details/54093862 解压文件
public class JarFileSearch {
	private static Logger logger=LoggerFactory.getLogger(JarFileSearch.class);
	
	/**
	 * 
	 * @param dir jar文件或某个目录下的所有jar
	 * @param pathprefix 指定开始的目录结构的
	 * @param suffix 解压指定文件后最的文件
	 * @return
	 */
	public static List<File> unzipJarFile(String dir , String pathprefix) {
		List<File> list=new ArrayList<File>();
		JarFileSearch.unzipJarFile(dir ,list, pathprefix, FileUtils.getTempDirectoryPath());
		return list;
	}
	
	public static List<File> unzipJarFile(String dir, String pathprefix,String targetDir) {
		List<File> list=new ArrayList<File>();
		JarFileSearch.unzipJarFile(dir, list, pathprefix, targetDir);
		return list;
	}
	/**
	 * 
	 * @param dir jar文件
	 * @param list 存放最后的文件路径的list
	 * @param suffix 后缀
	 * @param targetDir 解压的目录
	 * @return
	 */
	public static List<File> unzipJarFile(String dir,List<File> list,final String pathprefix,String targetDir) {  
		if(list==null){
    		list=new ArrayList<File>();
    	}
    	
        try {  
            File d = new File(dir);  
            File[] files = null;
            if (!d.isDirectory()) {  
                 if(d.getName().endsWith(".jar")||d.getName().endsWith(".zip")){
                	 files=new File[]{d};
                 } 
            }  else {
            	 files =d.listFiles(); 
            }
            
            for (int i = 0; i < files.length; i++) {  
                if (files[i].isDirectory()) {  
                	JarFileSearch.unzipJarFile(files[i].getAbsolutePath(), list, pathprefix,targetDir);  
                } else {  
                    String filename = files[i].getAbsolutePath();  
                    if (filename.endsWith(".jar")||filename.endsWith(".zip")) {  
                        ZipFile zip = new ZipFile(filename);  
                        Enumeration entries = zip.entries();  
                        while (entries.hasMoreElements()) {  
                            ZipEntry entry = (ZipEntry) entries.nextElement();  
                            String entry_name = entry.getName();  //templates/2.0/server/${simpleClassName}Controller.java.ftl
                            //用正则表达式来匹配，name是否合适，合适就加进来，以name为key，把文件写到其他地方去，然后再加载到到freemark中进行访问
                              
                            //不搜索扩展名为.class的文件  
                            String pathprefix__=pathprefix;
                            if(pathprefix.indexOf("/")==0) {
                            	pathprefix__=pathprefix.substring(1);
                            }
                            //entry_name.lastIndexOf(suffix)!=-1 &&
                            if( entry_name.indexOf(pathprefix__)==0){
                            	//InputStream inputStream=zip.getInputStream(entry);
                            	File file=new File(targetDir+entry.getName());
                            	
                            	if(entry.isDirectory()) {
                            		if(!file.exists()) {
                                		file.mkdirs();
                                	}
                            		continue;
                            	}
                            	
                            	//FileUtils.forceMkdirParent(file);
                            	
                            	list.add(file);
                            	FileOutputStream fos=new FileOutputStream(file);
                            	FileUtils.copyStream(zip.getInputStream(entry), fos);
                            	logger.info("解压文件到:"+file.getAbsolutePath());
//                                BufferedReader r = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));  
//
//                                while(r.read()!=-1){  
//                                    String tempStr = r.readLine();  
//                                    FileUtils.write(file, tempStr, "UTF-8",true);
////                                    if(null!=tempStr && tempStr.indexOf(condition)>-1){  
////                                        this.jarFiles.add(filename + "  --->  " + thisClassName);  
////                                        break;  
////                                    }  
//                                }  
                            }  
                              
 
                        }  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return null;
	}
	/**
	 * 
	 * @param dir 搜索的jar目录 或者 jar文件
	 * @param suffix
	 * @return
	 */
	public static List<ZipEntry> searchFile(String dir, String suffix) {  
		List<ZipEntry> list=new ArrayList<ZipEntry>();
		
		searchFile( dir,list, suffix.indexOf('.')!=-1?suffix:("."+suffix));
		return list;
	}
	
    protected static void searchFile(String dir, List<ZipEntry> list,String suffix) {  
    	if(list==null){
    		list=new ArrayList<ZipEntry>();
    	}
    	
        try {  
            File d = new File(dir);  
            File[] files = null;
            if (!d.isDirectory()) {  
                 if(d.getName().endsWith(".jar")||d.getName().endsWith(".zip")){
                	 files=new File[]{d};
                 } 
            }  else {
            	 files =d.listFiles(); 
            }
            
            for (int i = 0; i < files.length; i++) {  
                if (files[i].isDirectory()) {  
                	JarFileSearch.searchFile(files[i].getAbsolutePath(), list,suffix);  
                } else {  
                    String filename = files[i].getAbsolutePath();  
                    if (filename.endsWith(".jar")||filename.endsWith(".zip")) {  
                        ZipFile zip = new ZipFile(filename);  
                        Enumeration entries = zip.entries();  
                        while (entries.hasMoreElements()) {  
                            ZipEntry entry = (ZipEntry) entries.nextElement();  
                            String entry_name = entry.getName();  
                              
                            //不搜索扩展名为.class的文件  
                            if(entry_name.lastIndexOf(suffix)!=-1){
                            	//InputStream inputStream=zip.getInputStream(entry);
                            	list.add(entry);
 //                               BufferedReader r = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));  
//                                while(r.read()!=-1){  
//                                    String tempStr = r.readLine();  
//                                    if(null!=tempStr && tempStr.indexOf(condition)>-1){  
//                                        this.jarFiles.add(filename + "  --->  " + thisClassName);  
//                                        break;  
//                                    }  
//                                }  
                            }  
                              
 
                        }  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  

    }  

}
