package test.mawujun.jpa;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mawujun 16064988
 * @createDate ：2018年11月30日 下午6:51:16
 */
public class Aaa {

	public static void main(String[] args) {
		//String hql="select * from (select * from aaaa where 1=1 order  by bbb) where 2=2 order by   id  ";
		String hql="select * from (select * from aaaa where 1=1 and a.bb=c.gg order  by bbb asc,a.ccc desc ) where 2=2 order by   id desc    ";
		//Pattern p = Pattern.compile("order\\s*by([\\s*|,].+(asc|desc|\\s*)*)+", Pattern.CASE_INSENSITIVE);
		//Pattern p = Pattern.compile("order\\s*by([\\s*|,].+(asc|desc|\\s*)*)+\\){1}", Pattern.CASE_INSENSITIVE);
		//匹配所有的order by子句
		String regex="order\\s+by\\s+([\\w*|\\w+\\.\\w+](asc|desc|\\s*){1},?)+";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		
	

		System.out.println(hql.matches(regex));
		System.out.println(hql.length());
		System.out.println(m.groupCount());
		while (m.find()) {
			System.out.println( " matches \"" + m.group(0) );
	        System.out.println("start:" + m.start() + " end:" + m.end());
	            
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		System.out.println(sb.toString());
		
		System.out.println("=============================================");
		m = p.matcher(hql);
		MatchResult ms = null;
        while (m.find()) {
            ms = m.toMatchResult();
           // System.out.print("匹配对象的组结果：");
            for (int i = 0; i < ms.groupCount(); i++) {
                //System.out.print(String.format("\n\t第%s组的结果是:%s",i+1,ms.group(i + 1)));
            	System.out.println(ms.group(i));
            }
            //System.out.print("\n匹配的整个结果:");
            //System.out.println(ms.group());
        }
		
		
//		// TODO Auto-generated method stub
//		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//		String dbURL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=test";
//		String userName = "sa";
//		String userPwd = "root";
//		try
//		{
//			Class.forName(driverName);
//			System.out.println("加载驱动成功！");
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("加载驱动失败！");
//		}
//
//		try {
//			Connection dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
//			System.out.println("连接数据库成功！");
//			DatabaseMetaData dmd = dbConn.getMetaData();
//			System.out.println("当前数据库是：" + dmd.getDatabaseProductName());
//			System.out.println("当前主版本：" + dmd.getDatabaseMajorVersion());
//			System.out.println("当前次要版本：" + dmd.getDatabaseMinorVersion());
//			System.out.println("当前数据库版本：" + dmd.getDatabaseProductVersion());
//			System.out.println("当前数据库驱动：" + dmd.getDriverVersion());
//			System.out.println("当前数据库URL：" + dmd.getURL());
//			System.out.println("当前数据库是否是只读模式？：" + dmd.isReadOnly());
//			System.out.println("当前数据库是否支持批量更新？：" + dmd.supportsBatchUpdates());
//			System.out.println("当前数据库是否支持结果集的双向移动（数据库数据变动不在ResultSet体现）？：" + dmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
//			System.out.println("当前数据库是否支持结果集的双向移动（数据库数据变动会影响到ResultSet的内容）？：" + dmd.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));
//			System.out.println("========================================");
//
//			
//		} catch (Exception e){
//			e.printStackTrace();
//			System.out.print("SQL Server连接失败！");
//		}
	}

}
