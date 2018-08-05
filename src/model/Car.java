package model;

import java.util.ArrayList;
import java.util.List;

import config.Config;

import server.StationServer;

public class Car {
	/**************����*******************/
	/** */
	public  StationServer myStationServer;
	
	/**************��������*******************/
	/**����*/
	public String carid;
	/**·��id*/
	public int roadid;
	/**����ǰ��γ��*/
	public double nowLat;
	/**����ǰ�ľ���*/
	public double nowLon;
	/**��һվ����ֹ����һվ���Ǳ�վ*/
	public int nextstationid;
	/**����һվ*/
	public int nextnextstationid;
	/**���߻�ֹͣ    1����ֹ��2����   CARSTATUS_STOP = 1;CARSTATUS_MOVE = 2;*/
	public int nowstatus;
	/**��̬����*/
	public  List<Seat> seatlist;
	/**������· */
	public Road myroad ;
	/**����*/
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
			str += ("nowstatus: ��ֹ");
		else
		{
			str += ("nowstatus: ����");
		}
		return str;
	}
	
	/**��ʼ������   */
	public boolean iniSeatList()
	{/*****��ʼ������**************************************************/
		
		myroad = myStationServer.getRoadFromId(roadid);
		if (myroad==null)
		{
			return false;
		}
		//��ش���2վ
		//��Ȧ�У�������Ȧ�ģ���Լ�����ǣ����յ�վ�ͳ�ʼվ��վ����Լ����
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
	/**���³���λ��    */
	public void refreshPositon( double lat, double lon) 
	{
		nowLat = lat;
		nowLon = lon;
	}
	
	/**�õ�·���ϵ���һվ*/
	public int getNextStationid() 
	{
		return nextstationid;
	}
	/**�õ�·���ϵ�����һվ*/
	public int getNextNextStationid() {
		return nextnextstationid;
	}
	/**���³���վ�����    1����ֹ��2���� */
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


	/**����*/
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
	/**�ӻ�*/
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
	/**���Խӻ���    stationslistվids   ���� */
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
