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
	/**�Ƿ���������*/
	public boolean isConnright()
	{
		return flag;
	}
	public Connection getConn()
	{
		try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		//���ݿ�����URL
		  String url="jdbc:mysql://"+ip+":3306/"+db;
		  //���ݿ��û���������
		  // String user="root";
		   //String key="123456";
		 //�������ݿ����ȡ��һ�����ݿ�����ֵ
		   connection =  (Connection) DriverManager.getConnection(url,user,key);
		}catch(Exception e){
			flag = false;
		}
	    return connection;
	}
}
