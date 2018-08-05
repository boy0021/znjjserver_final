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
	/**�����Ĺ���*/
	OrderServer myOrderServer;
	/**վ�Ĺ���*/
	StationServer myStationServer;
	/**���Ĺ���*/
	CarServer myCarServer;
	/**�˵Ĺ���*/
	UserServer myUserServer;
	
	/**Ϊ�˷�������     �����д�������ķ�������*/
	DealCar  myDealCar;
	/**Ϊ�˷�������     �����д����û�����ķ�������*/
	DealUser  myDealUser;
	
	
	/**�����¼��־*/
	WriteLog myWriteLog;
	/**������*/
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
	{/**����*/
		
		lock.lock();
		//myWriteLog.print("���� ");
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
	
	//�û�����
	public CMDUser dealUserCMDHuxi(String orderid) 
	{
		CMDUser myCMDUser = new CMDUser();
		
		lock.lock();
		/**
		 * 1.���س�վ��    Լ���߼�ok����˵
		 * 2.������ �������³�
		 * */
		myCMDUser.ret = 1;
		myCMDUser.cmd = 7;
		
		int returnstatus = myOrderServer.getOrderStatus(orderid);
		
		/**  -2,���쳣��-1,���쳣��  1�ȴ���2���������ϳ���3�ϳ���ɣ���ʻ�У���4������Ŀ�����³���5�³���ɣ��������� */
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

	/**�û� �³��ɹ�*/
	public CMDUser dealUserCMDOffCar(String orderid) {
		lock.lock();
		CMDUser myCMDUser = new CMDUser();
		myCMDUser.ret = 1;
		myCMDUser.cmd = 5;
		
		myOrderServer.changeUserOrderStatus( orderid);
		lock.unlock();
		return myCMDUser;
	}
	/**����    �ϳ����*/
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

	/**��������*/
	public CMDUser dealUserCMDApplyCar(String phonecode, int startstationid,int endstationid, int num) 
	{
		lock.lock();
		System.out.println("-----------------roadid:");
		CMDUser myCMDUser = myDealUser.dealUserCMDApplyCar(phonecode,startstationid,endstationid,num);
		lock.unlock();
		return myCMDUser;
	}
	/** ����վ*/
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
	/** �������е��ڴ�����*/
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
	/** ����  ��֤��*/
	public void doApplyCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		
		String phonecode = request.getParameter("phonecode");
		
		String userkey = RandomCode.getRandNumCode();
		JSONObject jsonobject =  new JSONObject();
		if (myUserServer.isExists(phonecode))
		{//���ڣ��ͷ���ʧ�ܣ���Ŀǰû���˳��û���һѡ�
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
	
	/** ��֤ ��֤��*/
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
	/** �ϻ��������*/
	public void UserOnline(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//����  �� 1�� ������֤�룻 2�� ��֤��֤��
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
	
	
	/** ����Android*/
	public void CarAndroid(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
			{
		String carid = request.getParameter("carid");
		JSONObject jsonobject =  new JSONObject();
		try 
		{
			Car mycar = myCarServer.getCarInfor(carid);
			
			//
			//��ֹ��������  1����ֹ��2���� 
			int status = mycar.nowstatus;
			
			//��һվ ��ʲô
			int nextstationid = mycar.nextstationid;
			
			int roadid = myStationServer.getRoadIdFromStationId(nextstationid);
			
			Station myStation = myStationServer.getStation(roadid, nextstationid);
			String stationname = "����Ϣ��";
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
