package model;

import java.util.ArrayList;
import java.util.List;

public class Road {
	/**·��*/
	public int roadid;
	public List<Station> mystationlist;
	
	/***/
	public Road() {
		mystationlist = new ArrayList<Station>();
	}
	
	/**����վ��˳�����ӳ�վ*/
	public void add(Station station)
	{
		mystationlist.add(station);
	}
	
	/**��·��id,վ��id �õ�վ�Ķ���*/
	public Station getStation(int stationid) 
	{
		for (int i = 0;i<mystationlist.size();i++)
		{
			int tempstationid = mystationlist.get(i).stationid;
			
			if (tempstationid==stationid)
			{
				return  mystationlist.get(i);
			}
		}
		return null;
	}
	/**�õ���ʼվ*/
	public Station getFirstStation( )
	{
		Station mystation = null;
		if (mystationlist.size()>0)
			mystation = mystationlist.get(0) ;
		return mystation;
	}
	/**�õ��յ�վ*/
	public Station getEndStation( )
	{
		Station mystation = null;
		if (mystationlist.size()>0)
			mystation = mystationlist.get(mystationlist.size()-1) ;
		return mystation;
	}
	/**�õ� վ ����һվ    ��Ȧ*/
	public Station getNextStation( int stationid )
	{
		for (int i = 0;i<mystationlist.size();i++)
		{
			int tempstationid = mystationlist.get(i).stationid;
			
			if (tempstationid==stationid)
			{
				Station EndStation = getEndStation();
				if (EndStation==null)
				{
					
				}
				int endstationid = EndStation.stationid;
				
				if(endstationid==stationid)
				{
					return getFirstStation();
				}
				else
				{
					return  mystationlist.get(i+1);
				}
			}
		}
		return null;
	}
	/**toString */
	@Override
	public String toString() 
	{
		String str = "";
		str = "station_size:"+mystationlist.size()+"|";
		str += ("roadid:"+roadid+"|");
		for (int i = 0;i<mystationlist.size();i++)
		{
			int tempstationid = mystationlist.get(i).stationid;
			str += ("stationid:"+tempstationid+"|");
		}
		return str;
	}
}
