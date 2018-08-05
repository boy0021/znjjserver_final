package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mysql.jdbc.Connection;

public class MysqlConnManager {
	
	/**最大连接数*/
	private int maxConnNUM;
	/**当前正在使用的连接数量*/
	private int  ConnNum;
	/**锁对象*/
	private Lock lock ;
	/**ip*/
	private String ip;
	/**port*/
	private String port;
	/**user*/
	private String user;
	/**key*/
	private String key;
	/**DB名字*/
	private String dbname;
	
	
	
	
	/***/
	List <Connection> connectlist;
	public MysqlConnManager(String ip,String user,String key,String dbname,int maxConnNUM) {
		lock = new ReentrantLock();
		
		this.maxConnNUM = maxConnNUM;
		this.ip = ip;
		this.user = user;
		this.dbname = dbname;
		this.key = key;
		ConnNum = 0;
		connectlist = new ArrayList<Connection> ();
	}
	
	/**使用完了，归还连接*/
	public void backConnection(Connection  conn)
	{
		// 得到锁  
		lock.lock();
		connectlist.add(conn);
		ConnNum--;
		// 释放锁
		lock.unlock();
	}
	/**获得一个连接      如果没有就返回空*/
	public Connection  getConnection()
	{
		Connection conn = null;
		// 得到锁  
		lock.lock();
		if(connectlist.size()==0)
			addNewConnection();

		if(connectlist.size()>0)
		{
			ConnNum++;
			conn = connectlist.get(0);
			connectlist.remove(0);
		}
		// 释放锁
		lock.unlock();
		
		
		return conn;
	}
	
	/**建立新的连接*/
	public void  addNewConnection()
	{
		if(ConnNum < maxConnNUM)
		{
			Connection conndb = null;
			ConnDB t = null;
			try
			{
				//"121.42.199.164","ZNJJ","znjj","znjj123!"
				t = new ConnDB(ip,dbname,user,key);
				conndb = t.getConn();
			}
			catch( Exception e )
			{
				return ;
			}
			if (conndb!=null)
			{
				if (t.flag)
				{
					connectlist.add(conndb);
					ConnNum++;
				}
			}
		}
		System.out.println(ConnNum+" "+ip+" "+dbname+" "+user+" "+key);
	}
}
