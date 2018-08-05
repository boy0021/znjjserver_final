package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import config.Config;
import model.CMDCar;
import model.CMDUser;
import model.Car;
import model.Road;
import model.Station;

import utl.RandomCode;
import utl.WriteLog;


public class ManagerServer {
	/**订单的管理*/
	OrderServer myOrderServer;
	/**站的管理*/
	StationServer myStationServer;
	/**车的管理*/
	CarServer myCarServer;
	/**人的管理*/
	UserServer myUserServer;
	
	/**为了分流代码     将所有处理车请求的放在这里*/
	DealCar  myDealCar;
	/**为了分流代码     将所有处理用户请求的放在这里*/
	DealUser  myDealUser;
	
	
	/**方便记录日志*/
	WriteLog myWriteLog;
	/**锁对象*/
	private Lock lock = new ReentrantLock(); 
	public ManagerServer(HttpServlet myHttpServlet) {
		
		
		myWriteLog = new WriteLog(myHttpServlet,"ManagerServer");
		//myWriteLog.print("ManagerServer");
		myWriteLog.log("ManagerServer start");
		myStationServer = new StationServer(myHttpServlet);
		myStationServer.inidata();
		ThreadBreath myThreadBreath = new ThreadBreath(this);
		myThreadBreath.start();
		
		myCarServer = new CarServer();
		myUserServer = new UserServer();
		
		myOrderServer = new OrderServer(myCarServer,myUserServer);
		
		myDealCar = new DealCar( myHttpServlet,myCarServer,myStationServer,myOrderServer);
		myDealUser = new DealUser(myCarServer,myOrderServer,myStationServer);
	}
	
	public void doApply()
	{/**呼吸*/
		
		lock.lock();
		//myWriteLog.print("运行 ");
		lock.unlock();
	}

	public CMDCar dealCarCMD(String caridS, int excmdI, int excmdstationidId,
			int roadidId, int maxseatsizeI, double latD, double lonD) {
		lock.lock();
		
		CMDCar myCMDCar =  myDealCar.dealCarCMD(caridS,excmdI,excmdstationidId,
				roadidId,maxseatsizeI,latD,lonD	);
		lock.unlock();
		return myCMDCar;
	}
	
	//用户呼吸
	public CMDUser dealUserCMDHuxi(String orderid) 
	{
		CMDUser myCMDUser = new CMDUser();
		
		lock.lock();
		/**
		 * 1.返回车站等    约车逻辑ok后再说
		 * 2.车到达 可以上下车
		 * */
		myCMDUser.ret = 1;
		myCMDUser.cmd = 7;
		
		int returnstatus = myOrderServer.getOrderStatus(orderid);
		
		/**  -2,车异常，-1,人异常，  1等待，2，到达请上车，3上车完成（行驶中），4，到达目的请下车，5下车完成，订单结束 */
		if (returnstatus==2)
			myCMDUser.cmd2 = 2;
		if (returnstatus==4)
			myCMDUser.cmd2 = 4;
		/***/
		myCMDUser.stationpositions = myStationServer.getStationsPosition();
		try {
			myCMDUser.carspositions = myCarServer.getcarPosition();
		} catch (Exception e) {
			myWriteLog.log(" eeeeeeeeeeeeeeeeeeeeeeeeeeee:  |"+e.toString());
		}
		
		
		lock.unlock();
		return myCMDUser;
	}

	/**用户 下车成功*/
	public CMDUser dealUserCMDOffCar(String orderid) {
		lock.lock();
		CMDUser myCMDUser = new CMDUser();
		myCMDUser.ret = 1;
		myCMDUser.cmd = 5;
		
		myOrderServer.changeUserOrderStatus( orderid);
		lock.unlock();
		return myCMDUser;
	}
	/**处理    上车完成*/
	public CMDUser dealUserCMDOnCar(String orderid) 
	{
		lock.lock();
		CMDUser myCMDUser = new CMDUser();
		myCMDUser.ret = 1;
		myCMDUser.cmd = 3;
		
		myOrderServer.changeUserOrderStatus( orderid);
		
		lock.unlock();
		return myCMDUser;
	}

	/**订单申请*/
	public CMDUser dealUserCMDApplyCar(String phonecode, int startstationid,int endstationid, int num) 
	{
		lock.lock();
		System.out.println("-----------------roadid:");
		CMDUser myCMDUser = myDealUser.dealUserCMDApplyCar(phonecode,startstationid,endstationid,num);
		lock.unlock();
		return myCMDUser;
	}
	/** 请求车站*/
	public CMDUser dealUserCMDApplyStation(String codestring) 
	{
		lock.lock();
		CMDUser myCMDUser = new CMDUser();
		myCMDUser.ret = 1;
		myCMDUser.cmd = 6;
		
		Station thisStation = myStationServer.getStationFromcodestring(  codestring ) ;
		if(thisStation!=null)
			System.out.println(":-----------------:"+thisStation.roadid);
		Road myRoad = myStationServer.getRoadFromId(thisStation.roadid);
		
		List<Station> mytempstationlist = new ArrayList<Station>();
		
		//System.out.println(":-----------------:"+myRoad.mystationlist.size());
		if (myRoad.mystationlist.size()>0)
		{
			//System.out.println(":-----------------:");
			myCMDUser.ret = 1;
			myCMDUser.cmd = 6;
			for (int i = 0;i<myRoad.mystationlist.size();i++)
			{
				if (myRoad.mystationlist.get(i).stationid >= thisStation.stationid)
				{//
					//System.out.println(":------333333-----------:");
					mytempstationlist.add(myRoad.mystationlist.get(i));
				}
				//System.out.println(":------3334444333-----------:");
			}
			//System.out.println(":------3334444333-----------:");
			myCMDUser.mystationlist = mytempstationlist;
			//System.out.println(":---------234234-------:");
		}
		else
		{
			//System.out.println(":--------3243---------:");
			myCMDUser.ret = -1;
			myCMDUser.cmd = -1;
		}
		lock.unlock();
		return myCMDUser;
	}
	
	
	public void doUserOnline(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		/*
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		JSONObject jsonobject =  new JSONObject();
		
		jsonobject.put("myOrderServer", myOrderServer.toString());
		jsonobject.put("myStationServer", myStationServer.toString());
		jsonobject.put("myCarServer", myCarServer.toString());
		jsonobject.put("myUserServer", myUserServer.toString());
		
		response.getWriter().println(jsonobject.toString());
		*/
	}
	/** 返回所有的内存数据*/
	public void doTestApply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		JSONObject jsonobject =  new JSONObject();
		
		jsonobject.put("myOrderServer", myOrderServer.toString());
		jsonobject.put("myStationServer", myStationServer.toString());
		jsonobject.put("myCarServer", myCarServer.toString());
		jsonobject.put("myUserServer", myUserServer.toString());
		
		response.getWriter().println(jsonobject.toString());
	}
	/** 申请  验证码*/
	public void doApplyCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		
		String phonecode = request.getParameter("phonecode");
		
		String userkey = RandomCode.getRandNumCode();
		JSONObject jsonobject =  new JSONObject();
		if (myUserServer.isExists(phonecode))
		{//存在，就返回失败，（目前没有退出用户这一选项）
			jsonobject.put("ret", -1 );
			//userkey = myUserServer.findcode(usercode);
		}
		else
		{//
			userkey = myUserServer.addUser(phonecode,userkey);
			jsonobject.put("ret", 1 );
			jsonobject.put("keycode", userkey );
		}
		response.getWriter().println(jsonobject.toString());
	}
	
	/** 验证 验证码*/
	public void doConfirmCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		
		String phonecode = request.getParameter("phonecode");
		String usercode = request.getParameter("keycode");
		//System.out.println(":--------3243--@@-------:"+phonecode+"  "+usercode);
		JSONObject jsonobject =  new JSONObject();
		if (myUserServer.compareCode(phonecode,usercode))
		{
			jsonobject.put("ret", 1 );
		}
		else
		{
			jsonobject.put("ret", -1 );
		}
		response.getWriter().println(jsonobject.toString());
	}
	/** 上户上线相关*/
	public void UserOnline(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//步骤  ： 1： 申请验证码； 2： 验证验证码
		String step = request.getParameter("step");
		try 
		{
			int step_i = Integer.valueOf(step);
			
			if (step_i==1)
			{
				
				 doApplyCode( request,  response);
			}
			if (step_i==2)
			{
				doConfirmCode(request,response);
			} 
		}
		catch (Exception e) 
		{
			JSONObject jsonobject =  new JSONObject();
			jsonobject.put("ret", -1 );
			response.getWriter().println(jsonobject.toString());
		}
	}
	
	
	/** 车载Android*/
	public void CarAndroid(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
			{
		String carid = request.getParameter("carid");
		JSONObject jsonobject =  new JSONObject();
		try 
		{
			Car mycar = myCarServer.getCarInfor(carid);
			
			//
			//静止或者行走  1，静止，2行走 
			int status = mycar.nowstatus;
			
			//下一站 是什么
			int nextstationid = mycar.nextstationid;
			
			int roadid = myStationServer.getRoadIdFromStationId(nextstationid);
			
			Station myStation = myStationServer.getStation(roadid, nextstationid);
			String stationname = "无信息！";
			if (myStation!=null)
			{
				stationname = myStation.name;
			}
			
			jsonobject.put("ret", 1 );
			jsonobject.put("status", status );
			jsonobject.put("stationname", stationname);
		}
		catch (Exception e) 
		{
			jsonobject.put("ret", -1 );
		}
		response.getWriter().println(jsonobject.toString());
	}
}
