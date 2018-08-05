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
	/**�����¼��־*/
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
		{//����
			myCMDCar =  doLoginOnline( carid,roadid,maxsize,lat,lon);
		}
		if (excmd==-1)
		{//��ê������
			myCMDCar =  doOffline(carid);
		}
		if (excmd==1)
		{//ԭ����ֹ�Ĵ���
			myCMDCar =  doStop(carid);
		}
		if (excmd==2)
		{//��ͣ������
			myCMDCar =  doGoStop(carid);
		}
		if (excmd==4)
		{//��ͣ���
			myCMDCar =  doGoStopOver(carid);
		}
		if (excmd==3)
		{//����ͣ��������
			myCMDCar =  doGoNotStop(carid);
		}
		if (excmd==6)
		{//����ͣ�����
			myCMDCar =  doGonotStopOver(carid);
		}
		
		/**���³���λ��*/
		Car mycar = myCarServer.getCarFromid(carid);
		if (mycar!=null)
			mycar.refreshPositon( lat, lon);
		
		return myCMDCar;
	}
	/**
	 * ����ͣ��������
	 * 1.��⳵�Ķ������Ƿ����(��һվ�ǵ�ͣ�ĵ���),
	 * */
	public CMDCar doGoNotStop(String carid)
	{
		Car myCar = myCarServer.getCarFromid(carid);
		int nextstationid = myCar.getNextStationid();
		int nextnextstationid = myCar.getNextNextStationid();
		
		CMDCar myCMDCar = new CMDCar();
		
		List<Order> myorderlist = myOrderServer.getCarOrderS( carid);
			
		if(myorderlist.size()<1)
		{//���޶�����˵���쳣��
			myCMDCar.ret = 1;
			myCMDCar.cmd = Config.error_code100001;
			return myCMDCar;
		}
		
		/**  -2,���쳣��-1,���쳣��  1�ȴ���2���������ϳ���3�ϳ���ɣ���ʻ�У���4������Ŀ�����³���5�³���ɣ��������� */
		//�ж���û�ж��� �� �������³���
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==1)
			{//�еȳ�����
				if (nextstationid == tempOrder.orderStartStationId)
				{//���ң��������һվ��id
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
	 * ����ͣ�����
	 * 1.���ĳ���״̬,��һվ
	 * 2.�ҵ���һվ�ĵ���
	 * ���� �����
	 * */
	public CMDCar doGonotStopOver(String carid)
	{
		Car myCar = myCarServer.getCarFromid(carid);
		//int nextstationid = myCar.getNextStationid();
		int nextnextstationid = myCar.getNextNextStationid();
		
		CMDCar myCMDCar = new CMDCar();
		
		List<Order> myorderlist = myOrderServer.getCarOrderS(carid);
			
		if(myorderlist.size()<1)
		{//���޶�����˵���쳣��
			myCMDCar.ret = 1;
			myCMDCar.cmd = Config.error_code100001;
			return myCMDCar;
		}
		
		/**  -2,���쳣��-1,���쳣��  1�ȴ���2���������ϳ���3�ϳ���ɣ���ʻ�У���4������Ŀ�����³���5�³���ɣ���������
		 * ��ͣ��ɣ��Ѿ����ܽ��ܶ����ˣ�
		//�ж���û�ж��� �� �������³���
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==1)
			{//�еȳ�����
				if (nextstationid == tempOrder.orderStartStationId)
				{//���ң��������һվ��id
					
					//��д����   ,�൱�ڵ�ͣ��
					myOrderServer.changeCarOrderStatus( carid, nextstationid) ;
					myCMDCar.ret = 1;
					myCMDCar.cmd = 1;
					return myCMDCar;
				}
			}
		} */
		
		//˵����û��ǡ��Լ�˸������ˣ���ô�ı�״̬  (��һվ������)
		myCar.reFreshStatus(nextnextstationid,2);
		
		/**  -2,���쳣��-1,���쳣��  1�ȴ���2���������ϳ���3�ϳ���ɣ���ʻ�У���4������Ŀ�����³���5�³���ɣ��������� */
		//�ж� ��һվ��û�����³���
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==1)
			{//�еȳ�����
				if (nextnextstationid == tempOrder.orderStartStationId )
				{//���ң��������һվ��id ����ʼվ�����ǽ���վ����һվ��
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
			{//�еȴ��³���
				if ( nextnextstationid == tempOrder.orderEndStationId)
				{//���ң��������һվ��id ����ʼվ�����ǽ���վ����һվ��
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
		//���û�е���������ô���Ǽ���������ͣ
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
	
	/**���  ��ͣ
	 *1����car����һվ��״̬
	 *2.���ؼ���ͣ
	 * */
	public CMDCar doGoStopOver(String carid)
	{
		/**
		 * ��Ϊ�������
		 * 
		 * */
		Car myCar = myCarServer.getCarFromid(carid);
		int nextstationid = myCar.getNextStationid();
		int nextnextstationid = myCar.getNextNextStationid();
		
		//д�ض����� 
		myOrderServer.changeCarOrderStatus( carid, nextstationid);
		
		myCar.reFreshStatus(nextstationid, 1);
		
		CMDCar myCMDCar = new CMDCar();
		myCMDCar.ret = 1;
		myCMDCar.cmd = 1;
		
		return myCMDCar;
	}
	/**��ʻ�У���Ŀ�ĵ�ͣ�� 
	 * 1.������������ =======���ҵ���������õ�վ��Ϣ��������
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
	/**ԭ����ֹ�Ĵ���  �ж���û�е���
	 * 1.�޵��ӣ�======>>>����ͣ
	 * 2.�е���,�Ǹ�վ�����³���δ�굥��  ======>>>����ͣ
	 * 3.�е���,���Ǹ�վ�����³���δ�굥��======>>>����  (����ͣ��ͣ)
	 * 4.�е���,�ı䳵��״̬��������
	 * */
	public CMDCar doStop(String carid)
	{
		CMDCar myCMDCar = new CMDCar();
		
		List<Order> myorderlist = myOrderServer.getCarOrderS( carid);
		
		if(myorderlist.size()<1)
		{//�޶���
			//����ͣ
			myCMDCar.ret = 1;
			myCMDCar.cmd = 1;
			return myCMDCar;
		}
		
		/**  -2,���쳣��-1,���쳣��  1�ȴ���2���������ϳ���3�ϳ���ɣ���ʻ�У���4������Ŀ�����³���5�³���ɣ��������� */
		//�ж���û�ж��� �� �������³���
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==2)
			{//�����������ϳ�
				//����ͣ
				myCMDCar.ret = 1;
				myCMDCar.cmd = 1;
				return myCMDCar;
			}
			if(tempOrder.status==4)
			{//�����������³�
				//����ͣ
				myCMDCar.ret = 1;
				myCMDCar.cmd = 1;
				return myCMDCar;
			}
		}
		
		/**��Ӧ��ȥ�ĳ�վ*/
		int firstStationid = Config.MaxStationId;
		//�ж���û�ж��� �� �еȳ�����
		//�ж���û�ж��� �� ���������ڳ��ϵĵ�
		for (int i = 0;i<myorderlist.size();i++)
		{
			Order tempOrder = myorderlist.get(i);
			if(tempOrder.status==1)
			{//�еȳ�����
				if (firstStationid>tempOrder.orderStartStationId)
				{
					firstStationid = tempOrder.orderStartStationId;
				}
			}
			if(tempOrder.status==3)
			{//��һվ�³�
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
		{//�����ͣ�ڵ���һվ�ȳ�
			myCMDCar.ret = 1;
			myCMDCar.cmd = 1;
			myOrderServer.changeCarOrderStatus( carid, nextstationid);
			
			return myCMDCar;
		}
		
		//��ִ�е�����˵������վ�������ˣ�����ǰ�����������
		if (firstStationid==nextnextstationid)
		{//��ͣ
			//д����״̬
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
			//д����״̬
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
	/**��������   �쳣����*/
	public CMDCar doOffline(String carid)
	{
		/**
		 * ȥ�������Ķ���
		 * ��myCarServer��ɾ��
		 * */
		myOrderServer.deleteCarAllOrder(carid);
		
		myCarServer.carOffline(carid);
		
		CMDCar myCMDCar = new CMDCar();
		myCMDCar.ret = 1;
		myCMDCar.cmd = 1;
		return myCMDCar;
	}
	/**��������*/
	public CMDCar doLoginOnline(String carid,int roadid,int maxsize,double lat,double lon)
	{
		/**
		 * ���������������ӵ�myCarServer��
		 * ��ʼ���������� 
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
			 * 1.���ӽ���
			 * 2.��ʼ����վ��״̬
			 * */
			Road myRoad = myStationServer.getRoadFromId(car.roadid);
			car.nextstationid = myRoad.getFirstStation().stationid;
			car.nextnextstationid = myRoad.getNextStation(car.nextstationid).stationid;
			car.nowstatus = 1;
			myCarServer.carOnline(car);
			myWriteLog.log(carid+" ��ʼ���ɹ���");
			myCMDCar.ret = 1;
			myCMDCar.cmd = 1;
		}
		else
		{
			myWriteLog.log(carid+" ·�Ų��ԣ������ݿ��в����ڣ�");
			myCMDCar.ret = -1;
			myCMDCar.cmd = Config.error_code100000 ;
		}
		return myCMDCar;
	}
}
