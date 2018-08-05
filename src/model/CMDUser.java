package model;

import java.util.ArrayList;
import java.util.List;

public class CMDUser {
	/**���˵�����*/
	public int ret;
	public int cmd;
	public int cmd2;
	public int orderid;
	public List<MyPosition> carspositions; 
	public List<MyPosition> stationpositions;
	public List<String> stationname; 
	/**��һ���Ǳ�վ��*/
	public List<Station> mystationlist;
	
	public Station thismystationlist;
	public CMDUser() {
		ret = 0;
		cmd = 0;
		cmd2 = 0;
		orderid = 0;
		
		carspositions = new ArrayList<MyPosition>();
		stationname = new ArrayList<String>();
		stationpositions = new ArrayList<MyPosition>();
		mystationlist = new ArrayList<Station>();
	}
}
