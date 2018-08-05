package model;

import java.util.ArrayList;
import java.util.List;

import config.Config;

import server.StationServer;

public class Car {
	/**************公共*******************/
	/** */
	public  StationServer myStationServer;
	
	/**************内容属性*******************/
	/**车号*/
	public String carid;
	/**路线id*/
	public int roadid;
	/**车当前的纬度*/
	public double nowLat;
	/**车当前的经度*/
	public double nowLon;
	/**下一站，静止的下一站就是本站*/
	public int nextstationid;
	/**下下一站*/
	public int nextnextstationid;
	/**行走或停止    1，静止，2行走   CARSTATUS_STOP = 1;CARSTATUS_MOVE = 2;*/
	public int nowstatus;
	/**动态空座*/
	public  List<Seat> seatlist;
	/**车所在路 */
	public Road myroad ;
	/**人数*/
	public  int  MAXSIZE;
	
	@Override
	public String toString() 
	{
		String str = "";
		
		str = "carid:"+carid+"|";
		str += ("roadid: "+roadid+"|");
		str += ("nextstationid: "+nextstationid+"|");
		str += ("nextnextstationid: "+nextnextstationid+"|");
		if (nowstatus==1)
			str += ("nowstatus: 静止");
		else
		{
			str += ("nowstatus: 行走");
		}
		return str;
	}
	
	/**初始化空座   */
	public boolean iniSeatList()
	{/*****初始化空座**************************************************/
		
		myroad = myStationServer.getRoadFromId(roadid);
		if (myroad==null)
		{
			return false;
		}
		//务必大于2站
		//单圈中，车是套圈的，但约车不是，即终点站和初始站两站不能约车。
		for (int i =  0;i<myroad.mystationlist.size()-1;i++)
		{
			Seat tempSeat = new Seat();
			tempSeat.startStationId = myroad.mystationlist.get(i).stationid;
			tempSeat.endStationId = myroad.mystationlist.get(i+1).stationid;
			tempSeat.seatsize = MAXSIZE;
			seatlist.add(tempSeat);
		}
		return true;
	}
	public Car(StationServer myStationServer) 
	{
		this.myStationServer =  myStationServer;
		seatlist = new ArrayList<Seat>();
		
		nowLat = -1;
		nowLon = -1;
	}
	/**更新车的位置    */
	public void refreshPositon( double lat, double lon) 
	{
		nowLat = lat;
		nowLon = lon;
	}
	
	/**得到路线上的下一站*/
	public int getNextStationid() 
	{
		return nextstationid;
	}
	/**得到路线上的下下一站*/
	public int getNextNextStationid() {
		return nextnextstationid;
	}
	/**更新车的站的情况    1，静止，2行走 */
	public void reFreshStatus(int nextstationid,int nowstatus) {
		this.nextstationid = nextstationid;
		this.nowstatus = nowstatus;
		
		int nextstationindex = -1;
		for (int i =  0;i<myroad.mystationlist.size();i++)
		{
			int tempStationId = myroad.mystationlist.get(i).stationid;
			if (tempStationId==this.nextstationid)
			{
				nextstationindex = i+1;
			}
		}
		if(nextstationindex>myroad.mystationlist.size()-1)
		{
			nextstationindex = 0;
		}
		nextnextstationid = myroad.mystationlist.get(nextstationindex).stationid;
	}


	/**完事*/
	public void jieHuoOver(List<Integer> stationslist, int num) 
	{
		for(int j = 0;j<seatlist.size();j++)
		{
			Integer tempstart = seatlist.get(j).startStationId;
			Integer tempend = seatlist.get(j).endStationId;
			if (stationslist.contains(tempstart) && stationslist.contains(tempend))
			{
				seatlist.get(j).seatsize+=num;
			}
		}
	}
	/**接活*/
	public void jieHuo(List<Integer> stationslist, int num) 
	{
		for(int j = 0;j<seatlist.size();j++)
		{
			Integer tempstart = seatlist.get(j).startStationId;
			Integer tempend = seatlist.get(j).endStationId;
			if (stationslist.contains(tempstart) && stationslist.contains(tempend))
			{
				seatlist.get(j).seatsize-=num;
			}
		}
	}
	/**可以接活吗    stationslist站ids   人数 */
	public boolean canJieHuo(List<Integer> stationslist,int num)
	{
		boolean tempflag = true;
		for(int j = 0;j<seatlist.size();j++)
		{
			Integer tempstart = seatlist.get(j).startStationId;
			Integer tempend = seatlist.get(j).endStationId;
			int tempsize = seatlist.get(j).seatsize;
			if (stationslist.contains(tempstart) && stationslist.contains(tempend))
			{
				if (tempsize<num)
				{
					tempflag = false;
					break;
				}
			}
		}
		return tempflag;
	}
}
