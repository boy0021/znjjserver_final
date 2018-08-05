package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mysql.jdbc.Connection;

public class MysqlConnManager {
	
	/**���������*/
	private int maxConnNUM;
	/**��ǰ����ʹ�õ���������*/
	private int  ConnNum;
	/**������*/
	private Lock lock ;
	/**ip*/
	private String ip;
	/**port*/
	private String port;
	/**user*/
	private String user;
	/**key*/
	private String key;
	/**DB����*/
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
	
	/**ʹ�����ˣ��黹����*/
	public void backConnection(Connection  conn)
	{
		// �õ���  
		lock.lock();
		connectlist.add(conn);
		ConnNum--;
		// �ͷ���
		lock.unlock();
	}
	/**���һ������      ���û�оͷ��ؿ�*/
	public Connection  getConnection()
	{
		Connection conn = null;
		// �õ���  
		lock.lock();
		if(connectlist.size()==0)
			addNewConnection();

		if(connectlist.size()>0)
		{
			ConnNum++;
			conn = connectlist.get(0);
			connectlist.remove(0);
		}
		// �ͷ���
		lock.unlock();
		
		
		return conn;
	}
	
	/**�����µ�����*/
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
