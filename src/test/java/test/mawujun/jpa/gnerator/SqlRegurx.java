package test.mawujun.jpa.gnerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRegurx {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//先读取文件，放到string里面，
		String str=txt2String(new File("E:\\workspace\\leon-repository\\db\\script\\create-drop.sql"));
		//对文件进行正则分析
		//String regEx ="\\s*create\\s*table\\s*t_city\\s*\\({1}([\\w|\\s|\\(|\\)])*";
		//String regEx ="\\s*create\\s*table\\s*t_city\\s*\\({1}(\r\n{1}.*,{1})*";//缺少最后的primary key
		String regEx ="create\\s*table\\s*t_city\\s*\\([\\s\\S]+?;";
		
		// 编译正则表达式
	    Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(str);
	    
	   // 查找字符串中是否有匹配正则表达式的字符/字符串
	    //boolean rs = matcher.find();
	   // System.out.println(rs);
	    while( matcher.find()) {
	    	System.out.println(matcher.group()+"位置:"+matcher.start()+":"+matcher.end());
	    }
	    
		

	}
	
	public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }



}
