package server;

import java.util.List;

import javax.servlet.http.HttpServlet;

import config.Config;
import model.CMDCar;
import model.Car;
import model.Order;
import model.Road;
import model.Station;
import utl.WriteLog;

public class DealCar {
	/**方便记录日志*/
	WriteLog myWriteLog;
	public  CarServer myCarServer;
	public  StationServer myStationServer;
	
	OrderServer myOrderServer;
	public DealCar(HttpServlet myHttpServlet,CarServer myCarServer,StationServer myStationServer,OrderServer myOrderServer) {
		myWriteLog = new WriteLog(myHttpServlet,"DealCar");
		this.myCarServer = myCarServer;
		this.myStationServer = myStationServer;
		this.myOrderServer = myOrderServer;
	}
	public CMDCar dealCarCMD(String carid, int excmd, int excmdstationidId,
			int roadid, int maxsize, double lat, double lon) {
		CMDCar myCMDCar = new CMDCar();
		
		if (excmd==10)
		{//上线
			myCMDCar =  doLoginOnline( carid,roadid,maxsize,lat,lon);
		}
		if (excmd==-1)
		{//抛锚，下线
			myCMDCar =  doOffline(carid);
		}
		if (excmd==1)
		{//原来静止的处理
			myCMDCar =  doStop(carid);
		}
		if (excmd==2)
		{//到停过程中
			myCMDCar =  doGoStop(carid);
		}
		if (excmd==4)
		{//到停完成
			myCMDCar =  doGoStopOver(carid);
		}
		if (excmd==3)
		{//过不停，过程中
			myCMDCar =  doGoNotStop(carid);
		}
		if (excmd==6)
		{//过不停，完成
			myCMDCar =  doGonotStopOver(carid);
		}
		
		/**更新车的位置*/
		Car mycar = myCarServer.getCarFromid(carid);
		if (mycar!=null)
			mycar.refreshPositon( lat, lon);
		
		return myCMDCar;
	}
	/**
	 * 过不停，过程中
	 * 1.检测车的订单中是否存在(下一站是到停的单子),
	 * */
	public CMDCar doGoNotStop(String carid)
	{
		Car myCar = myCarServer.getCarFromid(carid);
		int nextstationid = myCar.getNextStationid();
		int nextnextstationid = myCar.getNextNextStationid();
		
		CMDCar myCMDCar = new CMDCar();
		
		List<Order> myorderlist = myOrderServer.getCarOrderS( carid);
			
		if(myorderlist.size()<1)
		{//若无订单，说明异常了
			myCMDCar.ret = 1;
			myCMDCar.cmd = Config.error_code100001;
			return myCMDCar;
		}
		
		/**  -2,车异常，-1,人异常，  1等待，2，到达请上车，3上车完成（行驶中），4，到达目的请下车，5下车完成，订单结束 */
		//判断有没有订单 是 正在上下车的
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==1)
			{//有等车来的
				if (nextstationid == tempOrder.orderStartStationId)
				{//并且，这好是下一站的id
					myCMDCar.ret = 1;
					myCMDCar.cmd = 2;
					myCMDCar.aim1stationid = nextstationid;
					Station myStation = myStationServer.getStation(myCar.roadid,nextstationid);
					myCMDCar.aim1lat = myStation.lat;
					myCMDCar.aim1lon = myStation.lon;
					return myCMDCar;
				}
			}
		}
		
		myCMDCar.ret = 1;
		myCMDCar.cmd = 3;
		myCMDCar.aim1stationid = nextstationid;
		Station myStation1 = myStationServer.getStation(myCar.roadid,nextstationid);
		myCMDCar.aim1lat = myStation1.lat;
		myCMDCar.aim1lon = myStation1.lon;
		myCMDCar.aim2stationid = nextnextstationid;
		Station myStation2 = myStationServer.getStation(myCar.roadid,nextnextstationid);
		myCMDCar.aim2lat =  myStation2.lat;
		myCMDCar.aim2lon = myStation2.lon;
		return myCMDCar;
	}
	/**
	 * 过不停，完成
	 * 1.更改车的状态,下一站
	 * 2.找到下一站的单子
	 * 返回 坐标等
	 * */
	public CMDCar doGonotStopOver(String carid)
	{
		Car myCar = myCarServer.getCarFromid(carid);
		//int nextstationid = myCar.getNextStationid();
		int nextnextstationid = myCar.getNextNextStationid();
		
		CMDCar myCMDCar = new CMDCar();
		
		List<Order> myorderlist = myOrderServer.getCarOrderS(carid);
			
		if(myorderlist.size()<1)
		{//若无订单，说明异常了
			myCMDCar.ret = 1;
			myCMDCar.cmd = Config.error_code100001;
			return myCMDCar;
		}
		
		/**  -2,车异常，-1,人异常，  1等待，2，到达请上车，3上车完成（行驶中），4，到达目的请下车，5下车完成，订单结束
		 * 到停完成，已经不能接受订单了，
		//判断有没有订单 是 正在上下车的
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==1)
			{//有等车来的
				if (nextstationid == tempOrder.orderStartStationId)
				{//并且，这好是下一站的id
					
					//回写订单   ,相当于到停了
					myOrderServer.changeCarOrderStatus( carid, nextstationid) ;
					myCMDCar.ret = 1;
					myCMDCar.cmd = 1;
					return myCMDCar;
				}
			}
		} */
		
		//说明，没有恰巧约了个单的人，那么改变状态  (下一站的问题)
		myCar.reFreshStatus(nextnextstationid,2);
		
		/**  -2,车异常，-1,人异常，  1等待，2，到达请上车，3上车完成（行驶中），4，到达目的请下车，5下车完成，订单结束 */
		//判断 下一站有没有上下车的
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==1)
			{//有等车来的
				if (nextnextstationid == tempOrder.orderStartStationId )
				{//并且，这好是下一站的id （开始站或者是结束站是下一站）
					myCMDCar.ret = 1;
					myCMDCar.cmd = 2;
					myCMDCar.aim1stationid = nextnextstationid;
					Station myStation = myStationServer.getStation(myCar.roadid,nextnextstationid);
					myCMDCar.aim1lat = myStation.lat;
					myCMDCar.aim1lon = myStation.lon;
					return myCMDCar;
				}
			}
			//
			if(tempOrder.status==3)
			{//有等待下车的
				if ( nextnextstationid == tempOrder.orderEndStationId)
				{//并且，这好是下一站的id （开始站或者是结束站是下一站）
					myCMDCar.ret = 1;
					myCMDCar.cmd = 2;
					myCMDCar.aim1stationid = nextnextstationid;
					Station myStation = myStationServer.getStation(myCar.roadid,nextnextstationid);
					myCMDCar.aim1lat = myStation.lat;
					myCMDCar.aim1lon = myStation.lon;
					return myCMDCar;
				}
			}
		}
		//如果没有单子来，那么就是继续过而不停
		myCMDCar.ret = 1;
		myCMDCar.cmd = 3;
		myCMDCar.aim1stationid = myCar.nextstationid;
		Station myStation1 = myStationServer.getStation(myCar.roadid,myCar.nextstationid);
		myCMDCar.aim1lat = myStation1.lat;
		myCMDCar.aim1lon = myStation1.lon;
		myCMDCar.aim2stationid = myCar.nextnextstationid;
		Station myStation2 = myStationServer.getStation(myCar.roadid,myCar.nextnextstationid);
		myCMDCar.aim2lat =  myStation2.lat;
		myCMDCar.aim2lon = myStation2.lon;
		return myCMDCar;
	}
	
	/**完成  到停
	 *1设置car的下一站和状态
	 *2.返回继续停
	 * */
	public CMDCar doGoStopOver(String carid)
	{
		/**
		 * 分为多种情况
		 * 
		 * */
		Car myCar = myCarServer.getCarFromid(carid);
		int nextstationid = myCar.getNextStationid();
		int nextnextstationid = myCar.getNextNextStationid();
		
		//写回订单表 
		myOrderServer.changeCarOrderStatus( carid, nextstationid);
		
		myCar.reFreshStatus(nextstationid, 1);
		
		CMDCar myCMDCar = new CMDCar();
		myCMDCar.ret = 1;
		myCMDCar.cmd = 1;
		
		return myCMDCar;
	}
	/**行驶中，在目的地停车 
	 * 1.不做处理，继走 =======》找到这个单，得到站信息，继续发
	 * */
	public CMDCar doGoStop(String carid)
	{
		CMDCar myCMDCar = new CMDCar();
		
		Car myCar = myCarServer.getCarFromid(carid);
		int nextstationid = myCar.getNextStationid();
		myCMDCar.ret = 1;
		myCMDCar.cmd = 2;
		myCMDCar.aim1stationid = nextstationid;
		Station myStation = myStationServer.getStation(myCar.roadid,nextstationid);
		myCMDCar.aim1lat = myStation.lat;
		myCMDCar.aim1lon = myStation.lon;
		
		return myCMDCar;
	}
	/**原来静止的处理  判断有没有单子
	 * 1.无单子，======>>>继续停
	 * 2.有单子,是该站的上下车的未完单子  ======>>>继续停
	 * 3.有单子,不是该站的上下车的未完单子======>>>开车  (过不停，停)
	 * 4.有单子,改变车的状态，和命令
	 * */
	public CMDCar doStop(String carid)
	{
		CMDCar myCMDCar = new CMDCar();
		
		List<Order> myorderlist = myOrderServer.getCarOrderS( carid);
		
		if(myorderlist.size()<1)
		{//无订单
			//继续停
			myCMDCar.ret = 1;
			myCMDCar.cmd = 1;
			return myCMDCar;
		}
		
		/**  -2,车异常，-1,人异常，  1等待，2，到达请上车，3上车完成（行驶中），4，到达目的请下车，5下车完成，订单结束 */
		//判断有没有订单 是 正在上下车的
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==2)
			{//有任务，正在上车
				//继续停
				myCMDCar.ret = 1;
				myCMDCar.cmd = 1;
				return myCMDCar;
			}
			if(tempOrder.status==4)
			{//有任务，正在下车
				//继续停
				myCMDCar.ret = 1;
				myCMDCar.cmd = 1;
				return myCMDCar;
			}
		}
		
		/**最应该去的车站*/
		int firstStationid = Config.MaxStationId;
		//判断有没有订单 是 有等车来的
		//判断有没有订单 是 有正在做在车上的的
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==1)
			{//有等车来的
				if (firstStationid>tempOrder.orderStartStationId)
				{
					firstStationid = tempOrder.orderStartStationId;
				}
			}
			if(tempOrder.status==3)
			{//下一站下车
				if (firstStationid>tempOrder.orderEndStationId)
				{
					firstStationid = tempOrder.orderEndStationId;
				}
			}
		}
		
		Car myCar = myCarServer.getCarFromid(carid);
		int nextstationid = myCar.getNextStationid();
		int nextnextstationid = myCar.getNextNextStationid();
		
		
		//
		
		if (firstStationid==nextstationid)
		{//到达，在停在的那一站等车
			myCMDCar.ret = 1;
			myCMDCar.cmd = 1;
			myOrderServer.changeCarOrderStatus( carid, nextstationid);
			
			return myCMDCar;
		}
		
		//能执行到这里说明，本站无任务了，并且前面是有任务的
		if (firstStationid==nextnextstationid)
		{//到停
			//写回其状态
			myCar.reFreshStatus(nextnextstationid, 2);
			
			myCMDCar.ret = 1;
			myCMDCar.cmd = 2;
			myCMDCar.aim1stationid = nextnextstationid;
			Station myStation = myStationServer.getStation(myCar.roadid,nextnextstationid);
			myCMDCar.aim1lat = myStation.lat;
			myCMDCar.aim1lon = myStation.lon;
			
			return myCMDCar;
		}
		else
		{
			//写回其状态
			myCar.reFreshStatus(nextnextstationid, 2);
			
			myCMDCar.ret = 1;
			myCMDCar.cmd = 3;
			myCMDCar.aim1stationid = nextnextstationid;
			Station myStation1 = myStationServer.getStation(myCar.roadid,nextnextstationid);
			myCMDCar.aim1lat = myStation1.lat;
			myCMDCar.aim1lon = myStation1.lon;
			
			
			myCMDCar.aim2stationid = myCar.getNextNextStationid();
			Station myStation2 = myStationServer.getStation(myCar.roadid,myCMDCar.aim2stationid);
			myCMDCar.aim2lat =  myStation2.lat;
			myCMDCar.aim2lon = myStation2.lon;
			
			return myCMDCar;
		}
	}
	/**处理下线   异常下线*/
	public CMDCar doOffline(String carid)
	{
		/**
		 * 去掉车辆的订单
		 * 从myCarServer中删除
		 * */
		myOrderServer.deleteCarAllOrder(carid);
		
		myCarServer.carOffline(carid);
		
		CMDCar myCMDCar = new CMDCar();
		myCMDCar.ret = 1;
		myCMDCar.cmd = 1;
		return myCMDCar;
	}
	/**处理上线*/
	public CMDCar doLoginOnline(String carid,int roadid,int maxsize,double lat,double lon)
	{
		/**
		 * 创建车辆对象，增加到myCarServer中
		 * 初始化车辆空座 
		 * */
		CMDCar myCMDCar = new CMDCar();
		
		Car car = new Car(myStationServer); 
		car.roadid = roadid;
		car.carid = carid;
		car.nowLat = lat;
		car.nowLon = lon;
		car.MAXSIZE = maxsize;
		if(car.iniSeatList())
		{
			/**
			 * 1.增加进来
			 * 2.初始化车站和状态
			 * */
			Road myRoad = myStationServer.getRoadFromId(car.roadid);
			car.nextstationid = myRoad.getFirstStation().stationid;
			car.nextnextstationid = myRoad.getNextStation(car.nextstationid).stationid;
			car.nowstatus = 1;
			myCarServer.carOnline(car);
			myWriteLog.log(carid+" 初始化成功！");
			myCMDCar.ret = 1;
			myCMDCar.cmd = 1;
		}
		else
		{
			myWriteLog.log(carid+" 路号不对，在数据库中不存在！");
			myCMDCar.ret = -1;
			myCMDCar.cmd = Config.error_code100000 ;
		}
		return myCMDCar;
	}
}
