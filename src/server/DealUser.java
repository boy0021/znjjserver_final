package server;

import java.util.List;

import model.CMDUser;

public class DealUser {
	CarServer myCarServer;
	OrderServer myOrderServer;
	StationServer myStationServer;
	public DealUser(CarServer myCarServer,OrderServer myOrderServer,StationServer myStationServer) {
		this.myCarServer = myCarServer;
		this.myOrderServer = myOrderServer;
		this.myStationServer = myStationServer;
	}
	
	/**订单申请*/
	public CMDUser dealUserCMDApplyCar(String phonecode, int startstationid,
			int endstationid, int num) {
		//手机号就是 订单号		
		CMDUser myCMDUser  = new CMDUser();
		
		//myCarServer
		int roadid = myStationServer.getRoadIdFromStationId(startstationid);
		
		System.out.println("-----------------roadid:"+roadid);
		
		if (roadid<0)
		{
			
			return myCMDUser;
		}
			
		
		List<Integer> stationslist = myStationServer.getTwoStation(startstationid, endstationid, roadid);
		for (int i = 0;i<myCarServer.mycarlist.size();i++)
		{
			if ( myCarServer.mycarlist.get(i).canJieHuo(stationslist, num)  )
			{
				myCarServer.mycarlist.get(i).jieHuo(stationslist,num);
				String carid = myCarServer.mycarlist.get(i).carid;
				
				myOrderServer.createOrder(carid,phonecode,startstationid,endstationid,num);
				
				myCMDUser.cmd = 1;
			}
		}
		return myCMDUser;
	}
}
