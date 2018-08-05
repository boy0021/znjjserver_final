package server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;

import utl.WriteLog;

import com.mysql.jdbc.Connection;

import config.Config;

import db.MysqlConnManager;

import model.MyPosition;
import model.Order;
import model.Road;
import model.Station;

public class StationServer {
	/**��ʵӦ����myroadlist*/
	List<Road> myorderlist;

	/**���ݲ�ѯ��ع�����*/
	MysqlConnManager mysqlConnManager;
	/**�����¼��־*/
	WriteLog myWriteLog;
	
	/**�õ�����վ������*/
	public List<MyPosition> getStationsPosition()
	{
		List<MyPosition> tempMyPosition = new ArrayList<MyPosition>();
		
		for (int i = 0;i<myorderlist.size();i++)
		{
			List<Station> tempmystationlist = myorderlist.get(i).mystationlist;
			
			for (int j = 0;j<tempmystationlist.size(); j++)
			{
				MyPosition myPosition = new MyPosition();
				myPosition.lat = tempmystationlist.get(j).lat;
				myPosition.lon = tempmystationlist.get(j).lon;
				tempMyPosition.add(myPosition);
			}
		}
		return tempMyPosition;
	}
	
	public StationServer(HttpServlet myHttpServlet) {
		mysqlConnManager = new MysqlConnManager(Config.DBIP, Config.DBUser, 
		Config.DBKey, Config.DBname,  Config.CODEmaxConnNUM);
		myorderlist = new ArrayList<Road>();
		
		myWriteLog = new WriteLog(myHttpServlet,"StationServer");
		
		myWriteLog.log("StationServer start");
	}
	
	@Override
	public String toString() {
		String mes = "";
		mes += ("roadlist_num: "+myorderlist.size()+";");
		Iterator <Road> it = myorderlist.iterator();  
		while(it.hasNext())  
		{  
			mes += (it.next().toString()+",");
		} 
		return mes;
	}
	
	/**��·��id �õ�����*/
	public Road getRoadFromId(int roadid) 
	{
		for (int i = 0;i<myorderlist.size();i++)
		{
			int temproadid = myorderlist.get(i).roadid;
			
			if (temproadid==roadid)
			{
				return  myorderlist.get(i);
			}
		}
		return null;
	}
	/**��·��id,վ��id �õ�վ�Ķ���
	public Station getRoadFromStationId(String  codestring ) 
	{
		Station myStation = null;
		for (int i = 0;i<myorderlist.size();i++)
		{
			for (int j = 0;j<myorderlist.get(i).mystationlist.size();j++)
			{
				if(codestring.equals(myorderlist.get(i).mystationlist.get(j).twoDimensionalCode))
				{
					myStation = myorderlist.get(i).mystationlist.get(j);
					return myStation;
				}
			}
		}
		return myStation;
	}*/
	
	/**��·�Ķ�ά�� �õ�վ�Ķ���*/
	public Station getStationFromcodestring(String  codestring ) 
	{
		Station myStation = null;
		for (int i = 0;i<myorderlist.size();i++)
		{
			for (int j = 0;j<myorderlist.get(i).mystationlist.size();j++)
			{
				if(codestring.equals(myorderlist.get(i).mystationlist.get(j).twoDimensionalCode))
				{
					myStation = myorderlist.get(i).mystationlist.get(j);
					System.out.println(";;;;;;;;;;;;;;;;;;;;:"+codestring);
					return myStation;
				}
			}
		}
		return myStation;
	}
	/**��·��id,վ��id �õ�վ�Ķ���*/
	public Station getStation(int roadid ,int stationid) 
	{
		Road tempRoad =  getRoadFromId( roadid);
		if (tempRoad!=null)
		{
			return tempRoad.getStation( stationid);
		}
		return null;
	}
	
	/**�����ݿ��е�����ȡ��������ķŵ��ڴ��У�Road��Station��*/
	public void inidata()
	{
		Connection myConnection = null;
		while (myConnection==null)
		{
			myConnection = mysqlConnManager.getConnection();
			if (myConnection!=null)
				break;
			 try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try 
		{
			ResultSet rs = myConnection.createStatement().executeQuery(Config.sql_select_roadids);
			
			if (rs != null)
			{
				while(rs.next())
			   {
					Road tempRoad = new Road();
					//roadid
					int roadid = rs.getInt("roadid");
					tempRoad.roadid = roadid;
					ResultSet temprs = myConnection.createStatement().executeQuery(Config.sql_select_stations1+roadid+Config.sql_select_stations2);
					if (temprs != null)
					{
						while(temprs.next())
					   {
							//Ψһid
							int station_id = temprs.getInt("station_id");
							//γ��
							double lat  = temprs.getDouble("lat");
							//����
							double lon = temprs.getDouble("lon");
							//��������
							String namevarchar = temprs.getString("name");
							//��ά��code
							String twoDimensionalCode = temprs.getString("twoDimensionalCode");
							
							Station myStation = new Station();
							myStation.stationid = station_id;
							myStation.lat = lat;
							myStation.lon = lon;
							myStation.roadid = roadid;
							myStation.name = namevarchar;
							myStation.twoDimensionalCode = twoDimensionalCode;
							tempRoad.add(myStation);
					   }
					}
					//myWriteLog.log("--------"+tempRoad.toString()); 
					myorderlist.add(tempRoad);
					
			   }
			}
			mysqlConnManager.backConnection(myConnection);
		} 
		catch (Exception e) 
		{
		}	
	}
	/**��վ��id �õ�����վ֮�������վ��list����������վ*/
	public List<Integer> getTwoStation(int stationidstart,int stationidend,int roadid) 
	{
		/**
		 * ��ʱ��������Ȧ�����⣬��Ȼ���ڲ�����Ȼ��Ӱ��
		 * */
		List<Integer> templist = new ArrayList<Integer>();
		
		List<Integer> templiststart = new ArrayList<Integer>();
		for (int i = 0;i<myorderlist.size();i++)
		{
			if (myorderlist.get(i).roadid==roadid)
			{
				for (int j = 0;j<myorderlist.get(i).mystationlist.size();j++)
				{
					int tempid = myorderlist.get(i).mystationlist.get(j).stationid;
					if (stationidend<stationidstart)
					{//��Ȧ
						if (tempid >= stationidstart )
						{
							templist.add(tempid);
						}
					}
					else
					{//��һȦ��
						if (tempid >= stationidstart && tempid <= stationidend)
						{
							templist.add(tempid);
						}
					}
					
					templiststart.add(tempid);
				}
			}
		}
		
		if (stationidend<stationidstart)
		{
			for (int i = 0;i<templiststart.size();i++)
			{
				if (templiststart.get(i)<=stationidend)
				{
					templist.add(templiststart.get(i));
				}
			}
		}
		return templist;
	}
	
	/**��վ��id �õ�·��id*/
	public int getRoadIdFromStationId(int stationid) 
	{
		for (int i = 0;i<myorderlist.size();i++)
		{
			for (int j = 0;j<myorderlist.get(i).mystationlist.size();j++)
			{
				if (myorderlist.get(i).mystationlist.get(j).stationid == stationid)
				{
					return myorderlist.get(i).roadid;
				}
			}
		}
		return -1;
	}
}
