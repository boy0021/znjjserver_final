package db;
import java.sql.DriverManager;

import com.mysql.jdbc.Connection;
public class ConnDB {
	private Connection connection = null;
	String ip;
	String db;
	String user;
	String key;
	boolean flag;
	public ConnDB(String ip,String db,String user,String key)
	{
		this.ip = ip;
		this.db = db;
		this.user = user;
		this.key = key;
		flag = true;
	}
	/**是否连接正常*/
	public boolean isConnright()
	{
		return flag;
	}
	public Connection getConn()
	{
		try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		//数据库连接URL
		  String url="jdbc:mysql://"+ip+":3306/"+db;
		  //数据库用户名和密码
		  // String user="root";
		   //String key="123456";
		 //根据数据库参数取得一个数据库连接值
		   connection =  (Connection) DriverManager.getConnection(url,user,key);
		}catch(Exception e){
			flag = false;
		}
	    return connection;
	}
}
