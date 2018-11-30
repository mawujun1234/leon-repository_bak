package test.mawujun.jpa;

import java.sql.Connection;
import java.sql.DriverManager;

/**
* @author mawujun 16064988
* @createDate ：2018年11月30日 下午6:51:16
*/
public class Aaa {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";

		 String dbURL="jdbc:sqlserver://127.0.0.1:1434;DatabaseName=test";

		 String userName="sa";

		 String userPwd="root";

		try

		{

		   Class.forName(driverName);

		   System.out.println("加载驱动成功！");

		}catch(Exception e){

		   e.printStackTrace();

		   System.out.println("加载驱动失败！");

		}

		try{

		   Connection dbConn=DriverManager.getConnection(dbURL,userName,userPwd);

		       System.out.println("连接数据库成功！");

		}catch(Exception e)

		{

		   e.printStackTrace();

		   System.out.print("SQL Server连接失败！");

		}   
	}

}
