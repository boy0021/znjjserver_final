package model;

import java.util.ArrayList;
import java.util.List;

public class CMDUser {
	/**对人的命令*/
	public int ret;
	public int cmd;
	public int cmd2;
	public int orderid;
	public List<MyPosition> carspositions; 
	public List<MyPosition> stationpositions;
	public List<String> stationname; 
	/**第一个是本站，*/
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
